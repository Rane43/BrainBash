package com.prometheus.brainbash.test_helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.prometheus.brainbash.dao.QuizRepository;
import com.prometheus.brainbash.dao.UserQuizScoreRepository;
import com.prometheus.brainbash.dao.UserRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

@Component
public class DatabaseManager {
	private static final String SETUP_SCRIPTS_FOLDER = "/test_data/setup_scripts/";
    @Autowired
    private DataSource dataSource;
    
    // Repositories
    @Autowired
    private QuizRepository quizRepo;
    
    @Autowired
    private UserQuizScoreRepository userQuizScoreRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    // Order matters - call failure setup script must execute after event cause, failure class etc. due to referential integrity.
    private final List<String> scripts = List.of(
    		SETUP_SCRIPTS_FOLDER + "users.sql",
    		SETUP_SCRIPTS_FOLDER + "quizzes.sql",
    		SETUP_SCRIPTS_FOLDER + "questions.sql",
    		SETUP_SCRIPTS_FOLDER + "answers.sql",
    		SETUP_SCRIPTS_FOLDER + "quiz_developers.sql",
    		SETUP_SCRIPTS_FOLDER + "user_quiz_scores.sql"
    );
    
    @Transactional
    public void executeUserSetupScript() {
    	// Wipe database first
    	clearDatabase();
    	
    	// Execute user setup script
        try (Connection connection = dataSource.getConnection()) {
        	ScriptUtils.executeSqlScript(connection, new ClassPathResource(SETUP_SCRIPTS_FOLDER + "users.sql"));
        } catch (SQLException e) {
            throw new RuntimeException("Error executing setup scripts", e);
        }
    }
   
    @Transactional
    public void executeSetupScripts() {
    	// Wipe database first
    	clearDatabase();
    	
    	// Execute scripts
        try (Connection connection = dataSource.getConnection()) {
            for (String script : scripts) {
                ScriptUtils.executeSqlScript(connection, new ClassPathResource(script));
                System.out.println("Executed script: " + script);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing setup scripts", e);
        }
    }
    
    @Transactional
    public void clearDatabase() {
        try {
        	userQuizScoreRepo.deleteAll();
            quizRepo.deleteAll(); // Clears questions and answers also
            userRepo.deleteAll();
            System.out.println("Database cleared successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Error clearing the database", e);
        }
    }
}
