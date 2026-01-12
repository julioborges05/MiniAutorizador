CREATE TABLE users (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         login VARCHAR(50) NOT NULL UNIQUE,
                         password VARCHAR(255) NOT NULL,
                         enabled BOOLEAN NOT NULL DEFAULT TRUE
);

-- senha: password
INSERT INTO users (name, login, password, enabled)
VALUES ('admin', 'username', '$2a$10$IlJYiv3TFKAFRnHNDrFdaewLYukJuPF7ARO3h/QNjAMxbXmg31vo6', TRUE);
