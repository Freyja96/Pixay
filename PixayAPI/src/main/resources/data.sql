-- 1. ROLES
INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_ARTIST');

-- 2. USUARIOS
-- Contraseñas cifradas con BCrypt:
-- 'admin123' -> $2a$10$5Ovs1H/hoT48uwRdiAMiL.gnZISqmhtXuou7eLvNem0OfyQSAUsQK
INSERT INTO users (id, username, password, email, role_id) VALUES (1, 'admin', '$2a$10$5Ovs1H/hoT48uwRdiAMiL.gnZISqmhtXuou7eLvNem0OfyQSAUsQK', 'admin@pixay.com', 1);
INSERT INTO users (id, username, password, email, role_id) VALUES (2, 'juan', '$2a$10$5Ovs1H/hoT48uwRdiAMiL.gnZISqmhtXuou7eLvNem0OfyQSAUsQK', 'juan@pixay.com', 2);
INSERT INTO users (id, username, password, email, role_id) VALUES (3, 'maria', '$2a$10$5Ovs1H/hoT48uwRdiAMiL.gnZISqmhtXuou7eLvNem0OfyQSAUsQK', 'maria@pixay.com', 2);
INSERT INTO users (id, username, password, email, role_id) VALUES (4, 'julia', '$2a$10$5Ovs1H/hoT48uwRdiAMiL.gnZISqmhtXuou7eLvNem0OfyQSAUsQK','julia@correo.com', 2);

-- 3. CATEGORÍAS (Asegúrate de que la entidad se llame Category y la tabla categories)
INSERT INTO categories (id, name) VALUES (1, 'Ilustración');
INSERT INTO categories (id, name) VALUES (2, 'Fotografía');

--  4. SUBCATEGORÍAS
-- FOTOGRAFÍA (category_id = 1)
INSERT INTO subcategories (name, category_id) VALUES ('Retrato', 1);
INSERT INTO subcategories (name, category_id) VALUES ('Paisaje', 1);
INSERT INTO subcategories (name, category_id) VALUES ('Fotografía Urbana', 1);
INSERT INTO subcategories (name, category_id) VALUES ('Macro', 1);
INSERT INTO subcategories (name, category_id) VALUES ('Naturaleza Salvaje', 1);
INSERT INTO subcategories (name, category_id) VALUES ('Blanco y Negro', 1);
INSERT INTO subcategories (name, category_id) VALUES ('Arquitectura', 1);
-- ILUSTRACIÓN (category_id = 2)
INSERT INTO subcategories (name, category_id) VALUES ('Arte Conceptual', 2);
INSERT INTO subcategories (name, category_id) VALUES ('Pixel Art', 2);
INSERT INTO subcategories (name, category_id) VALUES ('Fan Art', 2);
INSERT INTO subcategories (name, category_id) VALUES ('Cómic / Manga', 2);
INSERT INTO subcategories (name, category_id) VALUES ('Ilustración Digital', 2);
INSERT INTO subcategories (name, category_id) VALUES ('Acuarela Digital', 2);
INSERT INTO subcategories (name, category_id) VALUES ('Minimalismo', 2);