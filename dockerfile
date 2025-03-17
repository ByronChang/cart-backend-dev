# Usar una imagen base de Java con Spring Boot
FROM openjdk:17-jdk-alpine

WORKDIR /app

# Copiar el script de espera
COPY wait-for-db.sh /wait-for-db.sh
RUN chmod +x /wait-for-db.sh

# Copiar el archivo JAR generado por Maven/Gradle
COPY target/cart-backend-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto en el que corre la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["/wait-for-db.sh", "db", "3306", "java", "-jar", "app.jar"]