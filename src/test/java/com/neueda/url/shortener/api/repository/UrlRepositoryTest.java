package com.neueda.url.shortener.api.repository;

import com.neueda.url.shortener.api.model.Url;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UrlRepositoryTest {

    private final UrlRepository urlRepository;

    @Autowired
    public UrlRepositoryTest(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Test
    public void shouldInsertAndGetFullurl() {
        Url url = new Url("http://example.com");
        urlRepository.save(url);

        assertThat(url.getId(), notNullValue());

        Url urlFromDb = urlRepository.findById(url.getId()).get();
        assertThat(urlFromDb.getId(), equalTo(url.getId()));
    }

}