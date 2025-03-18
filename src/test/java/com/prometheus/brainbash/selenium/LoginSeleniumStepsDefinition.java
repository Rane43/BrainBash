package com.prometheus.brainbash.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.github.bonigarcia.wdm.WebDriverManager;

/*
 * Steps Definition of Selenium Tests for acceptance criteria for US1 - Login
 */
public class LoginSeleniumStepsDefinition extends CucumberSeleniumConfiguration {
	
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
	
	@Before
	public static void setupAll() {
		 WebDriverManager.chromedriver().setup();
	     driver = new ChromeDriver();
	     wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // 20 second timeout
	}
	
	@After
	public static void teardownAll() {
		if (driver != null) {
			driver.quit();
		}
	}
	
	// ------------- SUCCESSFUL LOGIN ----------------
	@Given("I am on the login page")
	public void i_am_on_the_login_page() {
		driver.get(LOGIN_PAGE_URL);
	}
	
	@And("I enter a valid username {string}")
	public void i_enter_a_valid_email(String username) {
	    WebElement usernameElement = driver.findElement(By.id(USERNAME_FIELD_ID));
	    usernameElement.sendKeys(username);
	}

	@And("I enter a valid password {string}")
	public void i_enter_a_valid_password(String password) {
	    WebElement passwordElement = driver.findElement(By.id(PASSWORD_FIELD_ID));
	    passwordElement.sendKeys(password);
	    System.out.println(password);
	}

	
	@When("I click the login button")
	public void i_click_the_login_button() {
		WebElement loginButton = driver.findElement(By.id(LOGIN_BTN_ID));
		loginButton.click();
	}
	
	
	@Then("I am redirected to the homepage")
	public void i_am_redirected_to_the_homepage() {
		wait.until((Function<WebDriver, Boolean>) driver -> driver.getCurrentUrl().equals(HOMEPAGE_URL));
		assertEquals(HOMEPAGE_URL, driver.getCurrentUrl(), "Test quizzer should have been redirected to the homepage");
	}
	
	@And("my avatar appears in the top right to indicate I’m logged in")
	public void my_avatar_appears() {
	    WebElement userIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(USER_ICON_ID)));
	    assertTrue(userIcon.isDisplayed(), "User icon should be visible but is not.");
	}

	
	// ------------- UNSUCCESSFUL LOGIN ----------------
	@When("I enter an invalid username {string} or password {string}")
	public void i_enter_an_invalid_email_or_password(String username, String password) {
	    WebElement usernameField = driver.findElement(By.id(USERNAME_FIELD_ID));
	    WebElement passwordField = driver.findElement(By.id(PASSWORD_FIELD_ID));
	    
	    usernameField.sendKeys(username);
	    passwordField.sendKeys(password);
	}
	@Then("an error message {string} appears.")
	public void an_error_message_appears(String errorMessage) {
		WebElement errorMessageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ERROR_MESSAGE_ID)));
		assertEquals(errorMessage, errorMessageElement.getText());
	}


	
	
}
