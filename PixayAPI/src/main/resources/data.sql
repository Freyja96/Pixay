MERGE INTO roles (id, name) KEY(id) VALUES (1, 'ROLE_ADMIN');
MERGE INTO roles (id, name) KEY(id) VALUES (2, 'ROLE_ARTIST');

MERGE INTO users (id, username, password, email, role_id) KEY(id) VALUES
                                                               (1, 'admin', 'admin123', 'admin@pixay.com', 1),
                                                               (2, 'juan', '1234', 'juan@correo.com', 2),
                                                               (3, 'maria', '1234', 'maria@correo.com', 2),
                                                               (4, 'julia', '1234', 'julia@correo.com', 2);