package com.prometheus.brainbash.controller;

import org.springframework.boot.test.context.SpringBootTest;

import com.intuit.karate.junit5.Karate;

/*
 * Karate tests for AuthController
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthControllerKarateTest {
	
	@Karate.Test
	Karate runAllTests() {
		return Karate.run("classpath:features/karate/login-admin.feature").relativeTo(getClass());
	}
}
