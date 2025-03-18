package com.prometheus.brainbash.controller;

import org.springframework.boot.test.context.SpringBootTest;

import com.intuit.karate.junit5.Karate;

/*
 * Karate tests for AuthController
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthControllerKarateTest {
	private static final String FOLDER = "classpath:features/karate/login";
	
	@Karate.Test
	Karate runLoginTests() {
		return Karate.run(FOLDER + "/login.feature").relativeTo(getClass());
	}
}
