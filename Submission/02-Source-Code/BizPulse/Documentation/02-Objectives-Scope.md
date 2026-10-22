# Objectives and Scope

The general objective is to support repeatable inventory and sales recording for a small retail business on Android.

Specific objectives are to validate user inputs; persist accounts, products, categories, transactions, receipt lines, and audit entries in SQLite; ensure checkout cannot partially deduct stock; calculate category analytics from real receipt items; enforce owner-only changes; and provide receipts and portable backups.

The delivered scope includes all seven functional screen groups, six authenticated navigation destinations for owners, local login/register, role checks, product/user/category/transaction CRUD, barcode scanning, quick sale, cash/change validation, period analytics, audit history, JSON backup/import, and reset. Device capabilities include camera, Android print services, FileProvider sharing, and the document picker.

Limitations: this is single-device local storage with no server, payment gateway, cloud synchronization, or push notifications. Stock alerts are in-app. The cashier shift summary represents the current calendar day, not an explicit clock-in session. Demo data is seeded automatically. Cart state is retained during navigation but not process death. The device clock determines timestamps. Manual transactions use zero cost and no itemized category allocation.

Academic acceptance requires the team to test on actual devices, capture screenshots, perform user validation, publish a GitHub repository, document individual contributions, and demonstrate the app. Generated source alone cannot establish a grade.
