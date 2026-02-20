-- ============================================
-- MS-PRODUCTOS - SCRIPT DE INICIALIZACIÓN
-- ============================================

USE ms_productsdb;

-- ============================================
-- ELIMINAR TABLAS SI EXISTEN
-- ============================================
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS idempotency_responses;
DROP TABLE IF EXISTS inventory_idempotency;
DROP TABLE IF EXISTS inventory_movements;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- TABLA: CATEGORIES
-- ============================================
CREATE TABLE categories (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(100) UNIQUE NOT NULL,
                            description VARCHAR(500),
                            active TINYINT(1) DEFAULT 1,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            INDEX idx_name (name),
                            INDEX idx_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: PRODUCTS
-- ============================================
CREATE TABLE products (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          code VARCHAR(50) UNIQUE NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          stock INT NOT NULL DEFAULT 0,
                          price DECIMAL(10,2) NOT NULL,
                          category_id BIGINT NOT NULL,
                          version INT NOT NULL DEFAULT 0,
                          active TINYINT(1) DEFAULT 1,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          INDEX idx_code (code),
                          INDEX idx_category_id (category_id),
                          INDEX idx_name (name),
                          INDEX idx_price (price),
                          INDEX idx_active (active),
                          CONSTRAINT fk_product_category FOREIGN KEY (category_id)
                              REFERENCES categories(id)
                              ON DELETE RESTRICT
                              ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: INVENTORY_MOVEMENTS
-- ============================================
CREATE TABLE inventory_movements (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     product_id BIGINT NOT NULL,
                                     movement_type ENUM('ENTRADA', 'SALIDA', 'AJUSTE') NOT NULL,
                                     quantity INT NOT NULL,
                                     previous_stock INT NOT NULL,
                                     new_stock INT NOT NULL,
                                     reason VARCHAR(500),
                                     user_name VARCHAR(100),
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     INDEX idx_product_id (product_id),
                                     INDEX idx_movement_type (movement_type),
                                     INDEX idx_created_at (created_at),
                                     CONSTRAINT fk_movement_product FOREIGN KEY (product_id)
                                         REFERENCES products(id)
                                         ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: INVENTORY_IDEMPOTENCY
-- ============================================
CREATE TABLE inventory_idempotency (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       idempotency_key VARCHAR(100) NOT NULL UNIQUE,
                                       product_id BIGINT NOT NULL,
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       INDEX idx_idempotency_key (idempotency_key),
                                       INDEX idx_product_id (product_id),
                                       CONSTRAINT fk_idempotency_product FOREIGN KEY (product_id)
                                           REFERENCES products(id)
                                           ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: IDEMPOTENCY_RESPONSES (CACHE)
-- ============================================
CREATE TABLE idempotency_responses (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       idempotency_key VARCHAR(100) NOT NULL UNIQUE,
                                       response_body TEXT NOT NULL,
                                       status_code INT NOT NULL,
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       INDEX idx_idempotency_key (idempotency_key),
                                       CONSTRAINT fk_response_idempotency FOREIGN KEY (idempotency_key)
                                           REFERENCES inventory_idempotency(idempotency_key)
                                           ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- DATOS INICIALES: CATEGORIES
-- ============================================
INSERT INTO categories (name, description, active) VALUES
                                                       ('ELECTRONICA', 'Dispositivos electrónicos y computadoras', 1),
                                                       ('ACCESORIOS', 'Accesorios y periféricos', 1),
                                                       ('TELEFONIA', 'Teléfonos móviles y smartphones', 1),
                                                       ('AUDIO', 'Equipos de audio y audífonos', 1),
                                                       ('ALMACENAMIENTO', 'Dispositivos de almacenamiento', 1);

-- ============================================
-- DATOS INICIALES: PRODUCTS
-- ============================================
INSERT INTO products (code, name, description, stock, price, category_id, active) VALUES
                                                                                      ('ELEC-001', 'Laptop Dell XPS 13', 'Laptop ultraligera con procesador Intel i7 de 11va generación, 16GB RAM, 512GB SSD', 15, 25999.99, 1, 1),
                                                                                      ('ACC-001', 'Mouse Logitech MX Master 3', 'Mouse ergonómico inalámbrico con sensor de alta precisión', 50, 1499.99, 2, 1),
                                                                                      ('ACC-002', 'Teclado Mecánico RGB', 'Teclado gaming con switches azules y retroiluminación RGB personalizable', 30, 2299.99, 2, 1),
                                                                                      ('ELEC-002', 'Monitor LG 27 4K', 'Monitor 27 pulgadas resolución 4K UHD con HDR10', 20, 8999.99, 1, 1),
                                                                                      ('ACC-003', 'Webcam Logitech C920', 'Webcam Full HD 1080p con enfoque automático', 40, 1899.99, 2, 1),
                                                                                      ('TEL-001', 'iPhone 15 Pro', 'Smartphone Apple última generación con chip A17 Pro', 25, 28999.99, 3, 1),
                                                                                      ('TEL-002', 'Samsung Galaxy S24', 'Smartphone Samsung flagship con pantalla AMOLED', 18, 24999.99, 3, 1),
                                                                                      ('AUD-001', 'AirPods Pro', 'Audífonos inalámbricos con cancelación activa de ruido', 60, 5999.99, 4, 1),
                                                                                      ('ALM-001', 'Disco Duro SSD 1TB', 'Almacenamiento de estado sólido NVMe 1TB', 45, 2499.99, 5, 1),
                                                                                      ('ACC-004', 'Cable USB-C', 'Cable de carga USB-C 2 metros trenzado', 100, 199.99, 2, 1);

-- ============================================
-- DATOS DE EJEMPLO: INVENTORY_MOVEMENTS
-- ============================================
INSERT INTO inventory_movements (product_id, movement_type, quantity, previous_stock, new_stock, reason, user_name) VALUES
                                                                                                                        (1, 'ENTRADA', 10, 5, 15, 'Reabastecimiento de almacén', 'admin'),
                                                                                                                        (2, 'SALIDA', 5, 55, 50, 'Venta cliente ABC', 'vendedor1'),
                                                                                                                        (3, 'ENTRADA', 20, 10, 30, 'Nueva compra proveedor', 'admin'),
                                                                                                                        (4, 'SALIDA', 3, 23, 20, 'Venta corporativa', 'vendedor2'),
                                                                                                                        (5, 'AJUSTE', 40, 35, 40, 'Ajuste por inventario físico', 'admin');

-- ============================================
-- VERIFICACIÓN
-- ============================================
SELECT 'Categorías creadas:' as info, COUNT(*) as total FROM categories;
SELECT 'Productos creados:' as info, COUNT(*) as total FROM products;
SELECT 'Movimientos registrados:' as info, COUNT(*) as total FROM inventory_movements;