# VCamera Build Instructions / வில்டு செய்முறைகள்

## 🎯 100% Working Build Guide

### English Instructions

#### Prerequisites
1. Install JDK 17
   - Download from: https://adoptium.net/
   - Verify: `java -version`

2. Install Android Studio
   - Download from: https://developer.android.com/studio
   - Install Android SDK 34 via SDK Manager

#### Build Methods

**Method 1: GitHub Actions (Recommended for beginners)**

1. Fork this repository on GitHub
2. Go to your forked repo
3. Click on "Actions" tab
4. Click "Android CI Build" workflow
5. Click "Run workflow" button
6. Wait for build to complete (5-10 minutes)
7. Click on the completed workflow
8. Download APK from "Artifacts" section

**Method 2: Command Line**

```bash
# Clone repository
git clone https://github.com/yourusername/vcameras.git
cd vcameras

# Give execute permission
chmod +x gradlew

# Clean project
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# OR build release APK
./gradlew assembleRelease
```

APK Location:
- Debug: `app/build/outputs/apk/debug/app-debug.apk`
- Release: `app/build/outputs/apk/release/app-release.apk`

**Method 3: Android Studio**

1. Open Android Studio
2. File → Open → Select vcameras folder
3. Wait for Gradle sync to complete
4. Build → Build Bundle(s) / APK(s) → Build APK(s)
5. Click "locate" when build completes

---

### தமிழ் வழிகாட்டுதல்கள்

#### முன்நிபந்தனைகள்
1. JDK 17 install பண்ணுங்க
   - இங்கேர்ந்து download: https://adoptium.net/
   - Check பண்ணுங்க: `java -version`

2. Android Studio install பண்ணுங்க
   - Download: https://developer.android.com/studio
   - SDK Manager-ல Android SDK 34 install பண்ணுங்க

#### Build செய்யும் முறைகள்

**முறை 1: GitHub Actions (ஆரம்பநிலைக்கு சிறந்தது)**

1. இந்த repository-ஐ உங்க GitHub-ல fork பண்ணுங்க
2. உங்க fork பண்ண repo-க்கு போங்க
3. "Actions" tab-ல click பண்ணுங்க
4. "Android CI Build" workflow-ல click பண்ணுங்க
5. "Run workflow" button-ல click பண்ணுங்க
6. Build முடியும் வரை காத்திருங்க (5-10 நிமிடங்கள்)
7. முடிஞ்ச workflow-ல click பண்ணுங்க
8. "Artifacts" section-லேர்ந்து APK download பண்ணுங்க

**முறை 2: Command Line**

```bash
# Repository clone பண்ணுங்க
git clone https://github.com/yourusername/vcameras.git
cd vcameras

# Execute permission கொடுங்க
chmod +x gradlew

# Project clean பண்ணுங்க
./gradlew clean

# Debug APK build பண்ணுங்க
./gradlew assembleDebug

# அல்லது release APK build பண்ணுங்க
./gradlew assembleRelease
```

APK இருக்கும் இடம்:
- Debug: `app/build/outputs/apk/debug/app-debug.apk`
- Release: `app/build/outputs/apk/release/app-release.apk`

**முறை 3: Android Studio**

1. Android Studio open பண்ணுங்க
2. File → Open → vcameras folder select பண்ணுங்க
3. Gradle sync முடியும் வரை wait பண்ணுங்க
4. Build → Build Bundle(s) / APK(s) → Build APK(s) click பண்ணுங்க
5. Build முடிஞ்சதும் "locate" click பண்ணி APK-ஐ எடுத்துக்குங்க

---

## 🐛 Common Issues / சாதாரண பிரச்சனைகள்

### Problem: Build Failed
```bash
./gradlew clean
./gradlew assembleDebug --stacktrace
```

### Problem: Gradle Sync Failed
```bash
rm -rf ~/.gradle/caches/
./gradlew --refresh-dependencies
```

### Problem: SDK not found
**Solution:**
1. Open Android Studio
2. Tools → SDK Manager
3. Install Android SDK 34
4. Set ANDROID_HOME environment variable

### Problem: Java version mismatch
**Solution:**
```bash
# Check Java version
java -version

# Should show: openjdk version "17.x.x"
# If not, install JDK 17 from https://adoptium.net/
```

---

## ✅ Verification / சரிபார்ப்பு

After successful build, you should see:
```
BUILD SUCCESSFUL in Xs
```

And APK file at:
```
app/build/outputs/apk/debug/app-debug.apk
```

---

## 🎉 Success!

If you see the APK file, congratulations! 🎊

Install it on your Android device:
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## 📞 Need Help? / உதவி வேணுமா?

- Open an issue on GitHub
- Check existing issues for solutions
- Read the error messages carefully

**Made with ❤️ for developers**
