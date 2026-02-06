#!/bin/bash

# VCamera - Automated Build Script
# This script automates the complete build process

echo "╔════════════════════════════════════════════════╗"
echo "║   VCamera - Automated Build Script            ║"
echo "║   Building Complete Working APK                ║"
echo "╚════════════════════════════════════════════════╝"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored messages
print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_info() {
    echo -e "${YELLOW}ℹ $1${NC}"
}

# Check if running in project directory
if [ ! -f "settings.gradle" ]; then
    print_error "Error: Not in project root directory!"
    echo "Please run this script from VCamera-main folder"
    exit 1
fi

print_info "Step 1: Checking prerequisites..."

# Check for gradlew
if [ ! -f "./gradlew" ]; then
    print_error "gradlew not found!"
    exit 1
fi
print_success "Gradle wrapper found"

# Make gradlew executable
chmod +x ./gradlew
print_success "Gradle wrapper made executable"

print_info "Step 2: Cleaning previous builds..."
./gradlew clean
if [ $? -eq 0 ]; then
    print_success "Clean successful"
else
    print_error "Clean failed"
    exit 1
fi

print_info "Step 3: Building Debug APK..."
./gradlew assembleDebug
if [ $? -eq 0 ]; then
    print_success "Build successful!"
else
    print_error "Build failed"
    print_info "Check build errors above"
    exit 1
fi

# Find the APK
APK_PATH="app/build/outputs/apk/debug/app-debug.apk"

if [ -f "$APK_PATH" ]; then
    print_success "APK created: $APK_PATH"
    
    # Get APK size
    APK_SIZE=$(du -h "$APK_PATH" | cut -f1)
    print_info "APK Size: $APK_SIZE"
    
    echo ""
    echo "╔════════════════════════════════════════════════╗"
    echo "║          BUILD SUCCESSFUL! 🎉                  ║"
    echo "╚════════════════════════════════════════════════╝"
    echo ""
    echo "APK Location: $APK_PATH"
    echo "APK Size: $APK_SIZE"
    echo ""
    echo "Next steps:"
    echo "1. Install APK: adb install $APK_PATH"
    echo "2. Or copy to device and install manually"
    echo ""
    
    # Offer to install if device connected
    if adb devices | grep -q "device$"; then
        echo "📱 Android device detected!"
        read -p "Install APK now? (y/n): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            print_info "Installing APK..."
            adb install -r "$APK_PATH"
            if [ $? -eq 0 ]; then
                print_success "Installation successful!"
                echo "You can now launch VCamera on your device"
            else
                print_error "Installation failed"
                echo "Try installing manually"
            fi
        fi
    fi
    
else
    print_error "APK not found at expected location"
    print_info "Check build output for errors"
    exit 1
fi

echo ""
print_info "Build complete! Check README_COMPLETE.md for usage instructions"
