
-- ==========================================
-- INITIAL DATA FOR USERS, IMAGES AND RELATIONS
-- ==========================================

-- ---- USERS ----
-- Password = "1234" (BCrypt-hashed)
-- You can generate new hashes with new BCryptPasswordEncoder().encode("1234")

INSERT INTO users (username, password, email) VALUES
                                        ('cris',  '$2a$10$sE.gGqPd76mGsgz9VUH1.OzX2k8cAGtQtUGvmUNw7dTXHDnI5rA2K', 'cristina.oue@gmail.com');
                                        ('doris', '$2a$10$sE.gGqPd76mGsgz9VUH1.OzX2k8cAGtQtUGvmUNw7dTXHDnI5rA2K', 'doris@gmail.com'),
                                        ('daniel',  '$2a$10$sE.gGqPd76mGsgz9VUH1.OzX2k8cAGtQtUGvmUNw7dTXHDnI5rA2K', 'daniel@gmail.com'),

--INSERT INTO images (content, user_id) VALUES

-- ---- USERS_IMAGES ----
-- Because IDs are auto-generated, we must reference them dynamically.
-- H2 supports subqueries in INSERT for this purpose.

INSERT INTO users_images (user_id, user_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'cris' AND r.name = 'ROLE_ADMIN';
