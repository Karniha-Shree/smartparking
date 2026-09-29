package com.smartparking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This is the entry point of the whole application.
 * When you run this class, Spring Boot starts up:
 *  - scans com.smartparking and all its sub-packages
 *  - creates all @Component / @Service / @Repository / @RestController beans
 *  - starts an embedded Tomcat server on the port set in application.properties
 */
@SpringBootApplication
public class SmartParkingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartParkingApplication.class, args);
    }

}
