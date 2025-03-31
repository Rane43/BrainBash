package com.prometheus.brainbash.selenium;

import java.time.Duration;
import java.util.function.Function;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
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
	
	// DRIVER
	private WebDriver driver;
    private WebDriverWait wait;
    
    private LoginHelper loginHelper;
	
    @Autowired
    private DatabaseManager databaseManager;
    
	@BeforeAll
	public void setupAll() {
		databaseManager.executeSetupScripts();
		WebDriverManager.chromedriver().setup();
	    driver = new ChromeDriver();
	    wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	    
	    loginHelper = new LoginHelper(driver);
	}
	
	@AfterAll
	public void teardownAll() {
		if (driver != null) {
			driver.quit();
		}
		databaseManager.clearDatabase();
	}
	
	// ----------- SUCCESSFULLY PLAY A GAME -------------
	@Test
	void successfullyPlayAGame() {
		// Given I am logged in as a quizzer
		loginHelper.loginAs(Role.ROLE_QUIZZER);

		// And I am on the homepage
		wait.until((Function<WebDriver, Boolean>) webDriver -> webDriver.getCurrentUrl().equals(HOMEPAGE_URL));
		
		// When I click play on a quiz card
		WebElement quizCard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("1")));
		quizCard.click();
		
		// And I answer all the questions (either right or wrong)

		
		// Then a message appears detailing my best score so far and my score for the game I just played.
		
	}
	
}