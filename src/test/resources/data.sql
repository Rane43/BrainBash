--- 'TestPassword123!' is unhashed password always ---
--- usernames MUST be inserted in lowercase to present case-insensitivity in code ---

--- Test Admin ---
INSERT INTO users (username, password) VALUES ('testadmin', '$2a$10$y9PNqnQSadWdfKuKQVbAzuYo6es3Nmnpp8R.WDtt8Qp9v9zNm.a..');

INSERT INTO user_roles (user_id, role) 
VALUES ((SELECT id FROM users WHERE username = 'testadmin'), 'ROLE_ADMIN');

--- Test Quizzer ---
INSERT INTO users (username, password) VALUES ('testquizzer', '$2a$10$y9PNqnQSadWdfKuKQVbAzuYo6es3Nmnpp8R.WDtt8Qp9v9zNm.a..');

INSERT INTO user_roles (user_id, role) 
VALUES ((SELECT id FROM users WHERE username = 'testquizzer'), 'ROLE_QUIZZER');

--- Test Quiz Designer ---
INSERT INTO users (username, password) VALUES ('testquizdesigner', '$2a$10$y9PNqnQSadWdfKuKQVbAzuYo6es3Nmnpp8R.WDtt8Qp9v9zNm.a..');

INSERT INTO user_roles (user_id, role) 
VALUES ((SELECT id FROM users WHERE username = 'testquizdesigner'), 'ROLE_QUIZ_DESIGNER');