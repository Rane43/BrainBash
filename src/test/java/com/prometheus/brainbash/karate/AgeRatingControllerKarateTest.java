package com.prometheus.brainbash.karate;

import org.springframework.boot.test.context.SpringBootTest;

import com.intuit.karate.junit5.Karate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AgeRatingControllerKarateTest {
	private static final String FOLDER = "classpath:features/karate/age_ratings/";
	
	@Karate.Test
	Karate runAllAgeRatingControllerTests() {
		return Karate.run(FOLDER + "get_all_age_ratings.feature");
	}
}
