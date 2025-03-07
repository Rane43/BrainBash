package com.prometheus.brainbash.selenium;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.github.bonigarcia.wdm.WebDriverManager;

/*
 * Steps Definition of Selenium Tests for acceptance criteria for US1 - Login
 */
public class LoginSeleniumStepsTest extends CucumberSeleniumConfiguration {
	
	private static final String LOGIN_PAGE_URL = "http://localhost:8081/";
	
	private static WebDriver driver;
    private static WebDriverWait wait;
	
	@BeforeAll
	public static void setupAll() {
		 WebDriverManager.chromedriver().setup();
	     driver = new ChromeDriver();
	     wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // 20 second timeout
	}
	
	@AfterAll
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
	
	@And("I enter a valid email {string}")
	public void i_enter_a_valid_email(String email) {
	    
	}

	@And("I enter a valid password {string}")
	public void i_enter_a_valid_password(String password) {
	    
	}

	
	@When("I click the login button")
	public void i_click_the_login_button() {
		
	}
	
	
	@Then("I am redirected to the homepage")
	public void i_am_redirected_to_the_homepage() {
		
	}
	
	@And("my avatar appears in the top right to indicate I’m logged in")
	public void my_avatar_appears() {
		
	}

}
