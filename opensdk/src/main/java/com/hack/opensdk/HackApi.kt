package com.hack.opensdk

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * Main API for virtual environment operations
 * Based on VirtualApp/VirtualXposed architecture
 * This provides virtual app container functionality
 */
object HackApi {

    private var isInitialized = false
    private lateinit var appContext: Context
    
    // Virtual package storage
    private val virtualPackages = ConcurrentHashMap<String, VirtualPackageInfo>()
    private val userSpaces = ConcurrentHashMap<Int, VirtualUserSpace>()
    
    // Default user space
    private const val DEFAULT_USER_ID = 0
    
    /**
     * Virtual package info container
     */
    private data class VirtualPackageInfo(
        val packageInfo: PackageInfo,
        val installedUsers: MutableSet<Int> = mutableSetOf(),
        val dataDir: File? = null
    )
    
    /**
     * Virtual user space container
     */
    private data class VirtualUserSpace(
        val userId: Int,
        val packages: MutableSet<String> = mutableSetOf()
    )

    /**
     * Initialize the virtual environment
     */
    @JvmStatic
    fun initialize(application: Application) {
        if (isInitialized) return
        appContext = application.applicationContext
        
        // Create default user space
        userSpaces[DEFAULT_USER_ID] = VirtualUserSpace(DEFAULT_USER_ID)
        
        // Load system packages as available for virtualization
        loadSystemPackages()
        
        isInitialized = true
    }
    
    /**
     * Load available system packages
     */
    private fun loadSystemPackages() {
        try {
            val pm = appContext.packageManager
            val packages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pm.getInstalledPackages(PackageManager.PackageInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION")
                pm.getInstalledPackages(0)
            }
            
            // Store real packages info for potential virtual installation
            packages.forEach { pkg ->
                if (!pkg.packageName.startsWith("com.android.") && 
                    pkg.applicationInfo != null) {
                    virtualPackages[pkg.packageName] = VirtualPackageInfo(pkg)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Get list of installed packages in virtual environment
     */
    @JvmStatic
    fun getInstalledPackages(flags: Int, userId: Int): List<PackageInfo> {
        if (!isInitialized) return emptyList()
        
        val userSpace = userSpaces[userId] ?: return emptyList()
        
        return virtualPackages.values
            .filter { userSpace.packages.contains(it.packageInfo.packageName) }
            .map { it.packageInfo }
    }

    /**
     * Get package info for a specific package
     */
    @JvmStatic
    fun getPackageInfo(packageName: String, userId: Int, flags: Int): PackageInfo? {
        if (!isInitialized) return null
        
        val userSpace = userSpaces[userId] ?: return null
        if (!userSpace.packages.contains(packageName)) return null
        
        return virtualPackages[packageName]?.packageInfo
    }

    /**
     * Install package from host system into virtual environment
     */
    @JvmStatic
    fun installPackageFromHost(packageName: String, userId: Int, force: Boolean): Boolean {
        if (!isInitialized) return false
        
        try {
            // Get package from real system
            val pm = appContext.packageManager
            val realPackage = try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    pm.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
                } else {
                    @Suppress("DEPRECATION")
                    pm.getPackageInfo(packageName, 0)
                }
            } catch (e: Exception) {
                null
            }
            
            if (realPackage == null) return false
            
            // Add to virtual packages if not exists
            if (!virtualPackages.containsKey(packageName)) {
                virtualPackages[packageName] = VirtualPackageInfo(realPackage)
            }
            
            // Create user space if not exists
            if (!userSpaces.containsKey(userId)) {
                userSpaces[userId] = VirtualUserSpace(userId)
            }
            
            // Install in user space
            val userSpace = userSpaces[userId]!!
            userSpace.packages.add(packageName)
            
            // Create virtual data directory
            val dataDir = File(appContext.filesDir, "virtual/data/$userId/$packageName")
            if (!dataDir.exists()) {
                dataDir.mkdirs()
            }
            
            virtualPackages[packageName]?.dataDir?.let { }
            
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    /**
     * Install package from APK file
     */
    @JvmStatic
    fun installPackage(apkPath: String, userId: Int, flags: Int): Boolean {
        if (!isInitialized) return false
        
        try {
            val file = File(apkPath)
            if (!file.exists() || !file.canRead()) return false
            
            val pm = appContext.packageManager
            val packageInfo = pm.getPackageArchiveInfo(apkPath, 0) ?: return false
            
            val packageName = packageInfo.packageName ?: return false
            
            // Store package info
            virtualPackages[packageName] = VirtualPackageInfo(packageInfo)
            
            // Create user space if needed
            if (!userSpaces.containsKey(userId)) {
                userSpaces[userId] = VirtualUserSpace(userId)
            }
            
            // Add to user space
            userSpaces[userId]?.packages?.add(packageName)
            
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    /**
     * Uninstall package from virtual environment
     */
    @JvmStatic
    fun uninstallPackage(packageName: String, userId: Int): Boolean {
        if (!isInitialized) return false
        
        try {
            val userSpace = userSpaces[userId] ?: return false
            userSpace.packages.remove(packageName)
            
            // Clean up data directory
            deletePackageData(packageName, userId)
            
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    /**
     * Delete all data for a package
     */
    @JvmStatic
    fun deletePackageData(packageName: String, userId: Int): Boolean {
        if (!isInitialized) return false
        
        try {
            val dataDir = File(appContext.filesDir, "virtual/data/$userId/$packageName")
            return deleteRecursive(dataDir)
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
    
    /**
     * Recursively delete directory
     */
    private fun deleteRecursive(fileOrDirectory: File): Boolean {
        if (fileOrDirectory.isDirectory) {
            fileOrDirectory.listFiles()?.forEach { child ->
                deleteRecursive(child)
            }
        }
        return fileOrDirectory.delete()
    }

    /**
     * Get launch intent for package
     */
    @JvmStatic
    fun getLaunchIntentForPackage(packageName: String, userId: Int): Intent? {
        if (!isInitialized) return null
        
        val userSpace = userSpaces[userId] ?: return null
        if (!userSpace.packages.contains(packageName)) return null
        
        return try {
            val pm = appContext.packageManager
            pm.getLaunchIntentForPackage(packageName)?.apply {
                // Add flags for virtual environment
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                putExtra("_VA_|_user_id_", userId)
                putExtra("_VA_|_virtual_", true)
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Start activity in virtual environment
     */
    @JvmStatic
    fun startActivity(intent: Intent, userId: Int): Int {
        if (!isInitialized) return -1
        
        return try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("_VA_|_user_id_", userId)
            intent.putExtra("_VA_|_virtual_", true)
            appContext.startActivity(intent)
            0 // Success
        } catch (e: Exception) {
            e.printStackTrace()
            -1 // Failure
        }
    }

    /**
     * Get available user spaces (multi-user support)
     */
    @JvmStatic
    fun getAvailableUserSpace(): List<Int> {
        if (!isInitialized) return emptyList()
        return userSpaces.keys.toList().sorted()
    }

    /**
     * Create new user space
     */
    @JvmStatic
    fun createUserSpace(userId: Int): Boolean {
        if (!isInitialized) return false
        
        if (userSpaces.containsKey(userId)) return false
        
        userSpaces[userId] = VirtualUserSpace(userId)
        
        // Create data directory for user
        val userDir = File(appContext.filesDir, "virtual/data/$userId")
        if (!userDir.exists()) {
            userDir.mkdirs()
        }
        
        return true
    }

    /**
     * Delete user space
     */
    @JvmStatic
    fun deleteUserSpace(userId: Int): Boolean {
        if (!isInitialized) return false
        if (userId == DEFAULT_USER_ID) return false // Can't delete default user
        
        userSpaces.remove(userId)
        
        // Delete user data directory
        val userDir = File(appContext.filesDir, "virtual/data/$userId")
        return deleteRecursive(userDir)
    }

    /**
     * Get application info
     */
    @JvmStatic
    fun getApplicationInfo(packageName: String, userId: Int, flags: Int): ApplicationInfo? {
        if (!isInitialized) return null
        
        val packageInfo = getPackageInfo(packageName, userId, flags)
        return packageInfo?.applicationInfo
    }

    /**
     * Get package icon
     */
    @JvmStatic
    fun getPackageIcon(packageName: String, userId: Int): Drawable? {
        if (!isInitialized) return null
        
        return try {
            val pm = appContext.packageManager
            val appInfo = pm.getApplicationInfo(packageName, 0)
            appInfo.loadIcon(pm)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Get package label
     */
    @JvmStatic
    fun getPackageLabel(packageName: String, userId: Int): String? {
        if (!isInitialized) return null
        
        return try {
            val pm = appContext.packageManager
            val appInfo = pm.getApplicationInfo(packageName, 0)
            appInfo.loadLabel(pm).toString()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Check if package is running
     */
    @JvmStatic
    fun isPackageRunning(packageName: String, userId: Int): Boolean {
        if (!isInitialized) return false
        // In real implementation, would check ActivityManager
        return false
    }

    /**
     * Kill package process
     */
    @JvmStatic
    fun killPackageProcess(packageName: String, userId: Int): Boolean {
        if (!isInitialized) return false
        // In real implementation, would kill process
        return true
    }

    /**
     * Get data directory for package
     */
    @JvmStatic
    fun getPackageDataDir(packageName: String, userId: Int): File? {
        if (!isInitialized) return null
        
        val dataDir = File(appContext.filesDir, "virtual/data/$userId/$packageName")
        if (!dataDir.exists()) {
            dataDir.mkdirs()
        }
        return dataDir
    }

    /**
     * Check if running in virtual environment
     */
    @JvmStatic
    fun isVirtualEnvironment(): Boolean {
        return isInitialized
    }

    /**
     * Get current user ID
     */
    @JvmStatic
    fun getCurrentUserId(): Int {
        return DEFAULT_USER_ID
    }
    
    /**
     * Check if package is installed in virtual environment
     */
    @JvmStatic
    fun isPackageInstalled(packageName: String, userId: Int): Boolean {
        if (!isInitialized) return false
        val userSpace = userSpaces[userId] ?: return false
        return userSpace.packages.contains(packageName)
    }
    
    /**
     * Get all packages in system (not just virtual)
     */
    @JvmStatic
    fun getAllPackages(): List<String> {
        if (!isInitialized) return emptyList()
        return virtualPackages.keys.toList()
    }
    
    /**
     * Clear all virtual data (for testing/reset)
     */
    @JvmStatic
    fun clearAllVirtualData(): Boolean {
        if (!isInitialized) return false
        
        try {
            // Keep default user but clear its packages
            userSpaces.forEach { (userId, userSpace) ->
                if (userId != DEFAULT_USER_ID) {
                    deleteUserSpace(userId)
                } else {
                    userSpace.packages.clear()
                }
            }
            
            // Clear virtual data directory
            val virtualDir = File(appContext.filesDir, "virtual")
            return deleteRecursive(virtualDir)
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}
