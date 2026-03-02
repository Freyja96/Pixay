INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('CLIENT');
INSERT INTO roles (name) VALUES ('DELIVERY');

INSERT INTO users (id, username, password, full_name, email, role_id) VALUES
                                                               (1, 'admin', 'admin123', 'Admin Root', 'admin@foodexpress.com', 1),
                                                               (2, 'juan', '1234', 'Juan Pérez', 'juan@correo.com', 2),
                                                               (3, 'maria', '1234', 'María López', 'maria@correo.com', 2),
                                                               (4, 'repa1', '1234', 'Repartidor Uno', 'repa1@correo.com', 3);

INSERT INTO restaurants (id, name, address, phone) VALUES
                                                                (1, 'Burger Planet', 'Calle Luna 45', '600111222'),
                                                                (2, 'Pasta Nova', 'Av. Italia 12', '600222333'),
                                                                (3, 'Sushi Go', 'Calle Japón 3', '600333444');

INSERT INTO dishes (id, name, price, category, restaurant_id) VALUES
                                                                (1, 'Cheeseburger', 8.50, 'Hamburguesas', 1),
                                                                (2, 'Doble Bacon', 10.90, 'Hamburguesas', 1),
                                                                (3, 'Veggie Burger', 9.20, 'Hamburguesas', 1),
                                                                (4, 'Spaghetti Carbonara', 11.50, 'Pasta', 2),
                                                                (5, 'Lasagna Bolognesa', 12.00, 'Pasta', 2),
                                                                (6, 'Fetuccine Alfredo', 10.75, 'Pasta', 2),
                                                                (7, 'Sushi Maki', 13.50, 'Sushi', 3),
                                                                (8, 'Nigiri Salmón', 12.90, 'Sushi', 3),
                                                                (9, 'Tempura', 9.80, 'Entrante', 3),
                                                                (10, 'Patatas Deluxe', 4.50, 'Entrante', 1),
                                                                (11, 'Tiramisú', 5.90, 'Postre', 2),
                                                                (12, 'Helado Matcha', 6.20, 'Postre', 3),
                                                                (13, 'Onigiri', 7.80, 'Entrante', 3),
                                                                (14, 'Tagliatelle Pesto', 10.20, 'Pasta', 2),
                                                                (15, 'Chicken Burger', 9.80, 'Hamburguesas', 1);

INSERT INTO orders (id, order_date, status, user_id, restaurant_id) VALUES
                                                                (1, '2025-10-01 12:00:00', 'ENTREGADO', 2, 1),
                                                                (2, '2025-10-02 13:00:00', 'PREPARANDO', 3, 2),
                                                                (3, '2025-10-03 20:15:00', 'ENTREGADO', 2, 3);

INSERT INTO order_details (order_id, dish_id, quantity, price) VALUES (1, 1, 2, 17.00);
INSERT INTO order_details (order_id, dish_id, quantity, price) VALUES (1, 10, 1, 4.50);
INSERT INTO order_details (order_id, dish_id, quantity, price) VALUES (2, 4, 1, 11.50);
INSERT INTO order_details (order_id, dish_id, quantity, price) VALUES (2, 5, 1, 12.00);
INSERT INTO order_details (order_id, dish_id, quantity, price) VALUES (3, 7, 1, 13.50);
INSERT INTO order_details (order_id, dish_id, quantity, price) VALUES (3, 8, 1, 12.90);
INSERT INTO order_details (order_id, dish_id, quantity, price) VALUES (3, 12, 1, 6.20);

ALTER TABLE users ALTER COLUMN id RESTART WITH 100;
ALTER TABLE restaurants ALTER COLUMN id RESTART WITH 100;
ALTER TABLE dishes ALTER COLUMN id RESTART WITH 100;
ALTER TABLE orders ALTER COLUMN id RESTART WITH 100;