INSERT INTO roles (name) VALUES ( 'ROLE_ADMIN');
INSERT INTO roles (name) VALUES ('ROLE_ARTIST');

INSERT INTO users (id, username, password, email, role_id) VALUES
                                                               (1, 'admin', 'admin123', 'admin@pixay.com', 1),
                                                               (2, 'juan', '1234', 'juan@correo.com', 2),
                                                               (3, 'maria', '1234', 'maria@correo.com', 2),
                                                               (4, 'julia', '1234', 'julia@correo.com', 2);

ALTER TABLE users ALTER COLUMN id RESTART WITH 100;