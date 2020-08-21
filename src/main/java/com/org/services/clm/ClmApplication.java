package com.org.services.clm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = { "com.org"} )
@EntityScan("com.org")
@EnableJpaRepositories("com.org")
@SpringBootApplication
public class ClmApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClmApplication.class, args);
	}

}
