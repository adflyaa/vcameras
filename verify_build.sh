#!/bin/bash

# VCamera Build Verification Script
# Checks if all build fixes are properly applied

echo "=========================================="
echo "  VCamera Build Configuration Checker"
echo "=========================================="
echo ""

ERRORS=0
WARNINGS=0

# Color codes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check function
check_file() {
    if [ -f "$1" ]; then
        echo -e "${GREEN}✅ Found:${NC} $1"
        return 0
    else
        echo -e "${RED}❌ Missing:${NC} $1"
        ERRORS=$((ERRORS + 1))
        return 1
    fi
}

check_directory() {
    if [ -d "$1" ]; then
        echo -e "${GREEN}✅ Found:${NC} $1/"
        return 0
    else
        echo -e "${YELLOW}⚠️  Missing:${NC} $1/ (will be created during build)"
        WARNINGS=$((WARNINGS + 1))
        return 1
    fi
}

check_content() {
    if grep -q "$2" "$1" 2>/dev/null; then
        echo -e "${GREEN}✅ Verified:${NC} $3"
        return 0
    else
        echo -e "${RED}❌ Not found:${NC} $3 in $1"
        ERRORS=$((ERRORS + 1))
        return 1
    fi
}

echo "1️⃣  Checking Essential Files..."
echo "-----------------------------------"
check_file "build.gradle"
check_file "settings.gradle"
check_file "gradle.properties"
check_file "gradle/wrapper/gradle-wrapper.properties"
check_file "app/build.gradle"
check_file "opensdk/build.gradle"
check_file ".github/workflows/android-build.yml"
echo ""

echo "2️⃣  Checking Build Configuration..."
echo "-----------------------------------"
check_content "build.gradle" "1.9.22" "Kotlin version 1.9.22"
check_content "build.gradle" "8.2.2" "AGP version 8.2.2"
check_content "gradle/wrapper/gradle-wrapper.properties" "gradle-8.2" "Gradle 8.2"
check_content "gradle.properties" "Xmx3072m" "Memory allocation (3GB)"
check_content "gradle.properties" "org.gradle.parallel=true" "Parallel builds enabled"
echo ""

echo "3️⃣  Checking Module Configuration..."
echo "-----------------------------------"
check_content "app/build.gradle" "namespace 'virtual.camera.app'" "App namespace defined"
check_content "opensdk/build.gradle" "namespace 'com.hack.opensdk'" "OpenSDK namespace defined"
check_content "settings.gradle" "PREFER_SETTINGS" "Repository mode set correctly"
echo ""

echo "4️⃣  Checking Directory Structure..."
echo "-----------------------------------"
check_directory "app/src/main"
check_directory "opensdk/src/main"
check_directory "opensdk/libs"
check_directory ".github/workflows"
echo ""

echo "5️⃣  Checking Gradle Wrapper..."
echo "-----------------------------------"
if [ -x "gradlew" ]; then
    echo -e "${GREEN}✅ Executable:${NC} gradlew"
else
    echo -e "${RED}❌ Not executable:${NC} gradlew (run: chmod +x gradlew)"
    ERRORS=$((ERRORS + 1))
fi
echo ""

echo "6️⃣  Checking AndroidManifest Files..."
echo "-----------------------------------"
check_file "app/src/main/AndroidManifest.xml"
check_file "opensdk/src/main/AndroidManifest.xml"
echo ""

echo "7️⃣  Checking GitHub Actions Configuration..."
echo "-----------------------------------"
check_content ".github/workflows/android-build.yml" "java-version: '17'" "JDK 17 configured"
check_content ".github/workflows/android-build.yml" "mkdir -p opensdk/libs" "Libs directory creation step"
check_content ".github/workflows/android-build.yml" "assembleDebug" "Debug build step"
check_content ".github/workflows/android-build.yml" "assembleRelease" "Release build step"
echo ""

echo "=========================================="
echo "  Verification Summary"
echo "=========================================="

if [ $ERRORS -eq 0 ]; then
    echo -e "${GREEN}✅ ALL CHECKS PASSED!${NC}"
    echo ""
    echo -e "${GREEN}Your project is ready to build!${NC}"
    echo ""
    echo "Next steps:"
    echo "  1. Run: ./gradlew clean"
    echo "  2. Run: ./gradlew assembleDebug"
    echo "  3. Or push to GitHub for automatic build"
    exit 0
else
    echo -e "${RED}❌ FOUND $ERRORS ERROR(S)${NC}"
    if [ $WARNINGS -gt 0 ]; then
        echo -e "${YELLOW}⚠️  FOUND $WARNINGS WARNING(S)${NC}"
    fi
    echo ""
    echo "Please fix the errors above before building."
    exit 1
fi
