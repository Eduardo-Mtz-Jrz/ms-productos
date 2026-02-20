# --- Etapa 1: Build (Compilación) ---
# Usamos Maven con JDK 17 para compilar el proyecto
FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app

# 1. Optimización: Copiar solo el pom.xml y descargar dependencias
# Esto se queda en cache a menos que cambies el pom.xml
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Copiar el código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests

# --- Etapa 2: Runtime (Ejecución) ---
# Usamos una imagen ligera de JRE (Java Runtime Environment) para el contenedor final
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar el JAR generado desde la etapa de build
# Usamos un comodín para capturar el jar generado (asegúrate que solo se genere uno)
COPY --from=build /app/target/*.jar app.jar

# Puerto en el que corre tu MS de Productos
EXPOSE 8081

# Variables de Entorno por defecto
# Estas pueden ser sobrescritas por Docker Compose o Kubernetes
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Variables para la conexión a la DB (ajusta según tu entorno)
ENV DB_URL=jdbc:mysql://mysql-db:3306/productos_db
ENV DB_USER=root
ENV DB_PASSWORD=secret

# Healthcheck: Verifica que el microservicio esté respondiendo
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8081/actuator/health || exit 1

# Comando para arrancar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]