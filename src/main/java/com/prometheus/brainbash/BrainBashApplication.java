package com.prometheus.brainbash;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.prometheus.brainbash.dao.UserRepository;
import com.prometheus.brainbash.model.Role;
import com.prometheus.brainbash.model.User;

@SpringBootApplication
public class BrainBashApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrainBashApplication.class, args);
	}
	
	
	// Temp user created
	@Bean
    CommandLineRunner init(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create a test admin user on program startup (if not exists)
        	User user = new User();
        	user.setUsername("testAdmin".toLowerCase());
        	user.setPassword(passwordEncoder.encode("TestPassword123!"));
        	user.setAuthorities(Set.of(Role.ROLE_ADMIN));
        	userRepo.save(user);
            System.out.println("User saved successfully!");
        };
    }

}
