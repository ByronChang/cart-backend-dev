-- Crear la base de datos (si no existe)
-- Crear la base de datos (si no existe) con utf8mb4
CREATE DATABASE IF NOT EXISTS cart_db CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
USE cart_db;

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- Tabla de productos
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    stock INT NOT NULL,
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- Tabla de carritos (cart)
CREATE TABLE IF NOT EXISTS cart (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    added_date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
) CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- Tabla de órdenes (orders)
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    order_date TIMESTAMP NOT NULL,
    shipping_address VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- Tabla de detalles de órdenes (order_details)
CREATE TABLE IF NOT EXISTS order_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
) CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- Insertar datos de inicio
INSERT INTO users (username, email, password, address, birth_date) 
VALUES 
    ('Admin', 'admin@admin.com', 'admin', '123 Main St, Cityville', '1990-01-01'),
    ('User', 'user@user.com', 'user', '456 Elm St, Townsville', '1992-02-02');

INSERT INTO products (name, description, price, stock, image_url) 
VALUES 
    ('Balón de Fútbol', 'Balón de fútbol de alta calidad, ideal para entrenamientos y partidos.', 29.99, 150, 'https://cemacogt.vtexassets.com/arquivos/ids/680803/1117313_2.jpg'),
    ('Raqueta de Tenis', 'Raqueta ligera y duradera, perfecta para jugadores de todos los niveles.', 89.99, 75, 'https://cdn.pixabay.com/photo/2013/07/13/09/45/tennis-racket-155963_1280.png'),
    ('Zapatillas de Correr', 'Zapatillas cómodas y transpirables para correr largas distancias.', 69.99, 200, 'https://sparta.cl/media/catalog/product/z/a/zapatillas-running-hombre-asics-gel-nimbus-27-azul-lateral.png'),
    ('Mancuernas Ajustables', 'Mancuernas ajustables para entrenamientos en casa.', 49.99, 100, 'https://walmartgt.vtexassets.com/arquivos/ids/467363/Mancuerna-Para-Ejercicio-Roker-10kg-1-36869.jpg'),
    ('Pelota de Baloncesto', 'Pelota de baloncesto oficial, ideal para canchas interiores y exteriores.', 39.99, 120, 'https://www.soyvisual.org/sites/default/files/styles/twitter_card/public/images/photos/dep_0002.jpg'),
    ('Bicicleta de Montaña', 'Bicicleta resistente para aventuras en la montaña.', 499.99, 30, 'https://elektragt.vtexassets.com/arquivos/ids/190326/BICICLETA-STEEL-DIAMOND-BL152-35003234-3-.jpg'),
    ('Patines en Línea', 'Patines en línea para disfrutar de la velocidad y el ejercicio.', 79.99, 80, 'https://m.media-amazon.com/images/I/7106xzu63PL._AC_UF1000,1000_QL80_.jpg'),
    ('Equipo de Yoga', 'Esteras y accesorios para una práctica de yoga cómoda.', 39.99, 60, 'https://m.media-amazon.com/images/I/41XNkL2et5L._AC_UF1000,1000_QL80_.jpg'),
    ('Balón de Rugby', 'Balón de rugby de alta calidad, ideal para entrenamientos y partidos.', 34.99, 90, 'https://m.media-amazon.com/images/I/A1wWj5IdjAL.jpg'),
    ('Camiseta Deportiva', 'Camiseta transpirable para actividades deportivas.', 24.99, 200, 'https://leonisa.gt/cdn/shop/files/508007_457_1200X1500_1_480x.jpg'),
    ('Gafas de Natación', 'Gafas de natación con protección UV y ajuste cómodo.', 19.99, 150, 'https://sears.com.gt/wp-content/uploads/imgSears/414256.jpg'),
    ('Escalera de Agilidad', 'Escalera de agilidad para mejorar la velocidad y coordinación.', 29.99, 50, 'https://www.boldtribe.com/cdn/shop/articles/escalera_agilidad_principal_store_1024x1024_fbd8c21f-0f64-4c2a-85f9-03e18ec861c7_1000x.jpg'),
    ('Banda de Resistencia', 'Banda de resistencia para entrenamientos de fuerza.', 15.99, 100, 'https://m.media-amazon.com/images/I/41z9u3Gb8NL._AC_SR300,300.jpg'),
    ('Pelota de Pilates', 'Pelota de pilates para ejercicios de equilibrio y tonificación.', 25.99, 70, 'https://www.redlemon.com.mx/cdn/shop/files/1_9a3d7913-4faa-4a09-9e43-c7f96e876a6b.jpg'),
    ('Cuerda para Saltar', 'Cuerda para saltar ajustable, ideal para cardio.', 12.99, 150, 'https://ferreteriavidri.com/images/items/large/151463.jpg'),
    ('Botella de Agua', 'Botella de agua reutilizable, perfecta para el gimnasio.', 9.99, 200, 'https://m.media-amazon.com/images/I/71-BHueY6eL.jpg'),
    ('Tienda de Campaña', 'Tienda de campaña para aventuras al aire libre.', 199.99, 40, 'https://m.media-amazon.com/images/I/61OML9T0u6L._AC_UF1000,1000_QL80_.jpg'),
    ('Saco de Dormir', 'Saco de dormir ligero y compacto para camping.', 59.99, 50, 'https://m.media-amazon.com/images/I/61xZIgnZVML._AC_UF1000,1000_QL80_.jpg'),
    ('Ciclón de Pesas', 'Ciclón de pesas para entrenamientos de fuerza.', 89.99, 30, 'https://m.media-amazon.com/images/I/61bKyki0k+L._AC_UF350,350_QL80_.jpg'),
    ('Bola de Ejercicio', 'Bola de ejercicio para tonificación y equilibrio.', 34.99, 80, 'https://m.media-amazon.com/images/I/61e8LdJ2AhL._AC_UF1000,1000_QL80_.jpg'),
    ('Guantes de Boxeo', 'Guantes de boxeo de alta calidad para entrenamiento.', 49.99, 60, 'https://m.media-amazon.com/images/I/81u6WuDoB3L._AC_UF894,1000_QL80_.jpg'),
    ('Tobilleras de Pesas', 'Tobilleras de pesas ajustables para entrenamiento.', 19.99, 100, 'https://m.media-amazon.com/images/I/51xHfgCtGxL._AC_UF1000,1000_QL80_.jpg');