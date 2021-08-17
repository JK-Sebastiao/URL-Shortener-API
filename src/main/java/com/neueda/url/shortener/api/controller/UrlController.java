package com.neueda.url.shortener.api.controller;

import com.neueda.url.shortener.api.util.UrlUtil;
import com.neueda.url.shortener.api.dto.ShortUrlDTO;
import com.neueda.url.shortener.api.exception.InvalidUrlException;
import com.neueda.url.shortener.api.dto.FullUrlDTO;
import com.neueda.url.shortener.api.service.UrlService;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.NoSuchElementException;

@RestController
public class UrlController {
    Logger logger = LoggerFactory.getLogger(UrlController.class);

    private final UrlService urlService;

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten-url")
    public ResponseEntity saveUrl(@RequestBody FullUrlDTO fullUrlDTO, HttpServletRequest request) {
        UrlValidator validator = new UrlValidator(new String[]{"http", "https"});
        String url = fullUrlDTO.getFullUrl();

        if (!validator.isValid(url)) {
            logger.error("Malformed Url provided");
            InvalidUrlException error = new InvalidUrlException("URL", fullUrlDTO.getFullUrl(), "Invalid URL");
            return ResponseEntity.badRequest().body(error);
        }

        String baseUrl;

        try {
            baseUrl = UrlUtil.getBaseUrl(request.getRequestURL().toString());
        } catch (MalformedURLException e) {
            logger.error("Malformed request URL");
            return ResponseEntity.badRequest().body("Request URL is invalid");
        }

        ShortUrlDTO shortUrlDTO = urlService.getShortUrl(fullUrlDTO);
        shortUrlDTO.setShortUrl(baseUrl + shortUrlDTO.getShortUrl());
        logger.debug(String.format("Short url for full url %s is %s", fullUrlDTO.getFullUrl(), shortUrlDTO.getShortUrl()));
        return new ResponseEntity<>(shortUrlDTO, HttpStatus.OK);
    }

    @GetMapping("/{shorten}")
    public ResponseEntity redirectToRealUrl(HttpServletResponse response, @PathVariable String shorten) {
        try {
            FullUrlDTO fullUrlDTO = urlService.getFullUrl(shorten);
            logger.info(String.format("Redirecting to %s", fullUrlDTO.getFullUrl()));
            response.sendRedirect(fullUrlDTO.getFullUrl());
        } catch (NoSuchElementException ex) {
            logger.error(String.format("No URL found for %s in the data base", shorten));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Url not found: " + ex.getMessage());
        } catch (IOException ex) {
            logger.error("Could not redirect to the full URL");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not redirect to the full URL: " + ex.getMessage());
        }
        return ResponseEntity.ok("URL was redirect");
    }

}
