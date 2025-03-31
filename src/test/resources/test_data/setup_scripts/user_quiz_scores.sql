INSERT INTO user_quiz_scores VALUES (1,1,1,1);
INSERT INTO user_quiz_scores VALUES (1,2,1,5);

ALTER TABLE user_quiz_scores ALTER COLUMN id RESTART WITH 3;