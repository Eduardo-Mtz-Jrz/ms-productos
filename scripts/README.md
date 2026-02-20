# Base de Datos MS-Productos

## Descripción

Base de datos para el microservicio de gestión de productos con funcionalidades avanzadas:
- ✅ Catálogo de productos con categorías
- ✅ Control de inventario con versionado (Optimistic Locking)
- ✅ Historial de movimientos de inventario
- ✅ Idempotencia en operaciones POST

## Estructura

### Tablas

1. **categories** - Catálogo de categorías
2. **products** - Productos con relación a categorías
3. **inventory_movements** - Historial de movimientos de stock
4. **inventory_idempotency** - Control de idempotencia
5. **idempotency_responses** - Cache de respuestas idempotentes

## Diagrama ER
```
categories (1) ──── (N) products (1) ──── (N) inventory_movements
                         │
                         └──── (N) inventory_idempotency
```

## Instalación

### Opción 1: Docker Compose (Recomendado)
```bash
docker-compose up -d
```

El script `01-init-database.sql` se ejecuta automáticamente.

### Opción 2: Manual
```bash
# Conectarse a MySQL
docker exec -it mysql-productos mysql -u productos_user -pproductos_pass ms_productsdb

# Ejecutar script
SOURCE /docker-entrypoint-initdb.d/01-init-database.sql;
```

### Opción 3: Desde archivo local
```bash
docker exec -i mysql-productos mysql -u productos_user -pproductos_pass ms_productsdb < scripts/01-init-database.sql
```

## Datos Iniciales

- 5 categorías predefinidas
- 10 productos de ejemplo
- 5 movimientos de inventario de ejemplo

## Acceso a la Base de Datos

### Conexión Directa
```bash
docker exec -it mysql-productos mysql -u productos_user -pproductos_pass ms_productsdb
```

### Adminer (GUI Web)
```
http://localhost:8080
Server: mysql-productos
Username: productos_user
Password: productos_pass
Database: ms_productsdb
```

## Configuración de Conexión

### Local (application-local.yml)
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ms_productsdb
    username: productos_user
    password: productos_pass
```

### Docker (application.yml)
```yaml
spring:
  datasource:
    url: jdbc:mysql://mysql-productos:3306/ms_productsdb
    username: productos_user
    password: productos_pass
```

## Consultas Útiles
```sql
-- Ver productos con stock bajo
SELECT * FROM products WHERE stock < 20;

-- Ver movimientos de un producto
SELECT * FROM inventory_movements WHERE product_id = 1 ORDER BY created_at DESC;

-- Ver productos por categoría
SELECT p.*, c.name as categoria 
FROM products p 
JOIN categories c ON p.category_id = c.id 
WHERE c.name = 'ELECTRONICA';
```

## Mantenimiento

### Backup
```bash
docker exec mysql-productos mysqldump -u productos_user -pproductos_pass ms_productsdb > backup.sql
```

### Restaurar
```bash
docker exec -i mysql-productos mysql -u productos_user -pproductos_pass ms_productsdb < backup.sql
```

### Resetear Base de Datos
```bash
docker-compose down -v
docker-compose up -d
```