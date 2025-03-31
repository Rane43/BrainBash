package com.prometheus.brainbash.test_helper;

import java.time.Duration;
import java.util.EnumMap;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.prometheus.brainbash.dto.LoginDto;
import com.prometheus.brainbash.model.Role;

public class LoginHelper {
	// URLs
	private static final String LOGIN_PAGE_URL = "http://localhost:8082/#login";
	private static final String HOMEPAGE_URL = "http://localhost:8082/#quizzer-dashboard";
	
	// ELEMENT IDs
	private static final String USERNAME_FIELD_ID = "username";
	private static final String PASSWORD_FIELD_ID = "password";
	private static final String LOGIN_BTN_ID = "login-btn";
		
		
	private static final String BASE_URL = "http://localhost:8082";
	private final WebDriver driver;
	private final WebDriverWait wait;
	
	private EnumMap<Role, LoginDto> loginCredentials;
	
	public LoginHelper(WebDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    
		// ------------ LOGIN CREDENTIALS ----------
		loginCredentials = new EnumMap<>(Role.class);
		final String password = "TestPassword123!";
		
		// Quizzer Login
		LoginDto quizzerDto = new LoginDto("testQuizzer", password);
		loginCredentials.put(Role.ROLE_QUIZZER, quizzerDto);
		
		// Customer Service Login
		LoginDto quizDesignerDto = new LoginDto("testQuizDesigner", password);
		loginCredentials.put(Role.ROLE_QUIZ_DESIGNER, quizDesignerDto);
	}
	
	/*
	 * Selenium method to drive login
	 */
	public void loginAs(Role role) {
		final LoginDto loginDto = loginCredentials.get(role);
		final String email = loginDto.getUsername();
		final String password = loginDto.getPassword();
		
		driver.get(BASE_URL);
		
		WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(USERNAME_FIELD_ID)));
		WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(PASSWORD_FIELD_ID)));
		WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LOGIN_BTN_ID)));

        emailField.sendKeys(email);
        passwordField.sendKeys(password);
        loginButton.click();
        
        wait.until((Function<WebDriver, Boolean>) webDriver -> webDriver.getCurrentUrl().equals(HOMEPAGE_URL));
	}
	
}
