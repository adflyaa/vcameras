# GitHub Repository Setup Guide 🚀

## Step-by-Step GitHub வைத்தல் வழிகாட்டி

### English Instructions

#### Step 1: Create GitHub Account
1. Go to https://github.com
2. Click "Sign up"
3. Create your account

#### Step 2: Create New Repository
1. Click the "+" icon (top right)
2. Select "New repository"
3. Repository name: `vcameras` (or any name you like)
4. Description: "Virtual Camera App for Android"
5. Choose "Public" or "Private"
6. ✅ **DO NOT** check "Add a README file"
7. Click "Create repository"

#### Step 3: Upload Your Code

**Method A: Using Git Command Line**

```bash
# Navigate to your project folder
cd /path/to/vcameras-fixed

# Initialize git
git init

# Add all files
git add .

# Commit
git commit -m "Initial commit - VCamera App"

# Add remote (replace YOUR_USERNAME and REPO_NAME)
git remote add origin https://github.com/YOUR_USERNAME/vcameras.git

# Push to GitHub
git branch -M main
git push -u origin main
```

**Method B: Using GitHub Desktop**

1. Download GitHub Desktop from https://desktop.github.com
2. Install and sign in
3. File → Add Local Repository
4. Choose your vcameras-fixed folder
5. Click "Publish repository"

**Method C: Drag and Drop (Easiest!)**

1. Go to your repository on GitHub
2. Click "uploading an existing file"
3. Drag and drop ALL files from vcameras-fixed folder
4. Add commit message: "Initial commit"
5. Click "Commit changes"

#### Step 4: Enable GitHub Actions

1. Go to your repository
2. Click "Actions" tab
3. Click "I understand my workflows, go ahead and enable them"
4. You should see "Android CI Build" workflow

#### Step 5: Trigger Build

1. In Actions tab, click "Android CI Build"
2. Click "Run workflow" dropdown
3. Click green "Run workflow" button
4. Wait 5-10 minutes
5. Click on the completed workflow run
6. Scroll down to "Artifacts"
7. Download "vcamera-debug" or "vcamera-release"

---

### தமிழ் வழிகாட்டுதல்

#### படி 1: GitHub Account உருவாக்குதல்
1. https://github.com க்கு போங்க
2. "Sign up" click பண்ணுங்க
3. உங்க account create பண்ணுங்க

#### படி 2: புதிய Repository உருவாக்குதல்
1. மேலே வலது பக்கம் "+" icon click பண்ணுங்க
2. "New repository" select பண்ணுங்க
3. Repository பெயர்: `vcameras` (அல்லது உங்களுக்கு பிடித்த பெயர்)
4. Description: "Virtual Camera App for Android"
5. "Public" அல்லது "Private" choose பண்ணுங்க
6. ✅ "Add a README file" check **பண்ணாதீங்க**
7. "Create repository" click பண்ணுங்க

#### படி 3: உங்க Code Upload பண்ணுதல்

**முறை A: Git Command Line use பண்ணி**

```bash
# உங்க project folder-க்கு போங்க
cd /path/to/vcameras-fixed

# Git initialize பண்ணுங்க
git init

# எல்லா files-ஐயும் add பண்ணுங்க
git add .

# Commit பண்ணுங்க
git commit -m "Initial commit - VCamera App"

# Remote add பண்ணுங்க (YOUR_USERNAME மாத்துங்க)
git remote add origin https://github.com/YOUR_USERNAME/vcameras.git

# GitHub-க்கு push பண்ணுங்க
git branch -M main
git push -u origin main
```

**முறை B: GitHub Desktop use பண்ணி**

1. https://desktop.github.com-ல இருந்து GitHub Desktop download பண்ணுங்க
2. Install பண்ணி sign in பண்ணுங்க
3. File → Add Local Repository
4. vcameras-fixed folder choose பண்ணுங்க
5. "Publish repository" click பண்ணுங்க

**முறை C: Drag and Drop (எளிமையான முறை!)**

1. GitHub-ல உங்க repository-க்கு போங்க
2. "uploading an existing file" click பண்ணுங்க
3. vcameras-fixed folder-லேர்ந்து எல்லா files-ஐயும் drag பண்ணி drop பண்ணுங்க
4. Commit message: "Initial commit"
5. "Commit changes" click பண்ணுங்க

#### படி 4: GitHub Actions Enable பண்ணுதல்

1. உங்க repository-க்கு போங்க
2. "Actions" tab click பண்ணுங்க
3. "I understand my workflows, go ahead and enable them" click பண்ணுங்க
4. "Android CI Build" workflow பார்க்கணும்

#### படி 5: Build Trigger பண்ணுதல்

1. Actions tab-ல, "Android CI Build" click பண்ணுங்க
2. "Run workflow" dropdown click பண்ணுங்க
3. பச்சை நிற "Run workflow" button click பண்ணுங்க
4. 5-10 நிமிடங்கள் காத்திருங்க
5. முடிஞ்ச workflow run-ல click பண்ணுங்க
6. கீழ scroll பண்ணி "Artifacts" பாருங்க
7. "vcamera-debug" அல்லது "vcamera-release" download பண்ணுங்க

---

## Important Files பற்றி

உங்க repository-ல இந்த முக்கிய files இருக்கணும்:

```
vcameras/
├── .github/
│   └── workflows/
│       └── android.yml          ← GitHub Actions configuration
├── app/
│   ├── build.gradle            ← App build config
│   └── src/                    ← Source code
├── gradle/
│   └── wrapper/                ← Gradle wrapper files
├── opensdk/
│   └── build.gradle            ← Library build config
├── build.gradle                ← Root build config
├── settings.gradle             ← Project settings
├── gradle.properties           ← Gradle properties
├── .gitignore                  ← Files to ignore
├── README.md                   ← Project overview
├── BUILD_INSTRUCTIONS.md       ← Build guide
├── TROUBLESHOOTING.md          ← Problem solving
└── GITHUB_SETUP.md            ← This file
```

---

## Verification / சரிபார்ப்பு

After uploading, check:

✅ Repository has all files
✅ `.github/workflows/android.yml` exists
✅ Actions tab is enabled
✅ Workflow runs successfully
✅ APK is generated in Artifacts

---

## Common Issues

### Issue 1: Actions not showing up
**Solution:** 
- Make sure `.github/workflows/android.yml` file exists
- Go to Settings → Actions → General
- Enable "Allow all actions and reusable workflows"

### Issue 2: Build fails in Actions
**Solution:**
- Check Actions log for errors
- Make sure all files are uploaded
- Verify `android.yml` has correct syntax

### Issue 3: Can't push to GitHub
**Solution:**
```bash
# If authentication fails, use personal access token
# GitHub → Settings → Developer settings → Personal access tokens
# Generate new token with 'repo' scope
# Use token as password when pushing
```

---

## Additional Tips

1. **Private Repository:** 
   - Actions minutes are limited in free plan
   - Public repos get unlimited Actions minutes

2. **Faster Builds:**
   - Enable Gradle caching in workflow
   - Use matrix builds for multiple variants

3. **Automatic Releases:**
   - Create GitHub releases when build succeeds
   - Tag versions (v1.0.0, v1.0.1, etc.)

4. **Build on Push:**
   - Workflow triggers automatically on push to main
   - No need to manually run every time

---

## Need Help?

- GitHub Docs: https://docs.github.com
- GitHub Actions: https://docs.github.com/actions
- Git Guide: https://git-scm.com/doc

**Happy Coding! 🎉**
