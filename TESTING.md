# Acceptance test matrix

These are planned device acceptance cases, not fabricated observations. Automated results are separately recorded in `BUILD-VERIFICATION.md`.

| # | Test Case | Input | Expected | Actual | Status |
| --- | --- | --- | --- | --- | --- |
| 1 | Valid login | owner / owner123; Business Owner | Dashboard opens; session persists | Not observed on device | Pending device test |
| 2 | Invalid login | owner / wrongpass | Clear error; no session | Not observed on device | Pending device test |
| 3 | Weak registration | Password 12345 | Reject; no user inserted | Not observed on device | Pending device test |
| 4 | Add product | Unique barcode, valid price/category | Product saved; audit logged | Not observed on device | Pending device test |
| 5 | Edit product | Change stock and price | Changes persist; stale carts rejected | Not observed on device | Pending device test |
| 6 | Delete product | Confirm Delete | Product removed; historical receipt survives | Not observed on device | Pending device test |
| 7 | Out-of-stock cart | Tap a stock-0 item | Cannot add; no negative stock | Not observed on device | Pending device test |
| 8 | Insufficient cash | Cash below total | Complete disabled or rejected; no writes | Not observed on device | Pending device test |
| 9 | Complete sale | Cola × 1; Cash ₱100 | Total ₱99.68; change ₱0.32; stock −1; receipt line saved | Not observed on device | Pending device test |
| 10 | Quick Sale | Enable toggle | Top eight available products appear | Not observed on device | Pending device test |
| 11 | Audit search | Search Sale or a username | Matches filter action/details/user | Not observed on device | Pending device test |
| 12 | Export and import JSON | Export, then import confirmed valid file | All six tables restored atomically; sign out; malformed input rejected | Not observed on device | Pending device test |
| 13 | Cashier inventory | Log in as cashier | Read-only; repository mutation denied | Not observed on device | Pending device test |
| 14 | Cashier Manage | Cashier session | Tab hidden; access/mutations blocked | Not observed on device | Pending device test |
| 15 | Offline operation | Disable network after install | Local sales, accounts and analytics still work | Not observed on device | Pending device test |
| 16 | Print receipt | Print completed sale | System print preview or Save as PDF | Not observed on device | Pending device test |
| 17 | Reset data | Confirm both reset prompts | Demo users/categories/products/sales restored; sign out | Not observed on device | Pending device test |
| 18 | Category analytics | Cola × 1 and Chips × 1 | Beverages ₱89; Snacks ₱45; VAT ₱16.08; grand total ₱150.08 | Not observed on device | Pending device test |
| 19 | Logout confirmation | Cancel, then confirm logout | Cancel retains session; confirm clears cart/session | Not observed on device | Pending device test |
| 20 | Transaction deletion | Delete an itemized sale | Stock restored for existing products; lines cascaded; analytics updated | Not observed on device | Pending device test |

Use a fresh demo installation. Each case should record actual behavior, device/OS, test date, and tester before being marked Pass. Export a backup before destructive tests. The unit/instrumented suites supplement this matrix; compilation is not a substitute for device acceptance testing.
