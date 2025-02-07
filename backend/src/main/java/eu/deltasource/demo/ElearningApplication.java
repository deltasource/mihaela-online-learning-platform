package eu.deltasource.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for the E-learning Spring Boot application.
 * This class is responsible for bootstrapping and launching the Spring application.
 */
@SpringBootApplication
public class ElearningApplication {
    /**
     * The main entry point of the application.
     * This method starts the Spring application context and runs the E-learning application.
     *
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(ElearningApplication.class, args);
    }
}
