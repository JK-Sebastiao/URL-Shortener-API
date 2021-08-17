package com.neueda.url.shortener.api.exception;

import lombok.Data;

@Data
public class InvalidUrlException {
    private String field;
    private String value;
    private String message;

    public InvalidUrlException(String field, String value, String message) {
        this.field = field;
        this.value = value;
        this.message = message;
    }

}
