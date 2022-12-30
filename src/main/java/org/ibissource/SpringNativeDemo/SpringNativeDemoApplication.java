package org.ibissource.SpringNativeDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ImportResource;

// Both annotations below are required. @ServletComponentScan enables @Weblistener support, which is used in ServletListener.java
// Without @ServletComponentScan, the servlets are not registered. Without @SpringBootApplication, the program isn't run as a Spring Boot app.

@SpringBootApplication
@ServletComponentScan 
public class SpringNativeDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringNativeDemoApplication.class, args);
	}

}
