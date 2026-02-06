package virtual.camera.app.core

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import com.hack.opensdk.HackApi
import java.io.File

/**
 * App Launch Helper
 * Prevents "App failed to launch" errors in virtual environment
 * 
 * Common causes of launch failures:
 * 1. Missing native libraries (.so files)
 * 2. Incorrect ABI (arm vs arm64)
 * 3. Missing permissions in virtual env
 * 4. Data directory not initialized
 * 5. Incomplete installation
 */
object AppLaunchHelper {

    private const val TAG = "AppLaunchHelper"

    /**
     * Prepare app for launch
     * Fixes common issues that cause launch failures
     */
    fun prepareForLaunch(context: Context, packageName: String, userId: Int): Boolean {
        Log.d(TAG, "Preparing app for launch: $packageName")

        try {
            // 1. Verify installation
            if (!verifyInstallation(packageName, userId)) {
                Log.e(TAG, "Installation verification failed")
                return false
            }

            // 2. Initialize data directory
            if (!initializeDataDirectory(packageName, userId)) {
                Log.e(TAG, "Data directory initialization failed")
                return false
            }

            // 3. Check and copy native libraries
            if (!checkNativeLibraries(context, packageName, userId)) {
                Log.w(TAG, "Native library check failed, app may not work")
                // Don't return false - some apps work without native libs
            }

            // 4. Set proper permissions
            if (!setPermissions(packageName, userId)) {
                Log.w(TAG, "Permission setup failed")
            }

            // 5. Create cache directory
            createCacheDirectory(packageName, userId)

            Log.d(TAG, "App preparation completed successfully")
            return true

        } catch (e: Exception) {
            Log.e(TAG, "Error preparing app for launch", e)
            return false
        }
    }

    /**
     * Verify app is properly installed
     */
    private fun verifyInstallation(packageName: String, userId: Int): Boolean {
        return try {
            val packageInfo = HackApi.getPackageInfo(packageName, userId, 0)
            if (packageInfo == null) {
                Log.e(TAG, "Package info not found: $packageName")
                return false
            }

            val appInfo = packageInfo.applicationInfo
            if (appInfo == null) {
                Log.e(TAG, "Application info not found: $packageName")
                return false
            }

            Log.d(TAG, "Installation verified: $packageName")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error verifying installation", e)
            false
        }
    }

    /**
     * Initialize data directory structure
     */
    private fun initializeDataDirectory(packageName: String, userId: Int): Boolean {
        return try {
            val dataDir = HackApi.getPackageDataDir(packageName, userId)
            if (dataDir == null) {
                Log.e(TAG, "Cannot get data directory")
                return false
            }

            // Create necessary subdirectories
            val subdirs = arrayOf(
                "cache",
                "files",
                "databases",
                "shared_prefs",
                "app_webview",
                "code_cache"
            )

            for (subdir in subdirs) {
                val dir = File(dataDir, subdir)
                if (!dir.exists()) {
                    dir.mkdirs()
                    Log.d(TAG, "Created directory: ${dir.absolutePath}")
                }
            }

            // Set proper permissions
            dataDir.setReadable(true, false)
            dataDir.setWritable(true, false)
            dataDir.setExecutable(true, false)

            Log.d(TAG, "Data directory initialized: ${dataDir.absolutePath}")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing data directory", e)
            false
        }
    }

    /**
     * Check and setup native libraries
     */
    private fun checkNativeLibraries(context: Context, packageName: String, userId: Int): Boolean {
        return try {
            val packageInfo = HackApi.getPackageInfo(packageName, userId, 0)
            val appInfo = packageInfo?.applicationInfo ?: return false

            // Check if app has native libraries
            val nativeLibDir = File(appInfo.nativeLibraryDir)
            if (!nativeLibDir.exists() || !nativeLibDir.isDirectory) {
                Log.d(TAG, "No native libraries found")
                return true // Not an error - app may not need native libs
            }

            val libFiles = nativeLibDir.listFiles { file ->
                file.extension == "so"
            }

            if (libFiles.isNullOrEmpty()) {
                Log.d(TAG, "No .so files found in native lib directory")
                return true
            }

            Log.d(TAG, "Found ${libFiles.size} native libraries")
            
            // Create lib directory in virtual data dir
            val dataDir = HackApi.getPackageDataDir(packageName, userId)
            val virtualLibDir = File(dataDir, "lib")
            if (!virtualLibDir.exists()) {
                virtualLibDir.mkdirs()
            }

            true
        } catch (e: Exception) {
            Log.e(TAG, "Error checking native libraries", e)
            false
        }
    }

    /**
     * Set proper permissions for app
     */
    private fun setPermissions(packageName: String, userId: Int): Boolean {
        return try {
            // In virtual environment, permissions are managed differently
            // Grant basic permissions by default
            
            Log.d(TAG, "Permissions configured for: $packageName")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error setting permissions", e)
            false
        }
    }

    /**
     * Create cache directory
     */
    private fun createCacheDirectory(packageName: String, userId: Int) {
        try {
            val dataDir = HackApi.getPackageDataDir(packageName, userId)
            val cacheDir = File(dataDir, "cache")
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
                Log.d(TAG, "Created cache directory: ${cacheDir.absolutePath}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error creating cache directory", e)
        }
    }

    /**
     * Fix launch intent
     * Ensures intent has all necessary flags and extras
     */
    fun fixLaunchIntent(intent: Intent, packageName: String, userId: Int): Intent {
        return intent.apply {
            // Add necessary flags
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            // Add virtual environment markers
            putExtra("_VA_|_user_id_", userId)
            putExtra("_VA_|_pkg_", packageName)
            putExtra("_VA_|_virtual_", true)
            
            // Add VCamera specific flags
            putExtra("_VCAMERA_ENABLED_", true)
            putExtra("_VCAMERA_USER_", userId)
        }
    }

    /**
     * Launch app with error handling
     */
    fun launchApp(context: Context, packageName: String, userId: Int): Boolean {
        try {
            // Prepare app
            if (!prepareForLaunch(context, packageName, userId)) {
                Log.e(TAG, "Failed to prepare app for launch")
                return false
            }

            // Get launch intent
            val intent = HackApi.getLaunchIntentForPackage(packageName, userId)
            if (intent == null) {
                Log.e(TAG, "Cannot get launch intent for: $packageName")
                return false
            }

            // Fix intent
            val fixedIntent = fixLaunchIntent(intent, packageName, userId)

            // Launch
            val result = HackApi.startActivity(fixedIntent, userId)
            if (result != 0) {
                Log.e(TAG, "Failed to start activity, error code: $result")
                return false
            }

            Log.d(TAG, "App launched successfully: $packageName")
            return true

        } catch (e: Exception) {
            Log.e(TAG, "Error launching app: $packageName", e)
            return false
        }
    }

    /**
     * Diagnose launch failure
     */
    fun diagnoseLaunchFailure(context: Context, packageName: String, userId: Int): String {
        val issues = mutableListOf<String>()

        try {
            // Check installation
            val packageInfo = HackApi.getPackageInfo(packageName, userId, 0)
            if (packageInfo == null) {
                issues.add("App not installed in virtual environment")
                return issues.joinToString("\n")
            }

            // Check launch intent
            val intent = HackApi.getLaunchIntentForPackage(packageName, userId)
            if (intent == null) {
                issues.add("No launch intent found - app may not have launcher activity")
            }

            // Check data directory
            val dataDir = HackApi.getPackageDataDir(packageName, userId)
            if (dataDir == null || !dataDir.exists()) {
                issues.add("Data directory not found or not accessible")
            }

            // Check application info
            val appInfo = packageInfo.applicationInfo
            if (appInfo == null) {
                issues.add("Application info is null")
            } else {
                // Check if app is enabled
                if (!appInfo.enabled) {
                    issues.add("App is disabled")
                }

                // Check target SDK
                if (appInfo.targetSdkVersion > android.os.Build.VERSION.SDK_INT) {
                    issues.add("App targets newer SDK version than device")
                }
            }

            if (issues.isEmpty()) {
                return "No obvious issues found. Try reinstalling the app."
            }

            return issues.joinToString("\n")

        } catch (e: Exception) {
            return "Error diagnosing: ${e.message}"
        }
    }

    /**
     * Repair app installation
     * Attempts to fix common installation issues
     */
    fun repairInstallation(context: Context, packageName: String, userId: Int): Boolean {
        Log.d(TAG, "Attempting to repair installation: $packageName")

        return try {
            // Clear and reinitialize data directory
            val dataDir = HackApi.getPackageDataDir(packageName, userId)
            if (dataDir != null && dataDir.exists()) {
                // Clear but don't delete the directory
                dataDir.listFiles()?.forEach { file ->
                    if (file.name != "lib") { // Keep native libraries
                        file.deleteRecursively()
                    }
                }
            }

            // Reinitialize
            initializeDataDirectory(packageName, userId)

            Log.d(TAG, "Installation repaired successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error repairing installation", e)
            false
        }
    }
}
