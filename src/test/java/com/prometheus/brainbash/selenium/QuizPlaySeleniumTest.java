package com.prometheus.brainbash.selenium;

import java.time.Duration;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.prometheus.brainbash.model.Role;
import com.prometheus.brainbash.test_helper.LoginHelper;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;

class QuizPlaySeleniumTest {
	private static final String HOMEPAGE_URL = "http://localhost:8082/#quizzer-dashboard";
	
	// DRIVER
	private WebDriver driver;
    private WebDriverWait wait;
    
    private LoginHelper loginHelper;
	
	@Before
	public void setupAll() {
		WebDriverManager.chromedriver().setup();
	    driver = new ChromeDriver();
	    wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	    
	    loginHelper = new LoginHelper(driver);
	}
	
	@After
	public void teardownAll() {
		if (driver != null) {
			driver.quit();
		}
	}
	

	@Given("I am logged in as a quizzer")
	public void i_am_logged_in_as_a_quizzer() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	
	@Given("I am logged in as a quizzer")
	void i_am_logged_in() {
		loginHelper.loginAs(Role.ROLE_QUIZZER);
	}
	
	@And("And I am on the homepage")
	void i_am_on_the_homepage() {
		wait.until((Function<WebDriver, Boolean>) webDriver -> webDriver.getCurrentUrl().equals(HOMEPAGE_URL));
	}
	
	@When("I click play on a quiz card") 
	void i_select_a_quiz_card() {
		WebElement quizCard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("1")));
		quizCard.click();
	}
	
	@And("I answer all the questions (either right or wrong")
	void i_answer_all_the_questions() {
		
	}
	
	@Then("a message appears detailing my best score so far and my score for the game I just played.")
	void a_message_appears_detailing_my_best_score() {
		
	}
	
}