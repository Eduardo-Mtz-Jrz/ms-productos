# Diagrama ER - MS-Productos

## Relaciones
```mermaid
erDiagram
    CATEGORIES ||--o{ PRODUCTS : "has"
    PRODUCTS ||--o{ INVENTORY_MOVEMENTS : "has"
    PRODUCTS ||--o{ INVENTORY_IDEMPOTENCY : "has"
    INVENTORY_IDEMPOTENCY ||--|| IDEMPOTENCY_RESPONSES : "links"

    CATEGORIES {
        BIGINT id PK
        VARCHAR name UK
        VARCHAR description
        TINYINT active
        TIMESTAMP created_at
        TIMESTAMP updated_at
    }

    PRODUCTS {
        BIGINT id PK
        VARCHAR code UK
        VARCHAR name
        TEXT description
        INT stock
        DECIMAL price
        BIGINT category_id FK
        INT version
        TINYINT active
        TIMESTAMP created_at
        TIMESTAMP updated_at
    }

    INVENTORY_MOVEMENTS {
        BIGINT id PK
        BIGINT product_id FK
        ENUM movement_type
        INT quantity
        INT previous_stock
        INT new_stock
        VARCHAR reason
        VARCHAR user_name
        TIMESTAMP created_at
    }

    INVENTORY_IDEMPOTENCY {
        BIGINT id PK
        VARCHAR idempotency_key UK
        BIGINT product_id FK
        TIMESTAMP created_at
    }

    IDEMPOTENCY_RESPONSES {
        BIGINT id PK
        VARCHAR idempotency_key UK FK
        TEXT response_body
        INT status_code
        TIMESTAMP created_at
    }
```