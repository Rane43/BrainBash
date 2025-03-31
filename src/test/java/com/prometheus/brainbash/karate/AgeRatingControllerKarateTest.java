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
class AgeRatingControllerKarateTest {
	private static final String FOLDER = "classpath:features/karate/age_ratings/";
	

	@Autowired
	private DatabaseManager databaseManager;
	
	@BeforeAll
	void setupAll() {
		databaseManager.executeUserSetupScript();
	}
	
	@Karate.Test
	Karate runAllAgeRatingControllerTests() {
		return Karate.run(FOLDER + "get_all_age_ratings.feature");
	}
	
	@AfterAll
	void teardownAll() {
		databaseManager.clearDatabase();
	}
}
