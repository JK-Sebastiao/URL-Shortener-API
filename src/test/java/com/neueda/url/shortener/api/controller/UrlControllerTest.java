package com.neueda.url.shortener.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neueda.url.shortener.api.dto.FullUrlDTO;
import com.neueda.url.shortener.api.service.UrlService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UrlControllerTest {

    private final MockMvc mockMvc;

    @Autowired
    public UrlControllerTest(MockMvc mockMvc, UrlService urlService) {
        this.mockMvc = mockMvc;
    }


    @Test
    public void shouldReturnStatusOk() throws Exception {
        FullUrlDTO fullUrlDTO = new FullUrlDTO("https://example.com/foo");

        mockMvc.perform(post("/shorten-url")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(fullUrlDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnJsonWithShortUrlProp() throws Exception {
        FullUrlDTO fullUrlDTOObj = new FullUrlDTO("https://example.com/foo");

        mockMvc.perform(post("/shorten-url")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(fullUrlDTOObj)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl").exists());
    }

    @Test
    public void shouldReturnJsonWithShortUrlValueHasHttp() throws Exception {
        FullUrlDTO fullUrlDTO = new FullUrlDTO("https://example.com/foo");
        mockMvc.perform(post("/shorten-url")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(fullUrlDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl", startsWith("http")));
    }

    @Test
    public void shouldNotInsertFullUrlIfAlreadyExists() throws Exception {
        FullUrlDTO fullUrlDTO = new FullUrlDTO("https://example.com/foo");

        String shortUrl1 = mockMvc.perform(post("/shorten-url")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(fullUrlDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl", startsWith("http"))).andReturn().getResponse().getContentAsString();

        String shortUrl2 = mockMvc.perform(post("/shorten-url")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(fullUrlDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl", startsWith("http"))).andReturn().getResponse().getContentAsString();

        Assert.assertEquals(shortUrl1, shortUrl2);
    }

    @Test
    public void shouldNotInsertFullUrlIfDoesNotExist() throws Exception {
        FullUrlDTO fullUrlDTO1 = new FullUrlDTO("https://example.com/foo1");
        FullUrlDTO fullUrlDTO2 = new FullUrlDTO("https://example.com/foo2");

        String shortUrl1 = mockMvc.perform(post("/shorten-url")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(fullUrlDTO1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl", startsWith("http"))).andReturn().getResponse().getContentAsString();

        String shortUrl2 = mockMvc.perform(post("/shorten-url")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(fullUrlDTO2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl", startsWith("http"))).andReturn().getResponse().getContentAsString();

        Assert.assertNotEquals(shortUrl1, shortUrl2);
    }

    public static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

}