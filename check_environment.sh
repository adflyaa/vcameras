#!/bin/bash

# VCamera Build Verification Script
# இந்த script உங்கள் build environment சரியாக உள்ளதா என்பதை சரிபார்க்கும்

echo "=================================="
echo "VCamera Build Environment Check"
echo "VCamera Build சூழல் சரிபார்ப்பு"
echo "=================================="
echo ""

# Check Java version
echo "🔍 Checking Java/JDK version..."
echo "🔍 Java/JDK version சரிபார்க்கிறது..."
if command -v java &> /dev/null; then
    java_version=$(java -version 2>&1 | head -n 1)
    echo "✅ Java found: $java_version"
    
    # Check if it's Java 17
    if echo "$java_version" | grep -q "17"; then
        echo "✅ Java 17 detected - Perfect!"
        echo "✅ Java 17 கண்டறியப்பட்டது - சரியானது!"
    else
        echo "⚠️  Warning: Java 17 recommended"
        echo "⚠️  எச்சரிக்கை: Java 17 பரிந்துரைக்கப்படுகிறது"
    fi
else
    echo "❌ Java not found! Please install JDK 17"
    echo "❌ Java காணப்படவில்லை! JDK 17 நிறுவுங்கள்"
    echo "   Download from: https://adoptium.net/"
    exit 1
fi

echo ""

# Check Android SDK
echo "🔍 Checking Android SDK..."
echo "🔍 Android SDK சரிபார்க்கிறது..."
if [ -n "$ANDROID_HOME" ] || [ -n "$ANDROID_SDK_ROOT" ]; then
    echo "✅ Android SDK found"
    echo "✅ Android SDK கண்டறியப்பட்டது"
else
    echo "⚠️  Android SDK not set (needed only for local builds)"
    echo "⚠️  Android SDK அமைக்கப்படவில்லை (local builds-க்கு மட்டுமே தேவை)"
fi

echo ""

# Check Gradle wrapper
echo "🔍 Checking Gradle wrapper..."
echo "🔍 Gradle wrapper சரிபார்க்கிறது..."
if [ -f "./gradlew" ]; then
    echo "✅ Gradle wrapper found"
    echo "✅ Gradle wrapper கண்டறியப்பட்டது"
    
    # Check if executable
    if [ -x "./gradlew" ]; then
        echo "✅ Gradle wrapper is executable"
        echo "✅ Gradle wrapper இயக்கக்கூடியது"
    else
        echo "⚠️  Making gradlew executable..."
        echo "⚠️  gradlew-ஐ இயக்கக்கூடியதாக்குகிறது..."
        chmod +x ./gradlew
        echo "✅ Fixed!"
        echo "✅ சரி செய்யப்பட்டது!"
    fi
else
    echo "❌ Gradle wrapper not found!"
    echo "❌ Gradle wrapper காணப்படவில்லை!"
    exit 1
fi

echo ""

# Check project structure
echo "🔍 Checking project structure..."
echo "🔍 திட்ட அமைப்பு சரிபார்க்கிறது..."

required_files=(
    "build.gradle"
    "settings.gradle"
    "app/build.gradle"
    "app/src/main/AndroidManifest.xml"
)

all_good=true
for file in "${required_files[@]}"; do
    if [ -f "$file" ]; then
        echo "✅ Found: $file"
    else
        echo "❌ Missing: $file"
        all_good=false
    fi
done

echo ""

if [ "$all_good" = true ]; then
    echo "=================================="
    echo "✅ All checks passed!"
    echo "✅ எல்லா சரிபார்ப்புகளும் வெற்றி!"
    echo "=================================="
    echo ""
    echo "You can now build the app:"
    echo "இப்போது app-ஐ build செய்யலாம்:"
    echo ""
    echo "  ./gradlew assembleDebug"
    echo ""
    echo "Or use Android Studio to build"
    echo "அல்லது Android Studio பயன்படுத்தி build செய்யவும்"
    echo ""
    exit 0
else
    echo "=================================="
    echo "❌ Some checks failed!"
    echo "❌ சில சரிபார்ப்புகள் தோல்வி!"
    echo "=================================="
    echo ""
    echo "Please check the errors above"
    echo "மேலே உள்ள பிழைகளை சரிபார்க்கவும்"
    echo ""
    exit 1
fi
