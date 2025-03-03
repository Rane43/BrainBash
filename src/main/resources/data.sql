--- 'Password123!' is unhashed password always ---

--- Development Admin ---
INSERT INTO users (username, password) VALUES ('admin', '$2a$10$F9KzBJhx/QVIxlruB7TtoeaatybKcZOYKXJH4nbsKwJAhkKqTGfUG');

INSERT INTO user_roles (user_id, role) 
VALUES ((SELECT id FROM users WHERE username = 'admin'), 'ROLE_ADMIN');