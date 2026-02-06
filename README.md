# VCamera - Virtual Camera App 📱

[English](#english) | [தமிழ்](#tamil)

---

<a name="english"></a>
## 🌟 English

### Overview
VCamera is an advanced Android application that enables virtual camera functionality with support for customized camera feeds, location spoofing, and app virtualization.

### ✨ Features
- 📸 Virtual camera implementation
- 🌍 Location customization
- 📦 App virtualization support
- 🔧 Xposed module integration
- 🎯 Camera feed manipulation

### 📋 Requirements
- **Android Studio**: Arctic Fox (2020.3.1) or newer
- **JDK**: 17 (Temurin/OpenJDK recommended)
- **Android SDK**: API Level 33
- **Gradle**: 8.1.1 (included in wrapper)
- **Minimum Android**: API 21 (Android 5.0)
- **Target Android**: API 33 (Android 13)

### 🚀 Quick Start

#### Option 1: Build on Your Computer

1. **Clone the repository:**
```bash
git clone https://github.com/YOUR_USERNAME/vcameras-fixed.git
cd vcameras-fixed
```

2. **Open in Android Studio:**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned folder
   - Wait for Gradle sync to complete

3. **Build the app:**
   - Click "Build" → "Build Bundle(s) / APK(s)" → "Build APK(s)"
   - Or use command line:
```bash
./gradlew assembleDebug
```

4. **Find your APK:**
   - Location: `app/build/outputs/apk/debug/app-debug.apk`
   - Install on your Android device

#### Option 2: Build with GitHub Actions (Recommended for Beginners)

1. **Fork this repository:**
   - Click the "Fork" button at the top right of this page
   - This creates your own copy

2. **Enable GitHub Actions:**
   - Go to your forked repository
   - Click "Actions" tab
   - Click "I understand my workflows, go ahead and enable them"

3. **Trigger a build:**
   - Go to "Actions" tab
   - Select "Android CI" workflow
   - Click "Run workflow" → "Run workflow"
   - Wait for the build to complete (5-10 minutes)

4. **Download your APK:**
   - Once build is complete (green checkmark ✓)
   - Click on the workflow run
   - Scroll down to "Artifacts"
   - Download "vcamera-debug"
   - Extract the ZIP file
   - Install the APK on your phone

### 📦 Project Structure
```
vcameras-fixed/
├── app/                    # Main application module
│   ├── src/
│   │   └── main/
│   │       ├── java/       # Java/Kotlin source code
│   │       ├── res/        # Resources (layouts, strings, etc.)
│   │       └── AndroidManifest.xml
│   └── build.gradle
├── opensdk/                # SDK module
├── .github/
│   └── workflows/
│       └── android.yml     # CI/CD configuration
├── build.gradle            # Root build configuration
├── settings.gradle         # Project settings
└── gradlew                 # Gradle wrapper script
```

### 🔧 Troubleshooting

#### Build Failed?
1. Make sure you have JDK 17 installed
2. Verify Android SDK is properly installed
3. Check internet connection (Gradle needs to download dependencies)
4. Try: `./gradlew clean` then rebuild

#### Gradle Sync Failed?
1. File → Invalidate Caches → Invalidate and Restart
2. Delete `.gradle` folder in project root
3. Sync again

#### APK Not Installing?
1. Enable "Unknown Sources" in Android settings
2. Make sure you have enough storage space
3. Try uninstalling previous version first

### 📄 License
This project is licensed under the GNU General Public License v3.0 - see [LICENSE](LICENSE) for details.

### 🤝 Contributing
Contributions are welcome! Please feel free to submit a Pull Request.

### ⚠️ Disclaimer
This app is for educational and testing purposes only. Use responsibly and in accordance with applicable laws.

---

<a name="tamil"></a>
## 🌟 தமிழ்

### கண்ணோட்டம்
VCamera என்பது மெய்நிகர் கேமரா செயல்பாட்டை செயல்படுத்தும் ஒரு மேம்பட்ட ஆண்ட்ராய்டு பயன்பாடு ஆகும்.

### ✨ அம்சங்கள்
- 📸 மெய்நிகர் கேமரா செயல்படுத்தல்
- 🌍 இடத்தை தனிப்பயனாக்குதல்
- 📦 பயன்பாட்டு மெய்நிகர்மயமாக்கல் ஆதரவு
- 🔧 Xposed தொகுதி ஒருங்கிணைப்பு
- 🎯 கேமரா ஊட்டத்தை கையாளுதல்

### 📋 தேவைகள்
- **Android Studio**: Arctic Fox (2020.3.1) அல்லது புதியது
- **JDK**: 17 (Temurin/OpenJDK பரிந்துரைக்கப்படுகிறது)
- **Android SDK**: API Level 33
- **Gradle**: 8.1.1 (wrapper-இல் சேர்க்கப்பட்டுள்ளது)
- **குறைந்தபட்ச Android**: API 21 (Android 5.0)
- **இலக்கு Android**: API 33 (Android 13)

### 🚀 விரைவு தொடக்கம்

#### வழி 1: உங்கள் கணினியில் Build செய்யவும்

1. **Repository-ஐ Clone செய்யவும்:**
```bash
git clone https://github.com/YOUR_USERNAME/vcameras-fixed.git
cd vcameras-fixed
```

2. **Android Studio-வில் திறக்கவும்:**
   - Android Studio-ஐ தொடங்கவும்
   - "Open an Existing Project" தேர்ந்தெடுக்கவும்
   - Clone செய்த folder-க்கு செல்லவும்
   - Gradle sync முடியும் வரை காத்திருக்கவும்

3. **App-ஐ Build செய்யவும்:**
   - "Build" → "Build Bundle(s) / APK(s)" → "Build APK(s)" என்பதை கிளிக் செய்யவும்
   - அல்லது command line பயன்படுத்தவும்:
```bash
./gradlew assembleDebug
```

4. **உங்கள் APK-ஐ கண்டுபிடிக்கவும்:**
   - இடம்: `app/build/outputs/apk/debug/app-debug.apk`
   - உங்கள் Android சாதனத்தில் நிறுவவும்

#### வழி 2: GitHub Actions மூலம் Build செய்யவும் (ஆரம்பநிலைக்கு பரிந்துரைக்கப்படுகிறது)

1. **இந்த repository-ஐ Fork செய்யவும்:**
   - இந்த பக்கத்தின் மேல் வலது பக்கத்தில் உள்ள "Fork" பொத்தானை கிளிக் செய்யவும்
   - இது உங்களுக்கு சொந்த copy உருவாக்கும்

2. **GitHub Actions-ஐ இயக்கவும்:**
   - உங்கள் forked repository-க்கு செல்லவும்
   - "Actions" tab-ஐ கிளிக் செய்யவும்
   - "I understand my workflows, go ahead and enable them" என்பதை கிளிக் செய்யவும்

3. **Build-ஐ trigger செய்யவும்:**
   - "Actions" tab-க்கு செல்லவும்
   - "Android CI" workflow-ஐ தேர்ந்தெடுக்கவும்
   - "Run workflow" → "Run workflow" என்பதை கிளிக் செய்யவும்
   - Build முடியும் வரை காத்திருக்கவும் (5-10 நிமிடங்கள்)

4. **உங்கள் APK-ஐ பதிவிறக்கம் செய்யவும்:**
   - Build முடிந்ததும் (பச்சை tick mark ✓)
   - Workflow run-ஐ கிளிக் செய்யவும்
   - கீழே "Artifacts" வரை scroll செய்யவும்
   - "vcamera-debug" பதிவிறக்கம் செய்யவும்
   - ZIP file-ஐ extract செய்யவும்
   - உங்கள் phone-இல் APK-ஐ நிறுவவும்

### 🔧 சிக்கல்களை தீர்ப்பது

#### Build தோல்வியடைந்ததா?
1. JDK 17 நிறுவப்பட்டுள்ளதா என்பதை உறுதிப்படுத்தவும்
2. Android SDK சரியாக நிறுவப்பட்டுள்ளதா என்பதை சரிபார்க்கவும்
3. இணைய இணைப்பை சரிபார்க்கவும் (Gradle dependencies பதிவிறக்க வேண்டும்)
4. முயற்சி செய்யவும்: `./gradlew clean` பின்பு rebuild செய்யவும்

#### Gradle Sync தோல்வியடைந்ததா?
1. File → Invalidate Caches → Invalidate and Restart
2. Project root-இல் உள்ள `.gradle` folder-ஐ நீக்கவும்
3. மீண்டும் Sync செய்யவும்

#### APK நிறுவ முடியவில்லையா?
1. Android settings-இல் "Unknown Sources" இயக்கவும்
2. போதுமான storage இடம் உள்ளதா என்பதை உறுதிப்படுத்தவும்
3. முந்தைய பதிப்பை முதலில் uninstall செய்து முயற்சிக்கவும்

### 📞 Support & Help / ஆதரவு & உதவி

**சிக்கல் இருந்தால்?**
- GitHub Issues-இல் post செய்யவும்
- Build logs-ஐ இணைக்கவும்
- என்ன செய்தீர்கள் என்பதை விவரிக்கவும்

### 🎯 முக்கிய குறிப்புகள்

1. **முதல் முறை build செய்யும்போது:**
   - 10-15 நிமிடங்கள் ஆகலாம்
   - Gradle dependencies பதிவிறக்க நேரம் ஆகும்
   - பொறுமையாக காத்திருக்கவும்!

2. **GitHub Actions பயன்படுத்தினால்:**
   - முற்றிலும் free!
   - JDK/Android Studio நிறுவ தேவையில்லை
   - எந்த சாதனத்திலும் செய்யலாம் (phone உட்பட!)

3. **APK கிடைத்த பிறகு:**
   - Settings → Security → Unknown Sources இயக்கவும்
   - APK file-ஐ tap செய்து install செய்யவும்
   - Permissions கேட்டால் allow செய்யவும்

### 📄 உரிமம்
இந்த திட்டம் GNU General Public License v3.0 கீழ் உரிமம் பெற்றுள்ளது.

### ⚠️ மறுப்பு
இந்த app கல்வி மற்றும் சோதனை நோக்கங்களுக்காக மட்டுமே. பொறுப்புடன் பயன்படுத்தவும்.

---

## 🌟 மேலும் உதவி தேவையா? / Need More Help?

**FAQ / அடிக்கடி கேட்கப்படும் கேள்விகள்:**

1. **Q: JDK 17 எங்கிருந்து பதிவிறக்கம் செய்வது?**
   - A: https://adoptium.net/ (Temurin JDK 17)

2. **Q: Android Studio எங்கிருந்து பதிவிறக்கம் செய்வது?**
   - A: https://developer.android.com/studio

3. **Q: GitHub Actions இலவசமா?**
   - A: ஆம்! Public repositories-க்கு முற்றிலும் இலவசம்

4. **Q: Build நேரம் எவ்வளவு?**
   - A: 5-15 நிமிடங்கள் (முதல் முறை அதிக நேரம் ஆகும்)

---

**Made with ❤️ for the Android community**

**Happy Building! / மகிழ்ச்சியான Building! 🎉**
