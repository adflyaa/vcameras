package virtual.camera.camera;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Multi-user SharedPreferences wrapper for camera settings
 * This manages camera replacement settings across different virtual user spaces
 */
public class MultiPreferences {
    
    private static final String PREFS_NAME = "vcamera_multi_prefs";
    private static MultiPreferences instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    
    private MultiPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    
    /**
     * Get singleton instance
     */
    public static synchronized MultiPreferences getInstance() {
        if (instance == null) {
            throw new IllegalStateException("MultiPreferences not initialized. Call initialize() first.");
        }
        return instance;
    }
    
    /**
     * Initialize MultiPreferences with application context
     */
    public static synchronized void initialize(Context context) {
        if (instance == null) {
            instance = new MultiPreferences(context.getApplicationContext());
        }
    }
    
    /**
     * Check if MultiPreferences is initialized
     */
    public static boolean isInitialized() {
        return instance != null;
    }
    
    // String operations
    
    public void setString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }
    
    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }
    
    // Integer operations
    
    public void setInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }
    
    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }
    
    // Boolean operations
    
    public void setBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }
    
    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }
    
    // Long operations
    
    public void setLong(String key, long value) {
        editor.putLong(key, value);
        editor.apply();
    }
    
    public long getLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }
    
    // Float operations
    
    public void setFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.apply();
    }
    
    public float getFloat(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }
    
    // Utility operations
    
    /**
     * Remove a specific key
     */
    public void remove(String key) {
        editor.remove(key);
        editor.apply();
    }
    
    /**
     * Clear all preferences
     */
    public void clear() {
        editor.clear();
        editor.apply();
    }
    
    /**
     * Check if key exists
     */
    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }
    
    /**
     * Get all keys
     */
    public java.util.Set<String> getAll() {
        return sharedPreferences.getAll().keySet();
    }
    
    /**
     * Commit changes synchronously (use sparingly)
     */
    public boolean commit() {
        return editor.commit();
    }
}
