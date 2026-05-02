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
INSERT INTO categories (id, name) VALUES (1, 'Fotografía');
INSERT INTO categories (id, name) VALUES (2, 'Ilustración');

--  4. SUBCATEGORÍAS
-- FOTOGRAFÍA (category_id = 1)
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('Arquitectura', 1);       --1
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('Blanco y Negro', 1);     --2
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('Cine', 1);               --3
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('Fotografía Urbana', 1);  --4
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('IA', 1);                 --5
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('Macro', 1);              --6
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('Meme', 1);               --7
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('Moda', 1);               --8
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('Naturaleza', 1);         --9
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('Paisaje', 1);            --10
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('Retrato', 1);            --11
-- ILUSTRACIÓN (category_id = 2)
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('Acuarela Digital', 2);   --1
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('Arte Conceptual', 2);    --2
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('Boceto', 2);             --3
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('Bodegón', 2);            --4
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('Caricatura', 2);         --5
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('Cómic / Manga', 2);      --6
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('Fan Art', 2);            --7
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('IA', 2);                 --8
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('Ilustración Digital', 2);--9
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('Minimalismo', 2);        --10
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('Naturaleza', 2);         --11
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('Paisaje', 2);            --12
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('Pixel Art', 2);          --13
MERGE INTO subcategories (name, category_id) KEY(name, category_id) VALUES ('Retrato', 2);            --14

--IMÁGENES DE MUESTRA
INSERT INTO users_images (title, user_id, category_id, subcategory_id, content)
VALUES ('Angel Beats', 1, 2,6, FILE_READ('src/main/resources/static/inicio/angel-beats.jpg'));
INSERT INTO users_images (title, user_id, category_id, subcategory_id, content)
VALUES ('Criss & Cross', 1, 1, 11,FILE_READ('src/main/resources/static/inicio/CrissCross.png'));
INSERT INTO users_images (title, user_id, category_id, subcategory_id, content)
VALUES ('Criss&Cross', 1, 1, 11, FILE_READ('src/main/resources/static/inicio/CrissCross2.jpg'));
INSERT INTO users_images (title, user_id, category_id, subcategory_id, content)
VALUES ('Darker than black', 1, 2, 6, FILE_READ('src/main/resources/static/inicio/darker-than-black.jpg'));
INSERT INTO users_images (title, user_id, category_id, subcategory_id, content)
VALUES ('Escarabajo', 1, 2, 11,  FILE_READ('src/main/resources/static/inicio/Escarabajo.png'));
INSERT INTO users_images (title, user_id, category_id, subcategory_id, content)
VALUES ('Garden for you', 1, 2, 11, FILE_READ('src/main/resources/static/inicio/garden4u.png'));
INSERT INTO users_images (title, user_id, category_id, subcategory_id, content)
VALUES ('Life is Strange', 1, 2, 7, FILE_READ('src/main/resources/static/inicio/lis.jpg'));
INSERT INTO users_images (title, user_id, category_id, subcategory_id,content)
VALUES ('Life is Strange: True Colors', 1, 2, 7, FILE_READ('src/main/resources/static/inicio/lis-true-colors.jpg'));
INSERT INTO users_images (title, user_id, category_id, subcategory_id, content)
VALUES ('Maga', 1, 1, 5, FILE_READ('src/main/resources/static/inicio/maga.PNG'));
INSERT INTO users_images (title, user_id, category_id, subcategory_id, content)
VALUES ('Mariquita', 1, 1, 9, FILE_READ('src/main/resources/static/inicio/mariquita.jpg'));
INSERT INTO users_images (title, user_id, category_id, subcategory_id, content)
VALUES ('Meme', 1, 1, 7, FILE_READ('src/main/resources/static/inicio/meme.jpeg'));
INSERT INTO users_images (title, user_id, category_id, subcategory_id, content)
VALUES ('Monstera', 1, 2, 3, FILE_READ('src/main/resources/static/inicio/monstera.jpg'));
INSERT INTO users_images (title, user_id, category_id, subcategory_id, content)
VALUES ('Pelirroja', 1, 1, 11, FILE_READ('src/main/resources/static/inicio/Pelirroja.PNG'));
INSERT INTO users_images (title, user_id, category_id, subcategory_id, content)
VALUES ('Pixel art', 1, 2, 13, FILE_READ('src/main/resources/static/inicio/pixel-art.jpeg'));
INSERT INTO users_images (title, user_id, category_id, subcategory_id, content)
VALUES ('Zoro Roronoa', 1, 2, 6, FILE_READ('src/main/resources/static/inicio/Zoro-Roronoa.png'));
INSERT INTO users_images (title, user_id, category_id, subcategory_id, content)
VALUES ('Bleach', 2, 2, 6, FILE_READ('src/main/resources/static/inicio/Bleach_gang.jpg'));
INSERT INTO users_images (title, user_id, category_id, subcategory_id, content)
VALUES ('Bleach', 2, 2, 6, FILE_READ('src/main/resources/static/inicio/bleach_rukia_ichigo.jpg'));
INSERT INTO users_images (title, user_id, category_id, subcategory_id, content)
VALUES ('Ceruledge', 2, 2, 6, FILE_READ('src/main/resources/static/inicio/Ceruledge.jpg'));
INSERT INTO users_images (title, user_id, category_id, subcategory_id, content)
VALUES ('Dragonair', 2, 2, 6, FILE_READ('src/main/resources/static/inicio/Dragonair.jpg'));
INSERT INTO users_images (title, user_id, category_id, subcategory_id, content)
VALUES ('Hoenn', 2, 2, 6, FILE_READ('src/main/resources/static/inicio/Hoenn.jpg'));
INSERT INTO users_images (title, user_id, category_id, subcategory_id, content)
VALUES ('Vegeta', 2, 2, 6,FILE_READ('src/main/resources/static/inicio/Majin_Vegeta.jpg'));
INSERT INTO users_images (title, user_id, category_id, subcategory_id, content)
VALUES ('Mark Grayson', 2, 2,6, FILE_READ('src/main/resources/static/inicio/mark_grayson.jpg'));
INSERT INTO users_images (title, user_id, category_id, subcategory_id, content)
VALUES ('Power Ultrakill', 2, 2, 6, FILE_READ('src/main/resources/static/inicio/Power_Ultrakill.jpg'));
INSERT INTO users_images (title, user_id, category_id, subcategory_id, content)
VALUES ('Scizor', 2, 2, 6, FILE_READ('src/main/resources/static/inicio/Scizor.jpg'));
INSERT INTO users_images (title, user_id, category_id, subcategory_id, content)
VALUES ('Totoro hora de aventuras', 2, 2, 6, FILE_READ('src/main/resources/static/inicio/Totoro_hora_aventuras.jpg'));


--IMÁGENES GUARDADAS POR LOS USUARIOS
INSERT INTO saved_images (user_id, image_id) VALUES (3, 1);
INSERT INTO saved_images (user_id, image_id) VALUES (3, 2);
INSERT INTO saved_images (user_id, image_id) VALUES (3, 3);
INSERT INTO saved_images (user_id, image_id) VALUES (3, 4);
INSERT INTO saved_images (user_id, image_id) VALUES (1, 16);
INSERT INTO saved_images (user_id, image_id) VALUES (1, 17);
INSERT INTO saved_images (user_id, image_id) VALUES (1, 18);
INSERT INTO saved_images (user_id, image_id) VALUES (1, 19);
INSERT INTO saved_images (user_id, image_id) VALUES (2, 1);
INSERT INTO saved_images (user_id, image_id) VALUES (2, 2);
INSERT INTO saved_images (user_id, image_id) VALUES (2, 3);
INSERT INTO saved_images (user_id, image_id) VALUES (2, 4);
INSERT INTO saved_images (user_id, image_id) VALUES (2, 5);
INSERT INTO saved_images (user_id, image_id) VALUES (2, 6);
INSERT INTO saved_images (user_id, image_id) VALUES (2, 7);


