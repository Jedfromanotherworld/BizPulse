# BizPulse implementation guide

## Section 1: Overview and setup

Start with `README.md`. Open the root folder in Android Studio, select JDK 17, install SDK 34 and Build-Tools 34.0.0, sync, and run `app`. All source files are complete files in this archive; `SOURCE-FILES.md` also contains full text listings grouped by the requested sections.

## Section 2: Gradle configuration files

`settings.gradle.kts` defines repositories and the app module. Root `build.gradle.kts` pins AGP/Kotlin/Hilt. `gradle.properties` sets AndroidX, memory, and Kotlin conventions. `app/build.gradle.kts` declares Android settings, Compose, Room, Hilt, Navigation, DataStore, barcode scanning, and tests. `gradle/wrapper` includes the actual official wrapper binary and Gradle 8.2 properties; `gradlew` and `gradlew.bat` run it.

## Section 3: Android manifest and resources

`app/src/main/AndroidManifest.xml` registers Hilt's Application, launcher Activity, optional camera, and a narrow FileProvider for exported backups. There is no internet permission. Resources contain the brand icon, theme, app name, and explicit backup exclusions. Both adaptive launcher icons support the minimum API 26.

## Section 4: Data layer

Six entities and six DAOs live under `data/db`. A product has a category FK with RESTRICT. A line item has a transaction FK with CASCADE. Historical product/cashier IDs deliberately remain snapshot references so deleted live records do not erase receipts. `DatabaseSeeder` runs synchronously in Room's creation callback and inserts real line items. A successful initial database open cannot expose partially seeded data. `TransactionWithItems` joins header/lines with Room's transaction annotation.

## Section 5: Repositories and ViewModels

Repositories authorize operations, validate inputs, and transact related writes. `AppStore` combines record Flows with DataStore identity. Each screen ViewModel extends `ScreenViewModel`, which provides StateFlow-backed form, modal, filter, busy, and error state. Duplicate checkout taps are guarded; failures return user-facing messages. A successful import signs out and clears navigation state.

## Section 6: UI theme and components

`ui/theme` contains the specified colors, typography, rounded shapes, and light/dark color schemes. `ui/components` contains reusable controls and cards. Emerald buttons use dark text for contrast. Standard padding is 16dp, small spacing 8dp, and buttons have at least 48dp height. Bar values are available by tapping and as textual amounts.

## Section 7: Screens

- **Auth:** role selection, login, registration, visibility toggle, confirmation, and demo hints.
- **Dashboard:** daily and total summaries, low stock, top category, cashier daily shift summary, target, and seven-day chart.
- **Inventory:** search, all/low/out-of-stock filters, owner CRUD, and cashier read-only view.
- **POS:** camera/manual/simulated scanning, search, Quick Sale, stock limits, cart, VAT, payment, change, receipt, and printing.
- **Analytics:** 7/30/all-time periods, totals, real category revenue, VAT and unallocated manual totals, and recent transactions.
- **Manage:** users, sales, audit, categories, target, export/import, and double-confirmed reset. Repository checks enforce owner-only operations.
- **Guide:** role-specific instructions, demo credentials, offline behavior, and scope.

## Section 8: Navigation and MainActivity

`MainActivity` enables edge-to-edge rendering and uses the Compose theme. `AppNavigation` uses Navigation Compose, hides bottom navigation on login, and clears active and saved screen state on user changes. Top-level destinations are Dashboard, Inventory, POS, Analytics, Manage (owner only), and Guide. Full screen headers use the requested screen names.

## Section 9: Utilities

`PasswordHasher` implements the exact requested SHA-256/password-plus-salt format. `CurrencyFormatter` uses peso symbols and two-decimal grouping independent of device locale. `DateUtils` uses device-local date/time. `BackupManager` serializes all six tables explicitly, limits imports to 10 MB, validates relationships and receipt math, and replaces records in one transaction. `ReceiptPrinter` creates multipage PDF pages through Android's print framework. A partial/invalid import rolls back and leaves the old database intact.

## Section 10: Tests

`app/src/test` has hashing, currency, cart arithmetic, profit/margin, and validation tests. `app/src/androidTest` has Room relationship tests, checkout/rollback/stock-restoration/permission tests, login UI tests, and an add-to-cart UI test. Actual execution results are in `BUILD-VERIFICATION.md`. User validation surveys remain a real-world task for the project team.

## Section 11: Documentation files

`Documentation/01-Project-Overview.md` through `06-Conclusion.md` describe the implementation. `03-System-Design.md` includes Mermaid ER, use-case, and checkout-flow diagrams. `TESTING.md` has the requested 20-case matrix. `Documentation/wireframes.svg` is an editable vector mockup. `screenshots/README.md` describes genuine screenshot evidence to collect. `LICENSE` is MIT.

## Section 12: Troubleshooting common build errors

| Symptom | Action |
| --- | --- |
| Gradle sync still in progress | Check the Build/Sync log. First sync downloads dependencies; verify network access and wait for active downloads. Do not start multiple syncs. |
| Android Gradle plugin requires Java 17 | Set the project's Gradle JDK to JDK 17 and sync again. |
| Unsupported class file major version | Check that Gradle is using JDK 17, not a newer bundled runtime. |
| SDK location not found | Select a local SDK in SDK Manager; let Android Studio create `local.properties`. |
| Failed to find Platform SDK 34 | Install Android SDK Platform 34 and Build-Tools 34.0.0. |
| Could not resolve dependencies/plugin | Disable Gradle offline mode, check proxy settings, and allow Google Maven/Maven Central/Plugin Portal/Gradle distribution downloads. |
| Permission denied running gradlew | Run `chmod +x gradlew`, or on Windows use `gradlew.bat`. |
| GradleWrapperMain missing | Restore the bundled `gradle/wrapper/gradle-wrapper.jar`; official source: https://raw.githubusercontent.com/gradle/gradle/v8.2.0/gradle/wrapper/gradle-wrapper.jar. |
| App has old experimental data | Export it first. Use Manage → Backup → Reset All Data; resetting destroys current records. |
| Login rejected | Match the selected role to the account; usernames are case-normalized. |
| Camera denied | Allow Camera permission, or search/enter the barcode. Scanning real products works only for barcodes registered in inventory. |
| Print provider unavailable | Install/enable a print service, or use Save as PDF when offered by Android. |
| Import rejected | Use a BizPulse version-1 JSON export with intact IDs, records, and computed receipt totals. No arbitrary web localStorage conversion is assumed. |
| POS reports a changed price | Clear the cart, then re-add products to accept the current inventory prices. |

The pinned Gradle/JDK pairing follows the official AGP 8.2 documentation: https://developer.android.com/build/releases/agp-8-2-0-release-notes. Compose compiler/Kotlin compatibility: https://developer.android.com/jetpack/androidx/releases/compose-kotlin.
