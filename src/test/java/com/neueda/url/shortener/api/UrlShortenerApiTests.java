package com.neueda.url.shortener.api;

import com.neueda.url.shortener.api.service.UrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UrlShortenerApiTests {

	@Autowired
	private UrlService urlService;

	@Test
	void contextLoads() {
	}

}
