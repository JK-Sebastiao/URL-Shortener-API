package com.neueda.url.shortener.api.dto;

import lombok.Data;

@Data
public class FullUrlDTO {

    private String fullUrl;

    public FullUrlDTO() {
    }

    public FullUrlDTO(String fullUrl) {
        this.fullUrl = fullUrl;
    }


}
