package com.prometheus.brainbash.karate;

import org.springframework.boot.test.context.SpringBootTest;

import com.intuit.karate.junit5.Karate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class DifficultyRatingControllerKarateTest {
	private static final String FOLDER = "classpath:features/karate/difficulty_rating_controller/";
	
	@Karate.Test
	Karate runAllAgeRatingControllerTests() {
		return Karate.run(FOLDER + "get_all_difficulty_ratings.feature");
	}
}
