--- 'TestPassword123!' is unhashed password always ---

--- Test Admin ---
INSERT INTO users (username, password) VALUES ('testAdmin', '$2a$10$y9PNqnQSadWdfKuKQVbAzuYo6es3Nmnpp8R.WDtt8Qp9v9zNm.a..');

INSERT INTO user_roles (user_id, role) 
VALUES ((SELECT id FROM users WHERE username = 'testAdmin'), 'ROLE_ADMIN');