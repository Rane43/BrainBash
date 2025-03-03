package com.prometheus.brainbash.controller;

import org.springframework.boot.test.context.SpringBootTest;

import com.intuit.karate.junit5.Karate;

/*
 * Karate tests for Role-based access control
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RoleBasedAccessKarateTest {
	private static final String FOLDER = "classpath:features/karate/jwt-auth";
	
	@Karate.Test
	Karate testRoleBasedAccess() {
		return Karate.run(FOLDER + "/authorization.feature").relativeTo(getClass());
	}

}