package com.prometheus.brainbash.karate;

import org.springframework.boot.test.context.SpringBootTest;

import com.intuit.karate.junit5.Karate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ImageControllerKarateTest {
	private static final String FOLDER = "classpath:features/karate/image_controller/";
	
	@Karate.Test
	Karate runAllImageControllerKarateTests() {
		return Karate.run(FOLDER + "get_all_images.feature");
	}
}
