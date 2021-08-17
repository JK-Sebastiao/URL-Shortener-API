package com.neueda.url.shortener.api.dto;

import lombok.Data;

@Data
public class ShortUrlDTO {

    private String shortUrl;

    public ShortUrlDTO() {
    }

    public ShortUrlDTO(String shortUrl) {
        this.shortUrl = shortUrl;
    }


}
