package com.neueda.url.shortener.api.service;

import com.neueda.url.shortener.api.repository.UrlRepository;
import com.neueda.url.shortener.api.util.ShortenUtil;
import com.neueda.url.shortener.api.model.Url;
import com.neueda.url.shortener.api.dto.FullUrlDTO;
import com.neueda.url.shortener.api.dto.ShortUrlDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UrlService {

    Logger logger = LoggerFactory.getLogger(UrlService.class);

    private final UrlRepository urlRepository;

    @Autowired
    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }


    private Url get(Long id) {
        logger.info(String.format("Fetching URL from database for ID %d", id));
        return urlRepository.findById(id).orElseThrow();
    }

    public FullUrlDTO getFullUrl(String shorten) {
        logger.debug(String.format("Converting Shorten string: %s from Base 62 to Base 10 (URL ID)", shorten));
        Long id = ShortenUtil.strToId(shorten);
        logger.info(String.format("Shorten string: %s, converted from Base 62  to URL ID: %d (in Base 10)", shorten, id));

        logger.info(String.format("Retrieving full URL for %d", id));
        return new FullUrlDTO(this.get(id).getFullUrl());
    }

    private Url save(FullUrlDTO fullUrlDTO) {
        return urlRepository.save(new Url(fullUrlDTO.getFullUrl()));
    }

    public ShortUrlDTO getShortUrl(FullUrlDTO fullUrlDTO) {

        logger.info("Checking if the given URL already exists");
        List<Url> savedUrls = checkFullUrlAlreadyExists(fullUrlDTO);

        Url savedUrl;

        if (savedUrls.isEmpty()) {
            logger.info("Saving given URL to database");
            savedUrl = this.save(fullUrlDTO);
            logger.debug(savedUrl.toString());
        }
        else {
            savedUrl = savedUrls.get(0);
            logger.info(String.format("url: %s already exists in the database!", savedUrl));
        }

        logger.debug(String.format("Converting URL ID: %d from Base 10 to Base 62", savedUrl.getId()));
        String shortUrlText = ShortenUtil.idToStr(savedUrl.getId());
        logger.info(String.format("URL ID: %d, converted from Base 10  to string %s (in Base 62)", savedUrl.getId(), shortUrlText));

        return new ShortUrlDTO(shortUrlText);
    }

    private List<Url> checkFullUrlAlreadyExists(FullUrlDTO fullUrlDTO) {
        return urlRepository.findByFullUrl(fullUrlDTO.getFullUrl());
    }
}
