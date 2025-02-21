package com.email.writer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class EmailWriterApplication {

	public static void main(String[] args) {
		System.out.println("Received email request: ");
		SpringApplication.run(EmailWriterApplication.class, args);

	}

}
