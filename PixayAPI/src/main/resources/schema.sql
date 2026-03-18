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
    content LONGBLOB NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
)