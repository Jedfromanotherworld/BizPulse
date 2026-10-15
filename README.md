# BizPulse

**GitHub:** https://github.com/Jedfromanotherworld/BizPulse  
**Start here for push/pull:** [START-HERE-GITHUB.md](START-HERE-GITHUB.md)

Small Business Management for Android: POS, inventory, analytics, and audit history.

**Category:** Business Application — CT-MAD-213, Bulacan Polytechnic College  
**Version:** 1.0.0  
**Package:** `com.bizpulse.app`  
**Platform:** Android 8.0+ (API 26); compile/target API 34

## Open in Android Studio

1. Extract `BizPulse-GitHub-Ready.zip` to a writable folder, such as `C:\AndroidProjects`.
2. Open Android Studio Hedgehog or a version that supports Android Gradle Plugin 8.2.2. Select **Open**, then the extracted **BizPulse** folder containing `settings.gradle.kts`.
3. In **Settings → Build, Execution, Deployment → Build Tools → Gradle**, select **JDK 17** as the Gradle JDK. Do not use JDK 21 for this pinned toolchain.
4. In SDK Manager, install **Android SDK Platform 34**, **Build-Tools 34.0.0**, and **Platform-Tools**. Accept the SDK licenses.
5. Allow Gradle sync to finish. The first sync requires internet access to Google Maven, Maven Central, the Gradle Plugin Portal, and Gradle distributions.
6. Choose an API 26+ emulator or enable USB debugging on a physical Android phone. Select the **app** run configuration and press **Run**.
7. Log in using the matching role and demo credentials below. Seed data is created only when the database is first created.

The official Gradle 8.2 wrapper JAR and scripts are included. No manual source-file copying is necessary. Android Studio creates machine-specific `local.properties`; it is intentionally excluded from the archive.

If using Git, clone your own uploaded repository, enter its directory, and follow steps 2–7. No remote repository has been created by this delivery.

## Demo credentials

| Role | Username | Password |
| --- | --- | --- |
| Business Owner | owner | owner123 |
| Cashier | cashier | cashier123 |
| Cashier | ana | ana123 |

## Features

- Login and registration with role selection and persistent local sessions.
- Owner product, user, category, and transaction CRUD with validation.
- Repository-level owner authorization; cashier inventory is read-only.
- Local Room database, foreign keys, unique indexes, and immutable receipt snapshots.
- POS search, camera barcode scanning, simulated scanning, and Quick Sale.
- Atomic checkout: sale + receipt items + stock update + audit entry.
- Cash, Card, and GCash recording; cash validation and change calculation.
- Android print framework receipts with multipage PDF output.
- Dashboard, daily target, seven-day chart, low-stock alerts, and cashier daily summary.
- Analytics from actual line items, including explicit VAT/manual-entry reconciliation.
- Audit search, JSON export through FileProvider, validated transactional import, and double-confirmed reset.
- Material 3, emerald/slate colors, dark mode, edge-to-edge insets, and offline operation.

## Tech stack and architecture

Kotlin 1.9.22, Compose compiler 1.5.8, Compose BOM 2024.02.02, Room 2.6.1,
Hilt 2.50, Navigation Compose 2.7.7, DataStore Preferences 1.0.0, Coroutines,
Gradle KTS, JDK 17, AGP 8.2.2, Gradle 8.2. Versions are pinned to match API 34.

```mermaid
flowchart TD
  UI["Compose screens"] --> VM["Screen ViewModels • StateFlow"]
  VM --> Repo["Repositories • validation and authorization"]
  Repo --> Room["Room • SQLite transactions"]
  VM --> Session["DataStore • local session"]
  Room --> Store["AppStore • reactive snapshots"]
  Session --> Store
  Store --> VM
```

`data/model` uses type aliases for immutable database records; `CartItem` is a distinct domain model. `AppStore` combines database streams for this small local dataset. Per-screen ViewModels expose UI/form state as StateFlow. Database work uses IO dispatchers; Room suspending operations also use Room's query/transaction executors.

## Financial and record rules

Prices are **before VAT**. Checkout computes rounded subtotal, adds rounded 12% VAT, and rounds the grand total to two decimals. As requested, displayed profit is collected revenue minus cost, so it includes VAT; this is a demonstration metric, not tax accounting. Category figures are before VAT and come exclusively from receipt items. Manual entries do not allocate category revenue; their cost is zero and they do not change stock.

Editing a POS transaction permits metadata corrections only; its item count and total are locked to its receipt. Deleting a sale restores quantities to products that still exist and cascades deletion of receipt lines. Product/user deletions preserve historical names, prices, costs, and IDs in sale snapshots. Categories used by historical items cannot be deleted. Seed stock is the opening balance after the six historical sample sales.

## Build and test

Windows:
```bat
gradlew.bat assembleDebug testDebugUnitTest
gradlew.bat assembleDebugAndroidTest
gradlew.bat connectedDebugAndroidTest
```

macOS/Linux:
```sh
chmod +x gradlew
./gradlew assembleDebug testDebugUnitTest
./gradlew assembleDebugAndroidTest
./gradlew connectedDebugAndroidTest
```

The last command requires a connected emulator/device. Use a fresh test installation with demo records; instrumentation tests alter local session state. See `BUILD-VERIFICATION.md` for actual checks performed, and `TESTING.md` for the 20-case acceptance matrix. The APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

## Screenshots and design

See `screenshots/README.md` for the device-capture checklist and `Documentation/wireframes.svg` for clearly labeled layout wireframes. No mockup is represented as a running-app screenshot. The original web source was not included, so visual fidelity follows the written design system, not a pixel comparison.

## Roadmap

- Replace demo authentication with unique per-user password salts and a slow KDF; make owner enrollment controlled.
- Add decimal/minor-unit monetary persistence and jurisdiction-specific tax rules before commercial use.
- Add production signing, encrypted backups, larger-dataset paging, accessibility/device evaluation, and real user survey results.
- Publish the source to the team's GitHub repository and capture actual demonstration screenshots.

## Developers and attribution

Prepared for JED's BizPulse project using the supplied specification and school references, with AI-assisted implementation. The team must record its own member names, individual contributions, Git history, and evaluation results before submission; these were not supplied and are not fabricated here.

Libraries: AndroidX (Apache 2.0), Kotlin/Coroutines (Apache 2.0), Dagger/Hilt (Apache 2.0), ZXing Android Embedded (Apache 2.0). Source code is MIT licensed; third-party dependencies keep their own licenses. See `LICENSE` and `Documentation/THIRD-PARTY.md`.

## Scope limits

This is a single-device academic app. Card/GCash buttons record externally collected payments; they do not contact a payment provider. Owner self-registration and the requested fixed-salt SHA-256 scheme are demonstration choices. Backups include password hashes and are unencrypted. Android automatic backup is disabled; explicit exports are available. Daily target is a device preference, separate from the six-table database export. UI-only carts do not survive process death. No push notification service, cloud sync, certified tax receipt, remote GitHub publication, signed release, or completed user survey is claimed.

Read `PROJECT-GUIDE.md` for the requested 12-section code map and build troubleshooting.
