package com.ibm.microservices.wfd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AppetizerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppetizerApplication.class, args);
		System.out.println("Running "+AppetizerApplication.class+" via Spring Boot!");
	}
}
