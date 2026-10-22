# Testing and Evaluation

See `../BUILD-VERIFICATION.md` for the environment and checks actually executed. See `../TESTING.md` for the 20-case acceptance matrix and `../app/src/test` / `../app/src/androidTest` for executable tests.

Unit tests target independent expected values: salted hash output, peso grouping, cent rounding, VAT, revenue-minus-cost profit, margins, and invalid input boundaries. Instrumented tests target Room foreign keys, insert/query/delete, atomic checkout, insufficient-cash rollback, sale deletion/restocking, owner permissions, login UI, and cart totals.

Acceptance testing should cover an Android 8 device/emulator and a newer device, light/dark themes, large font sizes, portrait/landscape, process restart, permissions denied, unavailable print services, low/out-of-stock products, malformed imports, and limited storage. Run tests on a fresh installation or resettable test database, not live business data.

The supplied BPC validation survey can be administered after participants actually use the app. Obtain voluntary consent using the supplied privacy statement and request letter, complete the team's contact fields, and record observed results. No participant answers, ratings, signatures, photos, or successful device observations have been fabricated.
