package com.prometheus.brainbash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BrainBashApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrainBashApplication.class, args);
	}
	
	
	// Temp user created
	/*
	@Bean
    CommandLineRunner init(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create a test admin user on program startup (if not exists)
        	User user = new User();
        	user.setUsername("admin".toLowerCase());
        	user.setPassword(passwordEncoder.encode("Password123!"));
        	user.setAuthorities(Set.of(Role.ROLE_ADMIN));
        	userRepo.save(user);
            System.out.println("User saved successfully!");
        };
    }
    */

}
