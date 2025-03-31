package com.prometheus.brainbash.karate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.intuit.karate.junit5.Karate;
import com.prometheus.brainbash.test_helper.DatabaseManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class QuizControllerKarateTest {
	private static final String FOLDER = "classpath:features/karate/quiz_controller/";
	
	@Autowired
	private DatabaseManager databaseManager;
	
	@BeforeAll
	void setupAll() {
		databaseManager.executeSetupScripts();
	}
	
	@Karate.Test
	Karate runAllPointsControllerKarateTests() {
		return Karate.run(
				FOLDER + "get_quiz_by_id.feature",
				FOLDER + "get_my_quizzes_by_filters.feature",
				FOLDER + "get_all_quizzes_by_filters.feature",
				FOLDER + "create_quiz.feature",
				FOLDER + "get_question_by_id.feature",
				FOLDER + "create_question.feature",
				FOLDER + "update_question.feature",
				FOLDER + "delete_quiz.feature"
		);
	}
	
	@AfterAll
	void teardownAll() {
		databaseManager.clearDatabase();
	}
}
