# 🎥 VCamera - Virtual Camera App (Auto-Fix Enhanced Edition)

[![Build VCamera APK](https://github.com/YOUR_USERNAME/vcamera-FIXED-WORKING/actions/workflows/build-apk.yml/badge.svg)](https://github.com/YOUR_USERNAME/vcamera-FIXED-WORKING/actions/workflows/build-apk.yml)
[![Auto-Fix Build Configuration](https://github.com/YOUR_USERNAME/vcamera-FIXED-WORKING/actions/workflows/auto-fix.yml/badge.svg)](https://github.com/YOUR_USERNAME/vcamera-FIXED-WORKING/actions/workflows/auto-fix.yml)

**Virtual Camera Application** with **Automated Build Configuration Management**

---

## ✨ Features

### 🎥 App Features
- Virtual camera functionality
- Camera hook system
- Multi-app support
- Advanced camera controls
- Video manipulation capabilities

### 🤖 Build Automation Features
- ✅ **Auto-Fix Workflow** - Automatically maintains optimal build configuration
- ✅ **One-Click Build** - Build APK with a single button click
- ✅ **Continuous Integration** - Automatic builds on code push
- ✅ **Artifact Management** - Easy APK download from Actions
- ✅ **Build Monitoring** - Real-time build status tracking

---

## 🚀 Quick Start

### Method 1: GitHub Actions (Recommended! ⭐)

**No Android Studio or local setup required!**

#### Step 1: Build APK

1. Go to **Actions** tab
2. Select **"🚀 Build VCamera APK"**
3. Click **"Run workflow"** button
4. Choose build type: `debug` (recommended for testing)
5. Click green **"Run workflow"** button
6. Wait 3-4 minutes ⏱️

#### Step 2: Download APK

1. Workflow completes ✅
2. Scroll down to **Artifacts** section
3. Click **vcamera-debug-apk** to download
4. Extract ZIP file
5. Install `app-debug.apk` on your device

**Done! 🎉**

---

### Method 2: Local Build

If you want to build locally:

```bash
# Clone repository
git clone https://github.com/YOUR_USERNAME/vcamera-FIXED-WORKING.git
cd vcamera-FIXED-WORKING

# Build Debug APK
./gradlew assembleDebug

# Build Release APK
./gradlew assembleRelease

# APK location:
# app/build/outputs/apk/debug/app-debug.apk
```

**Requirements:**
- JDK 17
- Android SDK
- Gradle 8.8 (auto-downloaded)

---

## 🔧 Auto-Fix Workflow

### What is Auto-Fix?

The **Auto-Fix workflow** automatically maintains your build configuration at optimal settings:

- ✅ **Gradle 8.8** - Latest stable version
- ✅ **AGP 8.3.2** - Android Gradle Plugin
- ✅ **Jetifier Disabled** - Modern AndroidX setup
- ✅ **Optimized settings.gradle** - Best practices

### When Does Auto-Fix Run?

**Automatically:**
- When you push changes to `.gradle` files
- When `gradle.properties` is modified
- When Gradle wrapper files change

**Manually:**
- Actions → "🔧 Auto-Fix Build Configuration" → Run workflow

### How to Use Auto-Fix

1. Go to **Actions** tab
2. Select **"🔧 Auto-Fix Build Configuration"**
3. Click **"Run workflow"**
4. (Optional) Check "Force apply all fixes"
5. Click green **"Run workflow"**
6. Wait ~30 seconds
7. ✅ Configuration automatically optimized!

---

## 📊 Current Configuration

```yaml
Gradle:      8.8
AGP:         8.3.2
Kotlin:      1.9.22
Java:        17
Min SDK:     21
Target SDK:  34
Compile SDK: 34

Jetifier:    Disabled ✅
AndroidX:    Enabled ✅
```

---

## 🛠️ GitHub Workflows

This repository includes three automated workflows:

### 1. 🔧 Auto-Fix Build Configuration
- **File:** `.github/workflows/auto-fix.yml`
- **Purpose:** Maintains optimal build settings
- **Triggers:** Manual, Push to gradle files
- **Duration:** ~30 seconds

### 2. 🚀 Build VCamera APK  
- **File:** `.github/workflows/build-apk.yml`
- **Purpose:** Builds Android APK
- **Triggers:** Manual, Push, Pull Request
- **Duration:** ~3-4 minutes

### 3. 📱 Android CI (Original)
- **File:** `.github/workflows/android.yml`
- **Purpose:** Continuous Integration
- **Triggers:** Push, Pull Request

---

## 📂 Project Structure

```
vcamera-FIXED-WORKING/
├── .github/
│   └── workflows/
│       ├── auto-fix.yml       # Auto-fix workflow
│       ├── build-apk.yml      # Build workflow
│       └── android.yml        # CI workflow
├── app/                       # Main application module
│   ├── src/
│   └── build.gradle
├── opensdk/                   # OpenSDK module
│   ├── src/
│   └── build.gradle
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties
├── build.gradle               # Root build configuration
├── gradle.properties          # Global Gradle properties
├── settings.gradle            # Project structure
└── README.md                  # This file
```

---

## 💡 Usage Tips

### Building Different Variants

**Debug Build (Recommended for Testing):**
```bash
./gradlew assembleDebug
# or via GitHub Actions: Choose "debug"
```

**Release Build (For Distribution):**
```bash
./gradlew assembleRelease
# or via GitHub Actions: Choose "release"
# Note: May require signing configuration
```

**Both:**
```bash
./gradlew assembleDebug assembleRelease
# or via GitHub Actions: Choose "both"
```

### Viewing Build Logs

**In GitHub Actions:**
1. Go to Actions tab
2. Click on the workflow run
3. Click on "Build Android APK" job
4. Expand each step to see detailed logs

**Local Build:**
```bash
./gradlew assembleDebug --stacktrace --info
```

### Common Issues & Solutions

**Issue: Build fails with Gradle error**
```bash
# Solution 1: Run Auto-Fix
Actions → Auto-Fix → Run workflow

# Solution 2: Clean build
./gradlew clean build
```

**Issue: APK not found**
```bash
# Check build outputs
find app/build/outputs/apk -name "*.apk"
```

**Issue: Gradle daemon issues**
```bash
./gradlew --stop
./gradlew assembleDebug
```

---

## 🔐 Signing Configuration (Optional)

For release builds, add signing configuration to `app/build.gradle`:

```gradle
android {
    signingConfigs {
        release {
            storeFile file("path/to/keystore.jks")
            storePassword "your_store_password"
            keyAlias "your_key_alias"
            keyPassword "your_key_password"
        }
    }
    
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            proguardFiles getDefaultProguardFile(
                'proguard-android-optimize.txt'
            ), 'proguard-rules.pro'
        }
    }
}
```

**Security Note:** Never commit keystore files or passwords to Git!

---

## 📖 Additional Documentation

- **QUICK_START.md** - Quick setup guide
- **BUILD_INSTRUCTIONS.md** - Detailed build instructions
- **TROUBLESHOOTING.md** - Common issues and fixes
- **TAMIL_COMPLETE_GUIDE.md** - Complete guide in Tamil
- **FIX_README_TAMIL.md** - Auto-fix guide in Tamil

---

## 🤝 Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test with both workflows
5. Submit a pull request

---

## 📱 Installation

### On Physical Device

1. Download APK from Artifacts
2. Enable **Unknown Sources** in settings
3. Install APK
4. Grant required permissions

### On Emulator

```bash
# Start emulator
emulator -avd Pixel_5_API_34

# Install APK
adb install app-debug.apk

# Launch app
adb shell monkey -p virtual.camera.app 1
```

---

## ⚙️ Build Configuration

### Gradle Properties

Current settings in `gradle.properties`:

```properties
org.gradle.jvmargs=-Xmx2048m
org.gradle.parallel=true
org.gradle.caching=true
android.useAndroidX=true
android.enableJetifier=false
```

### Module Configuration

**App Module:**
- Namespace: `virtual.camera.app`
- Min SDK: 21 (Android 5.0)
- Target SDK: 34 (Android 14)
- ABIs: armeabi-v7a, arm64-v8a

**OpenSDK Module:**
- Android library module
- Shared utilities and APIs

---

## 📊 Build Status

| Workflow | Status | Purpose |
|----------|--------|---------|
| Auto-Fix | ![Auto-Fix](https://img.shields.io/badge/status-active-success) | Config maintenance |
| Build APK | ![Build](https://img.shields.io/badge/status-active-success) | APK generation |
| Android CI | ![CI](https://img.shields.io/badge/status-active-success) | Continuous integration |

---

## 🎯 Roadmap

- [x] Auto-fix workflow
- [x] One-click build
- [x] Artifact management
- [ ] Automated testing
- [ ] Release automation
- [ ] Code signing workflow
- [ ] Multi-flavor builds

---

## 📄 License

This project is licensed under the terms specified in the LICENSE file.

---

## 🙏 Acknowledgments

- Android Open Source Project
- Gradle Build Tool
- GitHub Actions
- Contributors and testers

---

## 📞 Support

For issues or questions:

1. Check **TROUBLESHOOTING.md**
2. Review GitHub Actions logs
3. Run Auto-Fix workflow
4. Create an issue on GitHub

---

## 🌟 Star This Repo!

If you find this project useful, please star it! ⭐

---

**Made with ❤️ by the VCamera Team**

*Last Updated: February 2026*
