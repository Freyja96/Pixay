MERGE INTO roles (id, name) KEY(id) VALUES (1, 'ROLE_ADMIN');
MERGE INTO roles (id, name) KEY(id) VALUES (2, 'ROLE_ARTIST');

MERGE INTO users (id, username, password, email, role_id) KEY(id) VALUES
                                                               (1, 'admin', 'admin123', 'admin@pixay.com', 1),
                                                               (2, 'juan', '1234', 'juan@correo.com', 2),
                                                               (3, 'maria', '1234', 'maria@correo.com', 2),
                                                               (4, 'julia', '1234', 'julia@correo.com', 2);

-- Categorías Base
MERGE INTO categories (id, name) KEY(id) VALUES (1, 'Ilustración'),
                                                (2, 'Fotografía');

MERGE INTO subcategories (id, name, category) KEY(id) VALUES (1, 'Retrato', 'Fotografía'),
                                                             (2, 'Paisaje', 'Fotografía'),
                                                             (3, 'Fotografía urbana', 'Fotografía'),
                                                             (4, 'Macro', 'Fotografía'),
                                                             (5, 'Arquitectura', 'Fotografía'),
                                                             (6, 'Moda', 'Fotografía');

-- Subcategorías para 'Ilustración'
MERGE INTO subcategories (id, name, category) KEY(id) VALUES (7, 'Arte conceptual', 'Ilustración'),
                                                             (8, 'Pixel Art', 'Ilustración'),
                                                             (9, 'Fan Art', 'Ilustración'),
                                                             (10, 'Acuarela Digital', 'Ilustración'),
                                                             (11, 'Modelado 3D', 'Ilustración'),
                                                             (12, 'Digital Art', 'Ilustración');