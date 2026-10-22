# System Design

## Entity relationships

```mermaid
erDiagram
  CATEGORIES ||--o{ PRODUCTS : categorizes
  TRANSACTIONS ||--o{ LINE_ITEMS : contains
  USERS {
    long id PK
    string username UK
    string passwordHash
    string role
  }
  CATEGORIES {
    long id PK
    string name UK
  }
  PRODUCTS {
    long id PK
    long categoryId FK
    string barcode UK
    double price
    double cost
    int stock
  }
  TRANSACTIONS {
    long id PK
    long cashierId
    string cashierName
    double total
    double cost
    double profit
    boolean manual
  }
  LINE_ITEMS {
    long id PK
    long transactionId FK
    long productId
    long categoryId
    string productName
    int quantity
    double price
    double cost
  }
  AUDIT_LOG {
    long id PK
    long timestamp
    string userName
    string action
  }
```

`cashierId`, `productId`, and the category ID on a receipt are historical snapshots rather than delete-cascading live references. This permits removal of live users/products without corrupting past receipts. Categories used by lines remain protected by repository validation. The explicit foreign keys are products→categories (RESTRICT) and lines→transactions (CASCADE). Unique indexes protect usernames, category names, and product barcodes. Dates and audit timestamps are indexed.

## Use cases

```mermaid
flowchart TD
  Owner["Business Owner"] --> Shared["Login • dashboard • inventory view • guide"]
  Cashier["Cashier"] --> Shared
  Owner --> Admin["Product/user/category/record CRUD"]
  Owner --> Backup["Backup • import • reset • audit"]
  Shared --> POS["Search/scan • cart • payment • receipt"]
  Shared --> Reports["Sales analytics • low-stock alerts"]
```

## Checkout flow

```mermaid
flowchart TD
  Start["Add or scan product"] --> Stock{"Stock available?"}
  Stock -->|No| Error["Show input error"]
  Stock -->|Yes| Cart["Review cart and 12% VAT"]
  Cart --> Payment["Choose payment and tendered cash"]
  Payment --> Valid{"Payment valid?"}
  Valid -->|No| Error
  Valid -->|Yes| Atomic["Room transaction: recheck products, save sale/items, deduct stock, audit"]
  Atomic --> Commit{"All writes succeed?"}
  Commit -->|Yes| Receipt["Show/print receipt and clear cart"]
  Commit -->|No| Rollback["Roll back all writes and retain cart"]
  Error --> Cart
```

## Mutation and failure rules

All database operations occur off the main thread. Critical multi-record writes use `withTransaction`. Checkout refreshes products and verifies stock and price snapshots before writing. Failed cash validation or stock checks cannot leave partial sales. Sale deletion restores existing products and cascades line deletion. Import validates the complete input, then replaces all tables atomically in foreign-key-safe order. Export reads all six tables in a consistent transaction. DAO insertion uses ABORT rather than silent replacement.

Repository methods re-read the logged-in user and enforce owner rights. UI visibility is an additional convenience, not the authorization boundary. At least one owner must remain, and the active owner cannot delete or demote their own account.
