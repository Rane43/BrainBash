package com.prometheus.brainbash.filter;

import org.springframework.boot.test.context.SpringBootTest;

import com.intuit.karate.junit5.Karate;

/*
 * Karate tests for JwtAuthenticationFilter
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class JwtAuthenticationFilterKarateTest {
	private static final String FOLDER = "classpath:features/karate/jwt-auth";
	
	/*
	@Karate.Test
	Karate testNoUsernamePresent() {
		return Karate.run(FOLDER + "").relativeTo(getClass());
	}
	
	@Karate.Test
	Karate testValidToken() {
		return Karate.run(FOLDER + "").relativeTo(getClass());
	}
	
	@Karate.Test
	Karate testInvalidToken() {
		return Karate.run(FOLDER + "").relativeTo(getClass());
	}
	
	@Karate.Test
	Karate testUserDoesNotExist() {
		return Karate.run(FOLDER + "").relativeTo(getClass());
	}
	*/
}
