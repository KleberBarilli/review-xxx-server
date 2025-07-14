package com.idealizer.review_x;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class ReviewXApplication {

	public static void main(String[] args) {
		SpringApplicationBuilder  builder = new SpringApplicationBuilder(ReviewXApplication.class);
		builder.run(args);
	}
}
