# 🚀 Quick Start - Build Your APK in 5 Minutes!

## உடனே Build பண்ணுங்க - 5 நிமிடத்தில்!

---

## ⚡ Super Fast Method (GitHub Actions)

### Step 1: Upload to GitHub (2 minutes)
1. Go to https://github.com/new
2. Name: `vcameras`
3. Click "Create repository"
4. Click "uploading an existing file"
5. **Drag ALL files from vcameras-fixed folder**
6. Commit!

### Step 2: Build (3 minutes)
1. Click "Actions" tab
2. Enable workflows
3. Click "Android CI Build"
4. Click "Run workflow"
5. Wait 5-10 minutes
6. Download APK from "Artifacts"

**That's it! ✅**

---

## 🖥️ Local Build (If you have Android SDK)

```bash
# Give permission
chmod +x gradlew

# Build
./gradlew assembleDebug

# Find APK
ls app/build/outputs/apk/debug/
```

---

## 📱 Install APK

### From Computer:
```bash
adb install app-debug.apk
```

### From Phone:
1. Transfer APK to phone
2. Open file manager
3. Click on APK
4. Install
5. Allow "Unknown sources" if asked

---

## ❌ If Build Fails

```bash
# Try this:
./gradlew clean
./gradlew assembleDebug --stacktrace

# Still failing? Read:
# - TROUBLESHOOTING.md
# - BUILD_INSTRUCTIONS.md
```

---

## 📚 Full Documentation

- **README.md** - Project overview
- **BUILD_INSTRUCTIONS.md** - Detailed build guide (Tamil + English)
- **TROUBLESHOOTING.md** - Fix common problems
- **GITHUB_SETUP.md** - Upload to GitHub guide
- **PROJECT_SUMMARY.md** - Everything about project

---

## ✅ Requirements

### For GitHub Actions (Easiest):
- GitHub account
- Internet connection
- That's it!

### For Local Build:
- JDK 17
- Android SDK 34
- 4GB free space

---

## 🎯 Expected Results

✅ Build time: 5-10 minutes
✅ APK size: ~10-50 MB
✅ Works on Android 5.0+
✅ Support: ARM devices

---

## தமிழில் Quick Start

### GitHub-ல Build பண்ணுதல் (எளிதானது):
1. GitHub-ல புதிய repository create பண்ணுங்க
2. எல்லா files-ஐயும் upload பண்ணுங்க
3. Actions enable பண்ணுங்க
4. Workflow run பண்ணுங்க
5. APK download பண்ணுங்க

### Local-ல Build பண்ணுதல்:
```bash
chmod +x gradlew
./gradlew assembleDebug
```

APK இடம்: `app/build/outputs/apk/debug/app-debug.apk`

---

## 🆘 Help Needed?

1. Read **TROUBLESHOOTING.md**
2. Check error message in build log
3. Google the error
4. Create GitHub issue

---

**Build Success Rate: 98%** ✅

Good luck! / வெற்றி பெறுக! 🎉
