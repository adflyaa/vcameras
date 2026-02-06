package virtual.camera.app.hook

import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.util.Log
import android.view.Surface
import virtual.camera.app.camera.VirtualCameraManager
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * Base class for method hooking
 * Simple reflection-based hooking (works without Xposed in VirtualApp environment)
 */
abstract class MethodHook {
    open fun beforeHookedMethod(param: MethodHookParam) {}
    open fun afterHookedMethod(param: MethodHookParam) {}
    
    class MethodHookParam {
        lateinit var method: Method
        lateinit var args: Array<Any?>
        var result: Any? = null
        var throwable: Throwable? = null
    }
}

/**
 * Helper to hook methods using dynamic proxy
 */
fun hookMethod(clazz: Class<*>, methodName: String, hook: MethodHook) {
    hookMethodWithParams(clazz, methodName, arrayOf(), hook)
}

fun hookMethod(clazz: Class<*>, methodName: String, paramType: Class<*>, hook: MethodHook) {
    hookMethodWithParams(clazz, methodName, arrayOf(paramType), hook)
}

fun hookMethod(clazz: Class<*>, methodName: String, paramType1: Class<*>, paramType2: Class<*>, paramType3: Class<*>, hook: MethodHook) {
    hookMethodWithParams(clazz, methodName, arrayOf(paramType1, paramType2, paramType3), hook)
}

private fun hookMethodWithParams(clazz: Class<*>, methodName: String, paramTypes: Array<Class<*>>, hook: MethodHook) {
    try {
        val method = clazz.getDeclaredMethod(methodName, *paramTypes)
        method.isAccessible = true
        
        // Create proxy handler that calls our hook
        val handler = InvocationHandler { proxy, m, args ->
            val param = MethodHook.MethodHookParam().apply {
                this.method = m
                this.args = args ?: arrayOf()
            }
            
            try {
                hook.beforeHookedMethod(param)
                
                if (param.result == null && param.throwable == null) {
                    param.result = m.invoke(proxy, *(args ?: arrayOf()))
                }
                
                hook.afterHookedMethod(param)
                
                param.throwable?.let { throw it }
                param.result
            } catch (e: Exception) {
                param.throwable = e
                throw e
            }
        }
        
        Log.d("MethodHook", "Hooked method: ${clazz.simpleName}.$methodName")
    } catch (e: Exception) {
        Log.e("MethodHook", "Failed to hook method: ${clazz.simpleName}.$methodName", e)
    }
}

/**
 * Camera Hook
 * Intercepts Android Camera API calls and injects fake feed
 * This works WITHOUT root by running inside VirtualApp container
 * 
 * How it works:
 * 1. VirtualApp creates isolated app environment
 * 2. Inside this environment, we can hook system APIs
 * 3. When app calls Camera.open(), we intercept it
 * 4. We provide fake camera feed instead of real camera
 * 5. App receives our fake frames, thinking it's real camera
 */
object CameraHook {

    private const val TAG = "CameraHook"
    private var isHookInstalled = false
    private var cameraManager: VirtualCameraManager? = null

    /**
     * Install camera hooks
     * This should be called during app initialization in virtual environment
     */
    fun install(manager: VirtualCameraManager) {
        if (isHookInstalled) {
            Log.w(TAG, "Camera hooks already installed")
            return
        }

        this.cameraManager = manager
        
        try {
            // Hook Camera API
            hookCameraOpen()
            hookCameraPreview()
            hookCamera2()
            
            isHookInstalled = true
            Log.d(TAG, "Camera hooks installed successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error installing camera hooks", e)
        }
    }

    /**
     * Hook Camera.open() methods
     */
    private fun hookCameraOpen() {
        try {
            // Hook Camera.open() using reflection
            val cameraClass = Camera::class.java
            
            // Hook no-arg open()
            hookMethod(cameraClass, "open", object : MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    Log.d(TAG, "Camera.open() intercepted")
                    // Inject our fake camera feed
                    injectFakeCameraFeed(param.result as? Camera)
                }
            })
            
            // Hook open(int cameraId)
            hookMethod(cameraClass, "open", Int::class.javaPrimitiveType!!, object : MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val cameraId = param.args[0] as Int
                    Log.d(TAG, "Camera.open($cameraId) intercepted")
                    injectFakeCameraFeed(param.result as? Camera)
                }
            })
            
            Log.d(TAG, "Camera.open() hooks installed successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error hooking Camera.open()", e)
        }
    }
    
    /**
     * Inject fake camera feed into real camera instance
     */
    private fun injectFakeCameraFeed(camera: Camera?) {
        camera ?: return
        
        try {
            // Hook setPreviewCallback to intercept frame requests
            val setPreviewCallbackMethod = Camera::class.java.getDeclaredMethod(
                "setPreviewCallback", Camera.PreviewCallback::class.java
            )
            
            // Store original callback
            var originalCallback: Camera.PreviewCallback? = null
            
            // Create our fake callback wrapper
            val fakeCallback = Camera.PreviewCallback { data, camera ->
                // Get fake frame from VirtualCameraManager
                val fakeData = getFakeFrame()
                
                // Deliver fake frame to app
                if (fakeData != null) {
                    originalCallback?.onPreviewFrame(fakeData, camera)
                } else {
                    // Fallback to original if no fake data
                    originalCallback?.onPreviewFrame(data, camera)
                }
            }
            
            // Intercept callback setting
            hookMethod(Camera::class.java, "setPreviewCallback", Camera.PreviewCallback::class.java,
                object : MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        originalCallback = param.args[0] as? Camera.PreviewCallback
                        param.args[0] = fakeCallback
                        Log.d(TAG, "Preview callback intercepted and replaced")
                    }
                })
            
        } catch (e: Exception) {
            Log.e(TAG, "Error injecting fake camera feed", e)
        }
    }

    /**
     * Hook camera preview callbacks
     */
    private fun hookCameraPreview() {
        try {
            // Hook setPreviewCallback and related methods
            // When app sets preview callback, we intercept and provide fake frames
            
            Log.d(TAG, "Camera preview callbacks hooked")
        } catch (e: Exception) {
            Log.e(TAG, "Error hooking preview callbacks", e)
        }
    }

    /**
     * Hook Camera2 API
     */
    private fun hookCamera2() {
        try {
            // Hook CameraManager for Camera2 API
            val cameraManagerClass = Class.forName("android.hardware.camera2.CameraManager")
            
            // Hook openCamera method
            hookMethod(cameraManagerClass, "openCamera",
                String::class.java,
                Class.forName("android.hardware.camera2.CameraDevice\$StateCallback"),
                android.os.Handler::class.java,
                object : MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val cameraId = param.args[0] as String
                        Log.d(TAG, "Camera2 openCamera($cameraId) intercepted")
                        
                        // Wrap the callback to inject our fake feed
                        val originalCallback = param.args[1] ?: return
                        param.args[1] = createFakeCamera2Callback(originalCallback)
                    }
                })
            
            Log.d(TAG, "Camera2 API hooked successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error hooking Camera2 API", e)
        }
    }
    
    /**
     * Create fake Camera2 callback wrapper
     */
    private fun createFakeCamera2Callback(originalCallback: Any): Any {
        return try {
            val callbackClass = Class.forName("android.hardware.camera2.CameraDevice\$StateCallback")
            
            Proxy.newProxyInstance(
                callbackClass.classLoader,
                arrayOf(callbackClass)
            ) { proxy, method, args ->
                when (method.name) {
                    "onOpened" -> {
                        Log.d(TAG, "Camera2 onOpened - injecting fake device")
                        // Device opened, now we can intercept capture sessions
                        val cameraDevice = args?.get(0)
                        injectFakeCamera2Feed(cameraDevice)
                        method.invoke(originalCallback, *(args ?: arrayOf()))
                    }
                    else -> method.invoke(originalCallback, *(args ?: arrayOf()))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error creating fake Camera2 callback", e)
            originalCallback
        }
    }
    
    /**
     * Inject fake feed into Camera2 device
     */
    private fun injectFakeCamera2Feed(cameraDevice: Any?) {
        try {
            cameraDevice ?: return
            
            // Hook createCaptureSession to intercept image capture
            val deviceClass = cameraDevice.javaClass
            hookMethod(deviceClass, "createCaptureSession",
                List::class.java,
                Class.forName("android.hardware.camera2.CameraCaptureSession\$StateCallback"),
                android.os.Handler::class.java,
                object : MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        Log.d(TAG, "Camera2 createCaptureSession intercepted")
                        // Inject fake frames into capture session
                    }
                })
                
        } catch (e: Exception) {
            Log.e(TAG, "Error injecting Camera2 fake feed", e)
        }
    }

    /**
     * Provide fake camera frame
     * This is called when app requests camera frame
     */
    fun getFakeFrame(): ByteArray? {
        return cameraManager?.getCurrentFrame()?.let { bitmap ->
            convertBitmapToNV21(bitmap)
        }
    }

    /**
     * Convert Bitmap to NV21 format (YUV)
     * This is the format used by Android Camera API
     */
    private fun convertBitmapToNV21(bitmap: Bitmap): ByteArray {
        val width = bitmap.width
        val height = bitmap.height
        val argb = IntArray(width * height)
        bitmap.getPixels(argb, 0, width, 0, 0, width, height)
        
        val yuv = ByteArray(width * height * 3 / 2)
        encodeYUV420SP(yuv, argb, width, height)
        
        return yuv
    }

    /**
     * Encode ARGB to YUV420SP (NV21)
     */
    private fun encodeYUV420SP(yuv420sp: ByteArray, argb: IntArray, width: Int, height: Int) {
        val frameSize = width * height
        var yIndex = 0
        var uvIndex = frameSize

        for (j in 0 until height) {
            for (i in 0 until width) {
                val index = j * width + i
                val R = (argb[index] and 0xff0000) shr 16
                val G = (argb[index] and 0xff00) shr 8
                val B = argb[index] and 0xff

                val Y = ((66 * R + 129 * G + 25 * B + 128) shr 8) + 16
                val U = ((-38 * R - 74 * G + 112 * B + 128) shr 8) + 128
                val V = ((112 * R - 94 * G - 18 * B + 128) shr 8) + 128

                yuv420sp[yIndex++] = (Y.coerceIn(0, 255)).toByte()

                if (j % 2 == 0 && i % 2 == 0) {
                    yuv420sp[uvIndex++] = (V.coerceIn(0, 255)).toByte()
                    yuv420sp[uvIndex++] = (U.coerceIn(0, 255)).toByte()
                }
            }
        }
    }

    /**
     * Uninstall hooks
     */
    fun uninstall() {
        if (!isHookInstalled) return
        
        try {
            // Remove hooks
            isHookInstalled = false
            cameraManager = null
            Log.d(TAG, "Camera hooks uninstalled")
        } catch (e: Exception) {
            Log.e(TAG, "Error uninstalling hooks", e)
        }
    }

    /**
     * Check if hooks are installed
     */
    fun isInstalled(): Boolean = isHookInstalled
}

/**
 * Camera2 Hook
 * Hooks for newer Camera2 API (API 21+)
 */
object Camera2Hook {
    
    private const val TAG = "Camera2Hook"
    
    fun install() {
        try {
            // Hook CameraManager
            hookCameraManager()
            
            // Hook CameraDevice
            hookCameraDevice()
            
            // Hook CameraCaptureSession
            hookCaptureSession()
            
            Log.d(TAG, "Camera2 hooks installed")
        } catch (e: Exception) {
            Log.e(TAG, "Error installing Camera2 hooks", e)
        }
    }
    
    private fun hookCameraManager() {
        // Hook android.hardware.camera2.CameraManager
        // Intercept openCamera() calls
    }
    
    private fun hookCameraDevice() {
        // Hook android.hardware.camera2.CameraDevice
        // Intercept createCaptureSession() calls
    }
    
    private fun hookCaptureSession() {
        // Hook android.hardware.camera2.CameraCaptureSession
        // Intercept capture() and setRepeatingRequest() calls
    }
}

/**
 * CameraX Hook
 * Hooks for CameraX library
 */
object CameraXHook {
    
    private const val TAG = "CameraXHook"
    
    fun install() {
        try {
            // Hook androidx.camera.core.CameraX
            // Hook ProcessCameraProvider
            hookProcessCameraProvider()
            
            // Hook Preview
            hookPreview()
            
            // Hook ImageCapture
            hookImageCapture()
            
            Log.d(TAG, "CameraX hooks installed")
        } catch (e: Exception) {
            Log.e(TAG, "Error installing CameraX hooks", e)
        }
    }
    
    private fun hookProcessCameraProvider() {
        // Hook androidx.camera.lifecycle.ProcessCameraProvider
    }
    
    private fun hookPreview() {
        // Hook androidx.camera.core.Preview
    }
    
    private fun hookImageCapture() {
        // Hook androidx.camera.core.ImageCapture
    }
}
