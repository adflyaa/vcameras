# VCamera Troubleshooting Guide 🔧

## Common Build Errors and Solutions

### 1. ❌ "SDK location not found"

**Error Message:**
```
SDK location not found. Define location with an ANDROID_SDK_ROOT environment variable
```

**Solution:**
```bash
# Create local.properties file
echo "sdk.dir=/path/to/android/sdk" > local.properties

# Or set environment variable
export ANDROID_HOME=/path/to/android/sdk
export ANDROID_SDK_ROOT=/path/to/android/sdk
```

**Find your SDK path:**
- Windows: `C:\Users\YourName\AppData\Local\Android\Sdk`
- Mac: `/Users/YourName/Library/Android/sdk`
- Linux: `/home/YourName/Android/Sdk`

---

### 2. ❌ "Unsupported Java version"

**Error Message:**
```
Unsupported Java. Your build is currently configured to use Java X
```

**Solution:**
```bash
# Check current Java version
java -version

# Install JDK 17
# Download from: https://adoptium.net/

# Set JAVA_HOME
export JAVA_HOME=/path/to/jdk-17
export PATH=$JAVA_HOME/bin:$PATH
```

---

### 3. ❌ "Dependency resolution failed"

**Error Message:**
```
Could not resolve all dependencies for configuration ':app:debugCompileClasspath'
```

**Solution:**
```bash
# Method 1: Clean and rebuild
./gradlew clean
./gradlew build --refresh-dependencies

# Method 2: Clear Gradle cache
rm -rf ~/.gradle/caches/
./gradlew clean build

# Method 3: Check internet connection
# Make sure you can access:
# - https://maven.google.com
# - https://repo1.maven.org
# - https://jitpack.io
```

---

### 4. ❌ "Task :app:compileDebugKotlin FAILED"

**Error Message:**
```
Compilation error. See log for more details
```

**Solution:**
```bash
# Get detailed error log
./gradlew assembleDebug --stacktrace --info

# Common fixes:
# 1. Update Kotlin version in build.gradle
# 2. Check for syntax errors in .kt files
# 3. Invalidate caches in Android Studio:
#    File → Invalidate Caches / Restart
```

---

### 5. ❌ "Gradle daemon disappeared unexpectedly"

**Error Message:**
```
Gradle build daemon disappeared unexpectedly
```

**Solution:**
```bash
# Increase Gradle memory
# Edit gradle.properties:
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=512m

# Kill existing Gradle daemons
./gradlew --stop

# Rebuild
./gradlew clean assembleDebug
```

---

### 6. ❌ GitHub Actions Build Failed

**Problem:** Build fails in GitHub Actions but works locally

**Solution:**

Check the Actions log for:

1. **Java version mismatch:**
```yaml
- name: Set up JDK 17
  uses: actions/setup-java@v4
  with:
    java-version: '17'  # ← Make sure this is 17
```

2. **Missing permissions:**
```yaml
- name: Grant execute permission for gradlew
  run: chmod +x gradlew  # ← Make sure this exists
```

3. **Dependency download issues:**
```yaml
- name: Cache Gradle packages
  uses: actions/cache@v3
  with:
    path: ~/.gradle/caches
    key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*') }}
```

---

### 7. ❌ "Cannot find symbol" errors

**Error Message:**
```
error: cannot find symbol class/method/variable
```

**Solution:**
```bash
# Clean and rebuild
./gradlew clean

# In Android Studio:
# Build → Clean Project
# Build → Rebuild Project

# Check imports in affected files
# Make sure all dependencies are in build.gradle
```

---

### 8. ❌ APK not generated

**Problem:** Build succeeds but no APK found

**Solution:**

APK should be at:
```
app/build/outputs/apk/debug/app-debug.apk
```

If not found:
```bash
# Check build output
./gradlew assembleDebug --info | grep "APK"

# Try release build
./gradlew assembleRelease

# Check for build errors
./gradlew assembleDebug --stacktrace
```

---

### 9. ❌ "Manifest merger failed"

**Error Message:**
```
Manifest merger failed with multiple errors
```

**Solution:**
```bash
# Get detailed error
./gradlew assembleDebug --stacktrace

# Common fixes:
# 1. Check for duplicate permissions in AndroidManifest.xml
# 2. Check for conflicting library manifests
# 3. Add tools:replace in manifest:
```

```xml
<application
    android:name=".App"
    tools:replace="android:label,android:theme"
    ...>
```

---

### 10. ❌ "Out of memory" error

**Error Message:**
```
java.lang.OutOfMemoryError: GC overhead limit exceeded
```

**Solution:**

Edit `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=512m
org.gradle.parallel=true
org.gradle.caching=true
```

---

## Quick Fixes Checklist

Before asking for help, try these:

- [ ] `./gradlew clean`
- [ ] `./gradlew --stop`
- [ ] Delete `.gradle` folder in project root
- [ ] Delete `build` folders in all modules
- [ ] Sync project with Gradle files
- [ ] Invalidate caches and restart Android Studio
- [ ] Check internet connection
- [ ] Verify JDK 17 is installed
- [ ] Check Android SDK is installed (API 34)
- [ ] Update Android Studio to latest version

---

## Still Having Issues?

1. **Check the full error log:**
```bash
./gradlew assembleDebug --stacktrace --info > build-log.txt
```

2. **Search for the error message:**
   - Google the exact error
   - Check Stack Overflow
   - Check GitHub Issues

3. **Create an issue:**
   - Include full error log
   - Include Gradle version (`./gradlew --version`)
   - Include Java version (`java -version`)
   - Include OS and version

---

## தமிழில் விளக்கம்

### பொதுவான பிரச்சனைகளும் தீர்வுகளும்:

1. **SDK காணவில்லை** → `local.properties` file create பண்ணுங்க
2. **Java version தவறு** → JDK 17 install பண்ணுங்க
3. **Build fail ஆகுது** → `./gradlew clean` பண்ணி மறுபடி try பண்ணுங்க
4. **Memory error** → `gradle.properties`-ல memory increase பண்ணுங்க
5. **APK காணவில்லை** → `app/build/outputs/apk/debug/` folder-ல பாருங்க

**எல்லாம் செய்தும் வேலை ஆகலனா:**
- Error message-ஐ முழுசா copy பண்ணி Google-ல search பண்ணுங்க
- GitHub-ல issue create பண்ணுங்க
- Full error log attach பண்ணுங்க

---

**Remember:** Most build errors are due to:
- Wrong Java version (need JDK 17)
- Missing Android SDK
- Gradle cache issues
- Internet connection problems

**Fix rate: 95% of issues solved by these steps!** ✅
