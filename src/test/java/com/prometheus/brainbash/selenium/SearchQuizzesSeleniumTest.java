package com.prometheus.brainbash.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.prometheus.brainbash.dao.QuizRepository;
import com.prometheus.brainbash.model.AgeRating;
import com.prometheus.brainbash.model.DifficultyRating;
import com.prometheus.brainbash.model.Quiz;
import com.prometheus.brainbash.model.Role;
import com.prometheus.brainbash.test_helper.DatabaseManager;
import com.prometheus.brainbash.test_helper.LoginHelper;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class SearchQuizzesSeleniumTest {
	private static final String HOMEPAGE_URL = "http://localhost:8082/#quizzer-dashboard";
	
	private static final String SEARCH_BOX_ID = "search-bar";
	private static final String AGE_RATINGS_DROPDOWN_ID = "age-rating-dropdown";
	private static final String DIFFICULTY_RATINGS_DROPDOWN_ID = "difficulty-rating-dropdown";
	
	// DRIVER
	private WebDriver driver;
    private WebDriverWait wait;
    
    private LoginHelper loginHelper;
	
    @Autowired
    private DatabaseManager databaseManager;
    
    @Autowired
    private QuizRepository quizRepo;
    
	@BeforeAll
	public void setupAll() {
		databaseManager.executeSetupScripts();
		WebDriverManager.chromedriver().setup();
		
		ChromeOptions options = new ChromeOptions();
		String userDataDir = "/path/to/unique/directory";
		options.addArguments("user-data-dir=" + userDataDir);
		options.addArguments("--headless");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        
	    driver = new ChromeDriver(options);
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
	
	
	// ------------ SUCCESSFULLY SEARCH BY FILTERS -------------
	@Test
	void succesfullySearchByFilters() throws InterruptedException {
		// Given I am logged in as a quizzer
		loginHelper.loginAs(Role.ROLE_QUIZZER);

		// And I on the homepage
		wait.until((Function<WebDriver, Boolean>) webDriver -> webDriver.getCurrentUrl().equals(HOMEPAGE_URL));
		
		// When I enter search title
		final String searchText = "Geo";
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(SEARCH_BOX_ID))).sendKeys(searchText);
		
		// And I select the age rating
		final AgeRating ageRating = AgeRating.CHILDREN;
		Select ageRatingDropdown = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(AGE_RATINGS_DROPDOWN_ID))));
		ageRatingDropdown.selectByValue(ageRating.toString());
		
		// And I select a difficulty rating
		final DifficultyRating difficultyRating = DifficultyRating.EASY;
		Select difficultyRatingDropdown = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(DIFFICULTY_RATINGS_DROPDOWN_ID))));
		difficultyRatingDropdown.selectByValue(difficultyRating.toString());

		// Then a list of quizzes are displayed matching those filters
		Thread.sleep(3000); // 3-second wait
		
		List<WebElement> allCards = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
				By.cssSelector("div.card[quiz-card]")
		));
		
		// All Quizzes match
		Set<Long> quizCardIds = allCards.stream().map(card -> Long.parseLong(card.getAttribute("id"))).collect(Collectors.toSet());
		Set<Long> actualQuizIds = quizRepo.findBySearch(searchText, difficultyRating, ageRating).stream().map(Quiz::getId).collect(Collectors.toSet());
		
		assertEquals(quizCardIds, actualQuizIds);
	}

}


