# WildberriesDiscountMonitorFullFixed

This is a ready-to-push Android debug project for testing on device.

How to use:
1. Unpack the archive.
2. In the project root (folder containing gradlew and app/) run git commands to push to your repo.
3. GitHub Actions workflow `.github/workflows/android-debug.yml` will run `assembleDebug` using Gradle action (no signing required).
4. Download artifact `app-debug-apk` → install `app-debug.apk` on your phone.

Notes:
- CI uses Gradle 9.1 via gradle/gradle-build-action; no local gradlew is required.
- The price-parsing logic may require adjustments for Wildberries pages.
