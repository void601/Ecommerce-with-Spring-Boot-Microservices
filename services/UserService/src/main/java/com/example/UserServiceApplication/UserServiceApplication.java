package com.example.UserServiceApplication;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load(); // Load .env file
		dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue())); // Set as system properties
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
