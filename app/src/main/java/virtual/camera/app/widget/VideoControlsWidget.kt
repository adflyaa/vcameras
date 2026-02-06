package virtual.camera.app.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import virtual.camera.app.R
import virtual.camera.app.camera.VirtualCameraManager
import java.util.concurrent.TimeUnit

/**
 * Video Controls Widget
 * Provides play/pause/seek/speed controls for fake video feed
 */
class VideoControlsWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var btnPlayPause: ImageButton? = null
    private var btnForward: ImageButton? = null
    private var btnBackward: ImageButton? = null
    private var btnSpeed: ImageButton? = null
    private var seekBar: SeekBar? = null
    private var tvCurrentTime: TextView? = null
    private var tvDuration: TextView? = null

    private var cameraManager: VirtualCameraManager? = null
    private var isTracking = false
    private var updateRunnable: Runnable? = null

    private var currentSpeed = 1.0f
    private val speedOptions = floatArrayOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f)
    private var currentSpeedIndex = 2 // 1.0x

    init {
        // Note: This would need actual layout XML
        // For now, we'll create views programmatically
        initializeViews()
        setupListeners()
    }

    private fun initializeViews() {
        // In a real implementation, this would inflate from XML
        // For now, creating placeholder structure
    }

    private fun setupListeners() {
        btnPlayPause?.setOnClickListener {
            togglePlayPause()
        }

        btnForward?.setOnClickListener {
            cameraManager?.seekForward(5000) // 5 seconds forward
        }

        btnBackward?.setOnClickListener {
            cameraManager?.seekBackward(5000) // 5 seconds backward
        }

        btnSpeed?.setOnClickListener {
            cycleSpeed()
        }

        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    tvCurrentTime?.text = formatTime(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isTracking = true
                stopUpdating()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isTracking = false
                seekBar?.progress?.let { progress ->
                    cameraManager?.seekTo(progress)
                }
                startUpdating()
            }
        })
    }

    /**
     * Bind to VirtualCameraManager
     */
    fun bindToCameraManager(manager: VirtualCameraManager) {
        this.cameraManager = manager
        updateUI()
        startUpdating()
    }

    /**
     * Toggle play/pause
     */
    private fun togglePlayPause() {
        cameraManager?.let { manager ->
            if (manager.isPlaying()) {
                manager.pause()
                updatePlayPauseButton(false)
            } else {
                manager.play()
                updatePlayPauseButton(true)
            }
        }
    }

    /**
     * Cycle through playback speeds
     */
    private fun cycleSpeed() {
        currentSpeedIndex = (currentSpeedIndex + 1) % speedOptions.size
        currentSpeed = speedOptions[currentSpeedIndex]
        cameraManager?.setPlaybackSpeed(currentSpeed)
        updateSpeedButton()
    }

    /**
     * Update play/pause button icon
     */
    private fun updatePlayPauseButton(isPlaying: Boolean) {
        // In real implementation, would update icon
        // btnPlayPause?.setImageResource(if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play)
    }

    /**
     * Update speed button text
     */
    private fun updateSpeedButton() {
        // In real implementation, would update text
        // btnSpeed?.text = "${currentSpeed}x"
    }

    /**
     * Update UI with current state
     */
    private fun updateUI() {
        cameraManager?.let { manager ->
            val duration = manager.getDuration()
            val currentPosition = manager.getCurrentPosition()

            seekBar?.max = duration
            seekBar?.progress = currentPosition

            tvCurrentTime?.text = formatTime(currentPosition)
            tvDuration?.text = formatTime(duration)

            updatePlayPauseButton(manager.isPlaying())
        }
    }

    /**
     * Start periodic UI updates
     */
    private fun startUpdating() {
        stopUpdating()
        updateRunnable = object : Runnable {
            override fun run() {
                if (!isTracking) {
                    updateUI()
                }
                postDelayed(this, 100) // Update every 100ms
            }
        }
        post(updateRunnable)
    }

    /**
     * Stop periodic updates
     */
    private fun stopUpdating() {
        updateRunnable?.let { removeCallbacks(it) }
        updateRunnable = null
    }

    /**
     * Format time in milliseconds to MM:SS
     */
    private fun formatTime(timeMs: Int): String {
        val totalSeconds = TimeUnit.MILLISECONDS.toSeconds(timeMs.toLong())
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    /**
     * Show controls
     */
    fun show() {
        visibility = VISIBLE
        startUpdating()
    }

    /**
     * Hide controls
     */
    fun hide() {
        visibility = GONE
        stopUpdating()
    }

    /**
     * Cleanup
     */
    fun cleanup() {
        stopUpdating()
        cameraManager = null
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cleanup()
    }
}
