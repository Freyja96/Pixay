CREATE TABLE IF NOT EXISTS roles (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    username VARCHAR(50) NOT NULL UNIQUE,
                                    password VARCHAR(255) NOT NULL,
                                    email VARCHAR(100) NOT NULL,
                                    role_id BIGINT NOT NULL,
                                    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS subcategories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    category VARCHAR(50) NOT NULL,
    FOREIGN KEY (category) REFERENCES categories(name)
);
--tabla de imágenes!!
-- @Id
-- @GeneratedValue(strategy = GenerationType.IDENTITY)
-- private Long id;
--
-- private byte[] content; //archivo binario de UNA foto
--
-- @ManyToOne
-- @JoinColumn(name = "user_id")
-- private User user_id; //a quién pertenece
CREATE TABLE IF NOT EXISTS users_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100),
    content LONGBLOB NOT NULL,
    user_id BIGINT NOT NULL,
    category VARCHAR(50) NOT NULL,
    subcategory VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category) REFERENCES categories(name)
)