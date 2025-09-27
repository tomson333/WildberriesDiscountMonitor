# WildberriesDiscountMonitor Debug-ready Project

This project is prepared to build a **debug APK** via GitHub Actions (no signing secrets required).

How to use:
1. Unpack the archive.
2. Initialize git and push to your GitHub repo (see commands below).
3. GitHub Actions workflow `.github/workflows/android-debug.yml` will run `assembleDebug` and upload `app-debug.apk` as artifact.
4. Download and install `app-debug.apk` on your phone for testing.

Git commands example (replace with your repo):
```bash
git init
git add .
git commit -m "Initial commit - debug-ready"
git branch -M main
git remote add origin https://github.com/tomson333/WildberriesDiscountMonitor.git
git push -u origin main --force
```

After push: go to GitHub → Actions → choose "Android Debug Build" workflow run → open the run → download artifact `app-debug.apk`.

Notes:
- This is a minimal demo app. You may need to adjust `PriceCheckWorker` parsing for Wildberries pages.
- For production publishing, you'll need to sign the APK with a release key.
