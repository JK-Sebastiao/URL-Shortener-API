package com.neueda.url.shortener.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class UrlShortenerApi {

	public static void main(String[] args) {
		SpringApplication.run(UrlShortenerApi.class, args);
	}

}
