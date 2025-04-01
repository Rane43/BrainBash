package com.prometheus.brainbash.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.function.Function;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.prometheus.brainbash.model.Role;
import com.prometheus.brainbash.test_helper.DatabaseManager;
import com.prometheus.brainbash.test_helper.LoginHelper;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class QuizPlaySeleniumTest {
	private static final String HOMEPAGE_URL = "http://localhost:8082/#quizzer-dashboard";
	private static final String QUIZ_ID = "1";
	private static final String PLAY_BTN_ID = "play-btn";
	private static final String NEXT_BTN_ID = "next-btn";
	private static final String FINISH_BTN_ID = "finish-btn";
	private static final String FINAL_RESULT_ID = "FinalResult";
	
	// DRIVER
	private WebDriver driver;
    private WebDriverWait wait;
    
    private LoginHelper loginHelper;
	
    @Autowired
    private DatabaseManager databaseManager;
    
	@BeforeAll
	void setupAll() {
		databaseManager.executeSetupScripts();
		WebDriverManager.chromedriver().setup();
		
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        
	    driver = new ChromeDriver(options);
	    wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	    
	    loginHelper = new LoginHelper(driver);
	}
	
	@AfterAll
	void teardownAll() {
		if (driver != null) {
			driver.quit();
		}
		databaseManager.clearDatabase();
	}
	
	// ----------- SUCCESSFULLY PLAY A GAME -------------
	@Test
	void successfullyPlayAGame() throws InterruptedException {
		// Given I am logged in as a quizzer
		loginHelper.loginAs(Role.ROLE_QUIZZER);

		// And I am on the homepage
		wait.until((Function<WebDriver, Boolean>) webDriver -> webDriver.getCurrentUrl().equals(HOMEPAGE_URL));
		
		// When I click play on a quiz card
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(QUIZ_ID))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(PLAY_BTN_ID))).click();
		
		// And I answer all the questions (either right or wrong)
		// Wait until the button containing the text "Berlin" is visible, then click it
		wait.until(ExpectedConditions.visibilityOfElementLocated(
		    By.xpath("//button[contains(text(), 'Berlin') or contains(text(), '7')]")
		)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(NEXT_BTN_ID))).click();
		
		Thread.sleep(2000); // 2-second wait for next page to load
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(
		    By.xpath("//button[contains(text(), 'Berlin') or contains(text(), '7')]")
		)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(FINISH_BTN_ID))).click();
		
		// Then a message appears detailing my best score so far and my score for the game I just played.
		assertEquals("2!", wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(FINAL_RESULT_ID))).getText());
		
	}
	
}


