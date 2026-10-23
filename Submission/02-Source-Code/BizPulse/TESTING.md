# Device test report

- Tester: Jedediah Ebalobo
- Device: POCO X8 Pro
- Android version: 16
- App version reported: version 1 (submission package 1.0.0)
- Test date: October 19, 2026
- Results confirmed by tester: October 23, 2026

## Overall result

All tested functions worked as expected except barcode scanning. Jedediah Ebalobo confirmed that he completed the tests and encountered no other problems. The 20 existing acceptance cases are marked Pass based on his confirmation; barcode scanning is recorded separately as Fail.

These are tester-reported manual results, not a new automated test run or an independent reproduction. The Input and Expected columns retain the original test scenarios. Detailed measured outputs were not separately supplied, so the Actual column records the tester's confirmation without inventing measurements.

## Actual results and status

| # | Test Case | Input | Expected | Actual | Status |
| --- | --- | --- | --- | --- | --- |
| 1 | Valid login | owner / owner123; Business Owner | Dashboard opens; session persists | Tester confirmed the test worked as expected; no problem reported | Pass |
| 2 | Invalid login | owner / wrongpass | Clear error; no session | Tester confirmed the test worked as expected; no problem reported | Pass |
| 3 | Weak registration | Password 12345 | Reject; no user inserted | Tester confirmed the test worked as expected; no problem reported | Pass |
| 4 | Add product | Unique barcode, valid price/category | Product saved; audit logged | Tester confirmed the test worked as expected; no problem reported | Pass |
| 5 | Edit product | Change stock and price | Changes persist; stale carts rejected | Tester confirmed the test worked as expected; no problem reported | Pass |
| 6 | Delete product | Confirm Delete | Product removed; historical receipt survives | Tester confirmed the test worked as expected; no problem reported | Pass |
| 7 | Out-of-stock cart | Tap a stock-0 item | Cannot add; no negative stock | Tester confirmed the test worked as expected; no problem reported | Pass |
| 8 | Insufficient cash | Cash below total | Complete disabled or rejected; no writes | Tester confirmed the test worked as expected; no problem reported | Pass |
| 9 | Complete sale | Cola × 1; Cash ₱100 | Total ₱99.68; change ₱0.32; stock −1; receipt line saved | Tester confirmed the test worked as expected; no problem reported | Pass |
| 10 | Quick Sale | Enable toggle | Top eight available products appear | Tester confirmed the test worked as expected; no problem reported | Pass |
| 11 | Audit search | Search Sale or a username | Matches filter action/details/user | Tester confirmed the test worked as expected; no problem reported | Pass |
| 12 | Export and import JSON | Export, then import confirmed valid file | All six tables restored atomically; sign out; malformed input rejected | Tester confirmed the test worked as expected; no problem reported | Pass |
| 13 | Cashier inventory | Log in as cashier | Read-only; repository mutation denied | Tester confirmed the test worked as expected; no problem reported | Pass |
| 14 | Cashier Manage | Cashier session | Tab hidden; access/mutations blocked | Tester confirmed the test worked as expected; no problem reported | Pass |
| 15 | Offline operation | Disable network after install | Local sales, accounts and analytics still work | Tester confirmed the test worked as expected; no problem reported | Pass |
| 16 | Print receipt | Print completed sale | System print preview or Save as PDF | Tester confirmed the test worked as expected; no problem reported | Pass |
| 17 | Reset data | Confirm both reset prompts | Demo users/categories/products/sales restored; sign out | Tester confirmed the test worked as expected; no problem reported | Pass |
| 18 | Category analytics | Cola × 1 and Chips × 1 | Beverages ₱89; Snacks ₱45; VAT ₱16.08; grand total ₱150.08 | Tester confirmed the test worked as expected; no problem reported | Pass |
| 19 | Logout confirmation | Cancel, then confirm logout | Cancel retains session; confirm clears cart/session | Tester confirmed the test worked as expected; no problem reported | Pass |
| 20 | Transaction deletion | Delete an itemized sale | Stock restored for existing products; lines cascaded; analytics updated | Tester confirmed the test worked as expected; no problem reported | Pass |
| 21 | Barcode scanning | Scan a product barcode with the camera | Recognize the barcode and add the matching product | Camera opened, but barcode recognition did not function properly and no product was added | Fail |

## Known issue

The camera opened, but barcode scanning did not recognize the barcode or add the product. The cause has not yet been identified. Investigate decoding and product lookup, improve the implementation, and retest with a known product barcode. No barcode fix or APK rebuild is included in this documentation update.

## Supporting evidence

The repository's `Submission/04-UIUX/screenshots` folder contains the supplied app screenshots and two video-extracted images. The demonstration video is in `Submission/06-Presentation`. Password-entry sections are covered in the submitted video copy. Historical automated build and test results remain separately documented in `BUILD-VERIFICATION.md`.

These results apply to the reported device and tests. Additional device configurations, accessibility scenarios, and formal user survey feedback are outside this confirmation.
