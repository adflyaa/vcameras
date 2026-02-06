package virtual.camera.app.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.Surface
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Virtual Camera Manager
 * Manages fake camera feed (photo/video) injection
 * This is the core component that provides fake camera frames
 */
class VirtualCameraManager private constructor(private val context: Context) {

    companion object {
        private const val TAG = "VirtualCameraManager"
        
        @Volatile
        private var instance: VirtualCameraManager? = null

        fun getInstance(context: Context): VirtualCameraManager {
            return instance ?: synchronized(this) {
                instance ?: VirtualCameraManager(context.applicationContext).also { instance = it }
            }
        }
    }

    // Camera feed type
    enum class FeedType {
        DISABLED,       // Normal camera
        PHOTO,          // Static photo
        VIDEO_LOCAL,    // Local video file
        VIDEO_NETWORK   // Network video stream
    }

    // Playback state
    enum class PlaybackState {
        IDLE,
        PLAYING,
        PAUSED,
        STOPPED
    }

    // Current configuration
    private var feedType: FeedType = FeedType.DISABLED
    private var feedSource: String? = null
    private var audioEnabled: Boolean = true
    
    // Media player for video playback
    private var mediaPlayer: MediaPlayer? = null
    private var currentBitmap: Bitmap? = null
    
    // Playback state
    private var playbackState: PlaybackState = PlaybackState.IDLE
    private val isInitialized = AtomicBoolean(false)
    
    // Playback controls
    private var playbackSpeed: Float = 1.0f
    private var isLooping: Boolean = true
    
    // Callback for frame updates
    private var frameCallback: ((Bitmap?) -> Unit)? = null

    /**
     * Initialize camera manager
     */
    fun initialize() {
        if (isInitialized.getAndSet(true)) return
        Log.d(TAG, "VirtualCameraManager initialized")
    }

    /**
     * Configure camera feed
     */
    fun configure(type: FeedType, source: String?, audioEnabled: Boolean = true) {
        this.feedType = type
        this.feedSource = source
        this.audioEnabled = audioEnabled
        
        Log.d(TAG, "Configured: type=$type, source=$source, audio=$audioEnabled")
        
        // Prepare feed based on type
        when (type) {
            FeedType.PHOTO -> preparePhoto(source)
            FeedType.VIDEO_LOCAL -> prepareVideo(source)
            FeedType.VIDEO_NETWORK -> prepareNetworkVideo(source)
            FeedType.DISABLED -> cleanup()
        }
    }

    /**
     * Prepare photo feed
     */
    private fun preparePhoto(photoPath: String?) {
        cleanup()
        
        if (photoPath.isNullOrEmpty()) {
            Log.e(TAG, "Photo path is empty")
            return
        }

        try {
            val file = File(photoPath)
            if (!file.exists()) {
                Log.e(TAG, "Photo file not found: $photoPath")
                return
            }

            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeFile(photoPath, options)

            // Calculate inSampleSize to avoid OOM
            options.inSampleSize = calculateInSampleSize(options, 1920, 1080)
            options.inJustDecodeBounds = false

            currentBitmap = BitmapFactory.decodeFile(photoPath, options)
            playbackState = PlaybackState.PLAYING
            
            Log.d(TAG, "Photo prepared: ${currentBitmap?.width}x${currentBitmap?.height}")
        } catch (e: Exception) {
            Log.e(TAG, "Error preparing photo", e)
        }
    }

    /**
     * Prepare local video feed
     */
    private fun prepareVideo(videoPath: String?) {
        cleanup()
        
        if (videoPath.isNullOrEmpty()) {
            Log.e(TAG, "Video path is empty")
            return
        }

        try {
            val file = File(videoPath)
            if (!file.exists()) {
                Log.e(TAG, "Video file not found: $videoPath")
                return
            }

            mediaPlayer = MediaPlayer().apply {
                setDataSource(videoPath)
                isLooping = this@VirtualCameraManager.isLooping
                setVolume(if (audioEnabled) 1.0f else 0.0f, if (audioEnabled) 1.0f else 0.0f)
                prepare()
                
                setOnPreparedListener {
                    Log.d(TAG, "Video prepared: duration=${it.duration}ms")
                }
                
                setOnErrorListener { mp, what, extra ->
                    Log.e(TAG, "MediaPlayer error: what=$what, extra=$extra")
                    false
                }
            }
            
            playbackState = PlaybackState.IDLE
            Log.d(TAG, "Video player prepared")
        } catch (e: Exception) {
            Log.e(TAG, "Error preparing video", e)
        }
    }

    /**
     * Prepare network video stream
     */
    private fun prepareNetworkVideo(url: String?) {
        cleanup()
        
        if (url.isNullOrEmpty()) {
            Log.e(TAG, "Network URL is empty")
            return
        }

        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(url)
                isLooping = this@VirtualCameraManager.isLooping
                setVolume(if (audioEnabled) 1.0f else 0.0f, if (audioEnabled) 1.0f else 0.0f)
                prepareAsync()
                
                setOnPreparedListener {
                    Log.d(TAG, "Network video prepared: duration=${it.duration}ms")
                    playbackState = PlaybackState.IDLE
                }
                
                setOnErrorListener { mp, what, extra ->
                    Log.e(TAG, "Network video error: what=$what, extra=$extra")
                    false
                }
            }
            
            Log.d(TAG, "Network video player preparing...")
        } catch (e: Exception) {
            Log.e(TAG, "Error preparing network video", e)
        }
    }

    /**
     * Start playback
     */
    fun play() {
        when (feedType) {
            FeedType.PHOTO -> {
                playbackState = PlaybackState.PLAYING
                frameCallback?.invoke(currentBitmap)
            }
            FeedType.VIDEO_LOCAL, FeedType.VIDEO_NETWORK -> {
                mediaPlayer?.let { player ->
                    if (!player.isPlaying) {
                        player.start()
                        playbackState = PlaybackState.PLAYING
                        Log.d(TAG, "Playback started")
                    }
                }
            }
            else -> Log.w(TAG, "Cannot play: feed type is $feedType")
        }
    }

    /**
     * Pause playback
     */
    fun pause() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
                playbackState = PlaybackState.PAUSED
                Log.d(TAG, "Playback paused")
            }
        }
    }

    /**
     * Stop playback
     */
    fun stop() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.stop()
            }
            playbackState = PlaybackState.STOPPED
            Log.d(TAG, "Playback stopped")
        }
    }

    /**
     * Seek to position (in milliseconds)
     */
    fun seekTo(positionMs: Int) {
        mediaPlayer?.let { player ->
            player.seekTo(positionMs)
            Log.d(TAG, "Seeked to $positionMs ms")
        }
    }

    /**
     * Seek forward by delta (in milliseconds)
     */
    fun seekForward(deltaMs: Int = 5000) {
        mediaPlayer?.let { player ->
            val newPosition = (player.currentPosition + deltaMs).coerceAtMost(player.duration)
            player.seekTo(newPosition)
            Log.d(TAG, "Seeked forward by $deltaMs ms to $newPosition ms")
        }
    }

    /**
     * Seek backward by delta (in milliseconds)
     */
    fun seekBackward(deltaMs: Int = 5000) {
        mediaPlayer?.let { player ->
            val newPosition = (player.currentPosition - deltaMs).coerceAtLeast(0)
            player.seekTo(newPosition)
            Log.d(TAG, "Seeked backward by $deltaMs ms to $newPosition ms")
        }
    }

    /**
     * Set playback speed (requires API 23+)
     */
    fun setPlaybackSpeed(speed: Float) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            mediaPlayer?.let { player ->
                try {
                    player.playbackParams = player.playbackParams.setSpeed(speed)
                    this.playbackSpeed = speed
                    Log.d(TAG, "Playback speed set to $speed")
                } catch (e: Exception) {
                    Log.e(TAG, "Error setting playback speed", e)
                }
            }
        } else {
            Log.w(TAG, "Playback speed control requires API 23+")
        }
    }

    /**
     * Set looping
     */
    fun setLooping(looping: Boolean) {
        this.isLooping = looping
        mediaPlayer?.isLooping = looping
        Log.d(TAG, "Looping set to $looping")
    }

    /**
     * Set audio enabled/disabled
     */
    fun setAudioEnabled(enabled: Boolean) {
        this.audioEnabled = enabled
        val volume = if (enabled) 1.0f else 0.0f
        mediaPlayer?.setVolume(volume, volume)
        Log.d(TAG, "Audio set to $enabled")
    }

    /**
     * Get current position (in milliseconds)
     */
    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    /**
     * Get duration (in milliseconds)
     */
    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }

    /**
     * Get current frame as Bitmap
     */
    fun getCurrentFrame(): Bitmap? {
        return when (feedType) {
            FeedType.PHOTO -> currentBitmap
            FeedType.VIDEO_LOCAL, FeedType.VIDEO_NETWORK -> {
                // Extract frame from video at current position
                extractVideoFrame(feedSource, getCurrentPosition())
            }
            else -> null
        }
    }

    /**
     * Extract frame from video at specific time
     */
    private fun extractVideoFrame(videoPath: String?, timeMs: Int): Bitmap? {
        if (videoPath == null) return null
        
        return try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(videoPath)
            val bitmap = retriever.getFrameAtTime(timeMs * 1000L, MediaMetadataRetriever.OPTION_CLOSEST)
            retriever.release()
            bitmap
        } catch (e: Exception) {
            Log.e(TAG, "Error extracting video frame", e)
            null
        }
    }

    /**
     * Set frame callback for continuous updates
     */
    fun setFrameCallback(callback: (Bitmap?) -> Unit) {
        this.frameCallback = callback
    }

    /**
     * Get playback state
     */
    fun getPlaybackState(): PlaybackState {
        return playbackState
    }

    /**
     * Get feed type
     */
    fun getFeedType(): FeedType {
        return feedType
    }

    /**
     * Check if playing
     */
    fun isPlaying(): Boolean {
        return playbackState == PlaybackState.PLAYING
    }

    /**
     * Calculate sample size for bitmap loading
     */
    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    /**
     * Cleanup resources
     */
    fun cleanup() {
        try {
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.stop()
                }
                player.release()
            }
            mediaPlayer = null
            
            currentBitmap?.recycle()
            currentBitmap = null
            
            playbackState = PlaybackState.IDLE
            
            Log.d(TAG, "Cleanup completed")
        } catch (e: Exception) {
            Log.e(TAG, "Error during cleanup", e)
        }
    }

    /**
     * Release all resources
     */
    fun release() {
        cleanup()
        frameCallback = null
        isInitialized.set(false)
        instance = null
        Log.d(TAG, "VirtualCameraManager released")
    }
}
