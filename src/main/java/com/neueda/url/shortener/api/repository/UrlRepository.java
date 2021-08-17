package com.neueda.url.shortener.api.repository;

import com.neueda.url.shortener.api.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    List<Url> findByFullUrl(String fullUrl);
}
