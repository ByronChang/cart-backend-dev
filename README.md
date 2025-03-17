# Carrito de Compras - Backend

Este es el backend de una aplicación de carrito de compras desarrollada con Spring Boot. Proporciona servicios para el manejo de usuarios, productos, carritos y órdenes.

## Requisitos

- Java 17
- Docker y Docker Compose
- MySQL 8.0

## Configuración del proyecto

### 1. Clonar el repositorio

```bash
git clone https://github.com/ByronChang/cart-backend-dev.git
cd cart-backend-dev

### 2. Construir el proyecto
Compila el proyecto y genera el archivo JAR:

```bash
mvn clean package -DskipTests

### 3. Configurar Docker Compose
Asegúrate de que Docker y Docker Compose estén instalados. Luego, levanta los contenedores:

```bash
docker-compose up --build

Esto levantará dos contenedores:

cart_db: Contenedor de MySQL para la base de datos.

cart_app: Contenedor de la aplicación Spring Boot.

### 4. Acceder a la aplicación
La aplicación estará disponible en:

API: http://localhost:8080

Base de datos: localhost:3306 (usuario: user, contraseña: password)

### Estructura del proyecto

src/main/java: Código fuente de la aplicación.

src/main/resources: Archivos de configuración (como application.properties).

docker-compose.yml: Configuración de Docker Compose.

Dockerfile: Configuración de la imagen Docker para la aplicación.

init.sql: Script SQL para inicializar la base de datos.

### Variables de entorno

La aplicación utiliza las siguientes variables de entorno (configuradas en docker-compose.yml):

SPRING_DATASOURCE_URL: URL de la base de datos (por ejemplo, jdbc:mysql://db:3306/cart_db).

SPRING_DATASOURCE_USERNAME: Usuario de la base de datos.

SPRING_DATASOURCE_PASSWORD: Contraseña de la base de datos.

### Solución de problemas

Error: Communications link failure

Si la aplicación no puede conectarse a la base de datos, asegúrate de:

- Que el contenedor de la base de datos esté en ejecución.

- Que las credenciales de la base de datos sean correctas.

- Que la red de Docker esté configurada correctamente.

Error: mysql: not found

Si el script wait-for-db.sh no puede encontrar el comando mysql, asegúrate de que mysql-client esté instalado en la imagen de la aplicación.