# Device test report

- Tester: Jedediah Ebalobo
- Device: POCO X8 Pro
- Android: 16
- App version reported by tester: version 1; bundled package: 1.0.0
- Actual test date: October 19, 2026

## Overall result

The tester reported that all functions, including account creation and password handling, worked except barcode scanning. This is an overall tester report, not an independently verified pass for every case below.

| Function | Actual result and evidence | Status |
| --- | --- | --- |
| Login and registration | Tester reports success; recording shows registration/login and authenticated screens | Reported working |
| Owner and cashier dashboards | Both role interfaces are visible in screenshots/video | Observed display |
| Inventory and product form | Inventory, search and Add Product dialog visible | Observed display; complete CRUD assertions not established |
| POS checkout | Screenshot shows Rice 5kg, subtotal PHP250, VAT PHP30, total PHP280, and completed receipt | Observed completed sale; before/after stock not established |
| Receipt printing | Android print preview is visible | Preview observed; physical printing not verified |
| Analytics, Manage and audit | Screens/video show charts, users, transactions and audit entries | Observed display |
| Dark mode | Dark interface visible throughout evidence | Observed display |
| Camera access | Camera preview opens | Observed preview |
| Barcode recognition | Tester reports barcode does not respond/function properly | FAIL / unresolved |
| Other functions and password checks | Tester reports working; individual inputs/results are not fully recorded | Reported working; edge cases need verification |

## Known issue and recommendation

Barcode scanning did not reliably recognize a barcode. Camera opening is separate from successful decoding and product lookup. The exact cause has not been determined. Diagnose with a readable barcode matching a saved product, record the scanned value and lookup outcome, correct the issue, and retest. No barcode repair or APK rebuild was performed for this submission revision.

## Evidence

24 original screenshots and 2 video-extracted images are in 04-UIUX/screenshots. The submission video is in 06-Presentation. Password-entry sections have been covered and audio removed from that copy. The original recording is about 3 minutes 21 seconds. Screenshot and video review does not prove all boundary conditions, permission denial, data restore or stock restoration.

## Detailed acceptance cases

The matrix below preserves the planned cases. Cases without their exact inputs and outcomes remain unverified even though the tester reported overall success.

| # | Test Case | Input | Expected | Actual | Status |
| --- | --- | --- | --- | --- | --- |
| 1 | Valid login | owner / owner123; Business Owner | Dashboard opens; session persists | Specific case not individually verified | Further verification needed |
| 2 | Invalid login | owner / wrongpass | Clear error; no session | Specific case not individually verified | Further verification needed |
| 3 | Weak registration | Password 12345 | Reject; no user inserted | Specific case not individually verified | Further verification needed |
| 4 | Add product | Unique barcode, valid price/category | Product saved; audit logged | Specific case not individually verified | Further verification needed |
| 5 | Edit product | Change stock and price | Changes persist; stale carts rejected | Specific case not individually verified | Further verification needed |
| 6 | Delete product | Confirm Delete | Product removed; historical receipt survives | Specific case not individually verified | Further verification needed |
| 7 | Out-of-stock cart | Tap a stock-0 item | Cannot add; no negative stock | Specific case not individually verified | Further verification needed |
| 8 | Insufficient cash | Cash below total | Complete disabled or rejected; no writes | Specific case not individually verified | Further verification needed |
| 9 | Complete sale | Cola × 1; Cash ₱100 | Total ₱99.68; change ₱0.32; stock −1; receipt line saved | Specific case not individually verified | Further verification needed |
| 10 | Quick Sale | Enable toggle | Top eight available products appear | Specific case not individually verified | Further verification needed |
| 11 | Audit search | Search Sale or a username | Matches filter action/details/user | Specific case not individually verified | Further verification needed |
| 12 | Export and import JSON | Export, then import confirmed valid file | All six tables restored atomically; sign out; malformed input rejected | Specific case not individually verified | Further verification needed |
| 13 | Cashier inventory | Log in as cashier | Read-only; repository mutation denied | Specific case not individually verified | Further verification needed |
| 14 | Cashier Manage | Cashier session | Tab hidden; access/mutations blocked | Specific case not individually verified | Further verification needed |
| 15 | Offline operation | Disable network after install | Local sales, accounts and analytics still work | Specific case not individually verified | Further verification needed |
| 16 | Print receipt | Print completed sale | System print preview or Save as PDF | Specific case not individually verified | Further verification needed |
| 17 | Reset data | Confirm both reset prompts | Demo users/categories/products/sales restored; sign out | Specific case not individually verified | Further verification needed |
| 18 | Category analytics | Cola × 1 and Chips × 1 | Beverages ₱89; Snacks ₱45; VAT ₱16.08; grand total ₱150.08 | Specific case not individually verified | Further verification needed |
| 19 | Logout confirmation | Cancel, then confirm logout | Cancel retains session; confirm clears cart/session | Specific case not individually verified | Further verification needed |
| 20 | Transaction deletion | Delete an itemized sale | Stock restored for existing products; lines cascaded; analytics updated | Specific case not individually verified | Further verification needed |

Use a fresh demo installation. Each case should record actual behavior, device/OS, test date, and tester before being marked Pass. Export a backup before destructive tests. The unit/instrumented suites supplement this matrix; compilation is not a substitute for device acceptance testing.
