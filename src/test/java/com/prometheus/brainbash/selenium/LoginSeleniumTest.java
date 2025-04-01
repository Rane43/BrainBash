package com.prometheus.brainbash.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.prometheus.brainbash.test_helper.DatabaseManager;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class LoginSeleniumTest {
	// URLs
	private static final String LOGIN_PAGE_URL = "http://localhost:8082/";
	private static final String HOMEPAGE_URL = "http://localhost:8082/#quizzer-dashboard";
	
	// ELEMENT IDs
	private static final String USERNAME_FIELD_ID = "username";
	private static final String PASSWORD_FIELD_ID = "password";
	private static final String LOGIN_BTN_ID = "login-btn";
	private static final String ERROR_MESSAGE_ID = "login-error";
	private static final String USER_ICON_ID = "user-icon";
	
	// DRIVER
	private static WebDriver driver;
    private static WebDriverWait wait;
    
    @Autowired
    private DatabaseManager databaseManager;
    
    @BeforeAll
	public void setupAll() {
		databaseManager.executeSetupScripts();
		
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("user-data-dir=/tmp/chrom-user-data");
		options.addArguments("--headless");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        
	    driver = new ChromeDriver(options);
	    wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // 20 second timeout
	}
	
	@AfterAll
	public void teardownAll() {
		if (driver != null) {
			driver.quit();
		}
		databaseManager.clearDatabase();
	}
	
	// ------------- SUCCESSFUL LOGIN ----------------
	@Test
	void successfulLogin() {
		// Given I am on the login page
		driver.get(LOGIN_PAGE_URL);
		
		// And I enter a valid username
		final String username = "testQuizzer";
		WebElement usernameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(USERNAME_FIELD_ID)));
	    usernameElement.sendKeys(username);
	    
	    // And I enter a valid password
	    final String password = "TestPassword123!";
	    WebElement passwordElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(PASSWORD_FIELD_ID)));
	    passwordElement.sendKeys(password);
	    
	    // And I click the login button
	    WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LOGIN_BTN_ID)));
		loginButton.click();
		
		// Then I am redirected to the homepage
		wait.until((Function<WebDriver, Boolean>) driver -> driver.getCurrentUrl().equals(HOMEPAGE_URL));
		assertEquals(HOMEPAGE_URL, driver.getCurrentUrl(), "Test quizzer should have been redirected to the homepage");
		
		// And my avatar appears in the top right to indicate I’m logged in
	    WebElement userIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(USER_ICON_ID)));
	    assertTrue(userIcon.isDisplayed(), "User icon should be visible but is not.");
	}

	
	// ------------- UNSUCCESSFUL LOGIN ----------------
	@Test
	void unsuccessfulLogin() {
		// Given I am on the login page
		driver.get(LOGIN_PAGE_URL);
		
		// And I enter a invalid username
		final String username = "invalidUsername";
		WebElement usernameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(USERNAME_FIELD_ID)));
	    usernameElement.sendKeys(username);
	    
	    // And I enter a invalid password
	    final String password = "invalidPassword123!";
	    WebElement passwordElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(PASSWORD_FIELD_ID)));
	    passwordElement.sendKeys(password);
	    
	    // And I click the login button
	    WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LOGIN_BTN_ID)));
		loginButton.click();
		
		// Then an error message appears.
		final String errorMessage = "Invalid Username or Password";
		WebElement errorMessageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ERROR_MESSAGE_ID)));
		assertEquals(errorMessage, errorMessageElement.getText());
	}
}


