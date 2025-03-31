INSERT INTO quizzes VALUES (3,1,'Easy Teen Geography Quiz 1','landscape1.webp','Geography Quiz 1','TEEN','GEOGRAPHY','EASY');
INSERT INTO quizzes VALUES (3,2,'Easy Teen Geography Quiz 2','background-image.webp','Geography Quiz 2','TEEN','GEOGRAPHY','EASY');
INSERT INTO quizzes VALUES (3,3,'Easy Children''s Geography Quiz 3','landscape1.webp','Geography Quiz 3','CHILDREN','GEOGRAPHY','EASY');
INSERT INTO quizzes VALUES (3,4,'Easy adult quiz','background-image.webp','Geography Quiz 4','ADULT','GEOGRAPHY','EASY');
INSERT INTO quizzes VALUES (3,5,'Medium difficulty adult quiz','landscape1.webp','Geography Quiz 5','ADULT','GEOGRAPHY','MEDIUM');
INSERT INTO quizzes VALUES (3,6,'Hard adult quiz','background-image.webp','Geography Quiz 6','ADULT','GEOGRAPHY','HARD');
INSERT INTO quizzes VALUES (3,7,'Extremely difficult adult quiz','landscape1.webp','Geography Quiz 7','ADULT','GEOGRAPHY','EXTREME');
INSERT INTO quizzes VALUES (3,8,'Geography Description 8','background-image.webp','Geography Quiz 8','ADULT','GEOGRAPHY','MEDIUM');
INSERT INTO quizzes VALUES (3,9,'Anatomy Description 1','background-image.webp','Anatomy Quiz 1','ADULT','ANATOMY','MEDIUM');
INSERT INTO quizzes VALUES (3,10,'History Description 1','landscape1.webp','History Quiz 1','ADULT','HISTORY','MEDIUM');
INSERT INTO quizzes VALUES (3,11,'History Description 2','background-image.webp','History Quiz 2','ADULT','HISTORY','MEDIUM');
INSERT INTO quizzes VALUES (3,12,'History Description 3','landscape1.webp','History Quiz 3','ADULT','HISTORY','MEDIUM');
INSERT INTO quizzes VALUES (3,13,'History Description 4','background-image.webp','History Quiz 4','ADULT','HISTORY','MEDIUM');
INSERT INTO quizzes VALUES (3,14,'History Description 5','landscape1.webp','History Quiz 5','ADULT','HISTORY','MEDIUM');
INSERT INTO quizzes VALUES (6,15,'New Quiz Just for testing','landscape2.webp','New Quiz','CHILDREN','SCIENCE','EASY');

ALTER TABLE quizzes ALTER COLUMN id RESTART WITH 16;