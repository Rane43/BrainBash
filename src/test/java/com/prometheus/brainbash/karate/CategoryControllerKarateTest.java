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
class CategoryControllerKarateTest {
	private static final String FOLDER = "classpath:features/karate/category_controller/";
	
	@Autowired
	private DatabaseManager databaseManager;
	
	@BeforeAll
	void setupAll() {
		databaseManager.executeUserSetupScript();
	}
	
	@Karate.Test
	Karate runAllCategoryControllerKarateTests() {
		return Karate.run(FOLDER + "get_all_categories.feature");
	}

	@AfterAll
	void teardownAll() {
		databaseManager.clearDatabase();
	}
}
