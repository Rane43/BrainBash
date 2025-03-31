package com.prometheus.brainbash.selenium;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;

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

import com.prometheus.brainbash.model.AgeRating; 
import com.prometheus.brainbash.model.Category; 
import com.prometheus.brainbash.model.DifficultyRating; 
import com.prometheus.brainbash.model.Role; 
import com.prometheus.brainbash.test_helper.DatabaseManager; 
import com.prometheus.brainbash.test_helper.LoginHelper;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)

@TestInstance(Lifecycle.PER_CLASS) 
class QuizCreationSeleniumTest { 

    private static final String OPEN_CREATE_QUIZ_MODAL_BTN_ID = "create-quiz-button"; 
    private static final String MODAL_ID = "quiz-modal"; 
    private static final String TITLE_INPUT_ID = "quiz-creation-title"; 
    private static final String DESCRIPTION_INPUT_ID = "quiz-description"; 
    private static final String AGE_RATING_DROPDOWN_ID = "modal-age-rating-dropdown"; 
    private static final String DIFFICULTY_RATING_DROPDOWN_ID = "modal-difficulty-rating-dropdown"; 
    private static final String CATEGORY_DROPDOWN_ID = "modal-category-dropdown"; 
    private static final String CREATE_QUIZ_BTN_ID = "submit-quiz-creation";

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

    // ----------- SUCCESSFULLY CREATE A GAME -------------
    @Test 
    void successfullyCreateAGame() throws InterruptedException { 
        // Given I am logged in as a quiz designer 
        loginHelper.loginAs(Role.ROLE_QUIZ_DESIGNER);

        // And I am on the homepage (Create your own quiz page is the modal)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OPEN_CREATE_QUIZ_MODAL_BTN_ID))).click();

        // When I enter a title 
        final String newTitle = "New Title";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(TITLE_INPUT_ID))).sendKeys(newTitle);

        // And I enter a description 
        final String newDescription = "New Description";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(DESCRIPTION_INPUT_ID))).sendKeys(newDescription);

        // And I choose the category 
        final Category category = Category.ANATOMY;
        Select categoryDropdown = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CATEGORY_DROPDOWN_ID))));
        categoryDropdown.selectByValue(category.toString());

        // And I choose the difficulty rating 
        final DifficultyRating difficultyRating = DifficultyRating.EASY;
        Select difficultyRatingDropdown = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(DIFFICULTY_RATING_DROPDOWN_ID))));
        difficultyRatingDropdown.selectByValue(difficultyRating.toString());

        // And I choose the age rating 
        final AgeRating ageRating = AgeRating.CHILDREN;
        Select ageRatingDropdown = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(AGE_RATING_DROPDOWN_ID))));
        ageRatingDropdown.selectByValue(ageRating.toString());

        // And I click “create quiz”
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CREATE_QUIZ_BTN_ID))).click();

        // Get all quiz cards
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(MODAL_ID)));
        // Modal disappears

        List<WebElement> allCards = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("div.card[quiz-editor-card]")));

        // Check to see if card has been added 
        boolean cardFound = false; 
        for (WebElement card : allCards) { 
            WebElement cardBody = card.findElement(By.className("card-body"));

            WebElement cardTitle = cardBody.findElement(By.tagName("h5")); 
            String headerText = cardTitle.getText();

            WebElement cardText = cardBody.findElement(By.tagName("p")); 
            String paragraphText = cardText.getText();

            // Check if this card satisfies the requirement 
            if (headerText.contains(newTitle) && paragraphText.contains(newDescription)) { 
                cardFound = true; // If any card matches the condition break; // Exit the loop if we find a matching card 
                break; 
            } 
        }

        // Assert that we found at least one card that satisfies the condition 
        assertTrue(cardFound); 
    }

}
