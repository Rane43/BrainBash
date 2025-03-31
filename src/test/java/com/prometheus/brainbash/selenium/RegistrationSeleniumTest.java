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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.prometheus.brainbash.model.Role;
import com.prometheus.brainbash.test_helper.DatabaseManager;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class RegistrationSeleniumTest {
	// URLs
	private static final String REGISTRATION_PAGE_URL = "http://localhost:8082/";
	private static final String HOMEPAGE_URL = "http://localhost:8082/#quizzer-dashboard";
	
	// ELEMENT IDs
	private static final String REGISTRATION_LINK_ID = "link-to-register";
	private static final String USERNAME_FIELD_ID = "register-username";
	private static final String PASSWORD_FIELD_ID = "register-password";
	private static final String ROLE_DROPDOWN_ID = "register-role";
	private static final String REGISTER_BTN_ID = "register-btn";
	private static final String ERROR_MESSAGE_ID = "register-error";
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
	    driver = new ChromeDriver();
	    wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // 20 second timeout
	}
	
	@AfterAll
	public void teardownAll() {
		if (driver != null) {
			driver.quit();
		}
		databaseManager.clearDatabase();
	}
	
	// ------------- SUCCESSFUL REGISTER AS QUIZZER ----------------
	@Test
	void successfulLogin() {
		// Given I am on the registration page
		driver.get(REGISTRATION_PAGE_URL);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(REGISTRATION_LINK_ID))).click();
		
		// And I enter a non-taken username
		final String username = "newQuizzer";
		WebElement usernameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(USERNAME_FIELD_ID)));
	    usernameElement.sendKeys(username);
	    
	    // And I enter a valid password
	    final String password = "TestPassword123!";
	    WebElement passwordElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(PASSWORD_FIELD_ID)));
	    passwordElement.sendKeys(password);
	    
	    // And I choose my user role
	    final Role role = Role.ROLE_QUIZZER;
	    Select roleDropdown = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ROLE_DROPDOWN_ID))));
	    roleDropdown.selectByValue(role.toString());
	    
	    // And I click the register button
	    WebElement registerButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(REGISTER_BTN_ID)));
	    registerButton.click();
		
		// Then I am redirected to the homepage
		wait.until((Function<WebDriver, Boolean>) driver -> driver.getCurrentUrl().equals(HOMEPAGE_URL));
		assertEquals(HOMEPAGE_URL, driver.getCurrentUrl(), "Test quizzer should have been redirected to the homepage");
		
		// And my avatar appears in the top right to indicate I’m logged in
	    WebElement userIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(USER_ICON_ID)));
	    assertTrue(userIcon.isDisplayed(), "User icon should be visible but is not.");
	}

	
	// ------------- UNSUCCESSFUL REGISTRATION ----------------
	@Test
	void unsuccessfulRegistration() {
		// Given I am on the registration page
		driver.get(REGISTRATION_PAGE_URL);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(REGISTRATION_LINK_ID))).click();
		
		// And I enter a non-taken username
		final String username = "testQuizzer";
		WebElement usernameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(USERNAME_FIELD_ID)));
	    usernameElement.sendKeys(username);
	    
	    // And I enter a valid password
	    final String password = "TestPassword123!";
	    WebElement passwordElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(PASSWORD_FIELD_ID)));
	    passwordElement.sendKeys(password);
	    
	    // And I choose my user role
	    final Role role = Role.ROLE_QUIZZER;
	    Select roleDropdown = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ROLE_DROPDOWN_ID))));
	    roleDropdown.selectByValue(role.toString());
	    
	    // And I click the register button
	    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(REGISTER_BTN_ID))).click();
		
		// Then a message appears saying username has been taken
	    String errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ERROR_MESSAGE_ID))).getText();
		assertEquals("Username is taken.", errorMessage);
	}
}
