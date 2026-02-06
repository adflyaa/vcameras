# VCamera - Virtual Camera App 📱

[![Android CI Build](https://github.com/yourusername/vcameras/actions/workflows/android.yml/badge.svg)](https://github.com/yourusername/vcameras/actions/workflows/android.yml)

A powerful virtual camera application for Android with app virtualization support.

## 🚀 Quick Start

### GitHub Actions Build (Easiest!)

1. Fork this repository
2. Go to **Actions** tab
3. Click **"Android CI Build"**
4. Click **"Run workflow"** button
5. Wait 5-10 minutes
6. Download APK from **Artifacts**

### Local Build

```bash
git clone https://github.com/yourusername/vcameras.git
cd vcameras
chmod +x gradlew
./gradlew assembleDebug
```

APK location: `app/build/outputs/apk/debug/app-debug.apk`

## 📋 Requirements

- ✅ JDK 17
- ✅ Android SDK 34
- ✅ Gradle 8.1.1 (auto-downloaded)
- ✅ Min Android: 5.0 (API 21)

## 🛠️ Features

- 📷 Virtual Camera Management
- 📱 App Virtualization
- 🌍 Location Spoofing
- 🎨 Material Design UI
- 🌐 Multi-language Support

## 🔧 Troubleshooting

**Build Failed?**
```bash
./gradlew clean
./gradlew assembleDebug --stacktrace
```

**Gradle Issues?**
```bash
rm -rf ~/.gradle/caches/
./gradlew --refresh-dependencies
```

## 📦 Project Info

- **Version**: 1.0.0
- **Package**: virtual.camera.app
- **Min SDK**: 21 (Android 5.0)
- **Target SDK**: 34 (Android 14)

## 📄 License

Apache License 2.0

---

**100% Working Build ✓** | Made with ❤️ for Tamil developers
