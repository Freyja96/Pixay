-- 1. ROLES
INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_ARTIST');

-- 2. USUARIOS
-- Contraseñas cifradas con BCrypt:
-- 'admin123' -> $2a$10$X9nScW8ZpSFSK5f8f/xG9uFk/E6W5m8qB9.uF6G2H5m8qB9.uF6G2
-- '1234'     -> $2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xdq17LkRw0uEcSgy
-- 2. USUARIOS (admin / admin123)
INSERT INTO users (id, username, password, email, role_id) VALUES (1, 'admin', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xdq17LkRw0uEcSgy', 'admin@pixay.com', 1);
INSERT INTO users (id, username, password, email, role_id) VALUES (2, 'juan',  '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xdq17LkRw0uEcSgy', 'juan@correo.com', 2);
INSERT INTO users (id, username, password, email, role_id) VALUES (3, 'maria', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xdq17LkRw0uEcSgy', 'maria@correo.com', 2);
INSERT INTO users (id, username, password, email, role_id) VALUES (4, 'julia', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xdq17LkRw0uEcSgy', 'julia@correo.com', 2);
--
    --TODO volver a crear tablas de catgories y subcategories
-- -- 3. CATEGORÍAS (Asegúrate de que la entidad se llame Category y la tabla categories)
-- INSERT INTO categories (id, name) VALUES (1, 'Ilustración');
-- INSERT INTO categories (id, name) VALUES (2, 'Fotografía');
-- --
-- -- -- 4. SUBCATEGORÍAS
-- INSERT INTO subcategories (id, name, category) VALUES (1, 'Retrato', 'Fotografía');
-- INSERT INTO subcategories (id, name, category) VALUES (2, 'Paisaje', 'Fotografía');
-- INSERT INTO subcategories (id, name, category) VALUES (3, 'Fotografía urbana', 'Fotografía');
-- INSERT INTO subcategories (id, name, category) VALUES (7, 'Arte conceptual', 'Ilustración');
-- INSERT INTO subcategories (id, name, category) VALUES (8, 'Pixel Art', 'Ilustración');