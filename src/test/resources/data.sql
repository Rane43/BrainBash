--- 'TestPassword123!' is unhashed password always ---
--- usernames MUST be inserted in lowercase to present case-insensitivity in code ---

--- Test Admin ---
INSERT INTO users (username, password, role) VALUES ('testadmin', '$2a$10$y9PNqnQSadWdfKuKQVbAzuYo6es3Nmnpp8R.WDtt8Qp9v9zNm.a..', 'ROLE_ADMIN');

--- Test Quizzer ---
INSERT INTO users (username, password, role) VALUES ('testquizzer', '$2a$10$y9PNqnQSadWdfKuKQVbAzuYo6es3Nmnpp8R.WDtt8Qp9v9zNm.a..', 'ROLE_QUIZZER');

--- Test Quiz Designer ---
INSERT INTO users (username, password, role) VALUES ('testquizdesigner', '$2a$10$y9PNqnQSadWdfKuKQVbAzuYo6es3Nmnpp8R.WDtt8Qp9v9zNm.a..', 'ROLE_QUIZ_DESIGNER');