package com.prometheus.brainbash.karate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.intuit.karate.junit5.Karate;
import com.prometheus.brainbash.test_helper.DatabaseManager;

/*
 * Karate tests for Role-based access control
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class RoleBasedAccessKarateTest {
	private static final String FOLDER = "classpath:features/karate/jwt_auth/";
	
	@Autowired
	private DatabaseManager databaseManager;
	
	@BeforeAll
	void setupAll() {
		databaseManager.executeUserSetupScript();
	}
	
	@Karate.Test
	Karate testRoleBasedAccess() {
		return Karate.run(FOLDER + "authorization.feature").relativeTo(getClass());
	}
	
	@AfterAll
	void teardownAll() {
		databaseManager.clearDatabase();
	}

}