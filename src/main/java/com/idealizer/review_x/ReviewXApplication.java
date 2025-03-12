package com.idealizer.review_x;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ReviewXApplication {

	public static void main(String[] args) {
		SpringApplicationBuilder  builder = new SpringApplicationBuilder(ReviewXApplication.class);
		builder.run(args);
	}
}
