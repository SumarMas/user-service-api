package com.platform.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * Main application class for the User Service.
 */
@SpringBootApplication
public class UserServiceApplication {
    /**
     * The entry point of the User Service application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
