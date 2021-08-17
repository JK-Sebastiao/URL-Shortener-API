package com.neueda.url.shortener.api.model;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_url")
    private String fullUrl;

    @Column(name = "short_url")
    private String shortUrl;

    public Url() {
    }

    public Url(Long id, String fullUrl, String shortUrl) {
        this.id = id;
        this.fullUrl = fullUrl;
        this.shortUrl = shortUrl;
    }

    public Url(String fullUrl) {
        this.fullUrl = fullUrl;
    }

}
