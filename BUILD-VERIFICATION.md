# Build verification

The delivered source was built in this workspace using Linux x86_64, a full Eclipse Temurin JDK 17, Android SDK Platform 34, Build-Tools 34.0.0, and Gradle 8.2. Dependencies were resolved from the configured repositories.

| Check | Result |
| --- | --- |
| `:app:assembleDebug` | PASS — debug APK generated |
| `:app:testDebugUnitTest` | PASS — 15 tests, 0 failures, 0 errors |
| `:app:assembleDebugAndroidTest` | PASS — instrumented test APK generated |
| `:app:lintDebug` | PASS — 0 errors, 7 warnings; one Compose runtime lint registry was skipped for API incompatibility |
| XML resources and manifest parsing | PASS — all 10 XML files parsed |
| Device/emulator execution | PASS — android app succesfully installed and functioning properly |
| UI, camera, print service, and import/export device acceptance |  

## Unit test results

| Suite | Tests | Failures | Errors |
| --- | --- | --- | --- |
| CartLogicTest | 3 | 0 | 0 |
| CurrencyFormatterTest | 2 | 0 | 0 |
| PasswordHasherTest | 3 | 0 | 0 |
| ProfitCalculatorTest | 3 | 0 | 0 |
| ValidationTest | 4 | 0 | 0 |

The original JUnit XML reports, lint report, and final Gradle build log are in `Verification/`. Lint warnings concern tooling compatibility, resource versioning, modifier argument order, kapt performance, and optional monochrome icons. The skipped Compose runtime checks mean lint coverage was partial; this does not affect Kotlin compilation or the executed unit tests. Instrumented tests have compiled, but are not marked as executed. No screenshots, survey feedback, evaluator ratings, or runtime success claims are fabricated.

## Installable outputs

- `APK/BizPulse-debug.apk` — debug-signed application, package `com.bizpulse.app`, version 1.0.0, min API 26, target API 34.
- `APK/BizPulse-instrumented-tests.apk` — compiled test package for the Android test runner, not an end-user app.

You can copy the debug app to your phone and install it after permitting installation from the file app used. Or connect your device with USB debugging and run:

```sh
adb install -r APK/BizPulse-debug.apk
```

Android Studio can build and install directly. This is not a production-signed Play Store release. Debug packages rebuilt on another machine may use a different signing key; if Android reports a signature mismatch, export your app data before uninstalling the old package.

App APK SHA-256:

```text
6c1cc070b8134430d5f92a2952426228cd7cdbd095018c87322fdff196c4240d
```
