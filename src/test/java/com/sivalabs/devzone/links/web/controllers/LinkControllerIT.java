package com.sivalabs.devzone.links.web.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.devzone.common.AbstractIntegrationTest;
import com.sivalabs.devzone.links.services.LinkService;
import com.sivalabs.devzone.links.services.LinksImportService;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

class LinkControllerIT extends AbstractIntegrationTest {

    @Autowired LinkService linkService;

    @Autowired LinksImportService linksImportService;

    @BeforeEach
    void setUp() throws CsvValidationException, IOException {
        linkService.deleteAllLinks();
        linksImportService.importLinks("/data/test-links.csv");
    }

    @ParameterizedTest
    @CsvSource({
        "1,25,3,1,true,false,true,false",
        "2,25,3,2,false,false,true,true",
        "3,25,3,3,false,true,false,true"
    })
    void shouldFetchLinksByPageNumber(
            int pageNo,
            int totalElements,
            int totalPages,
            int pageNumber,
            boolean isFirst,
            boolean isLast,
            boolean hasNext,
            boolean hasPrevious)
            throws Exception {
        this.mockMvc
                .perform(get("/api/links?page=" + pageNo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", equalTo(totalElements)))
                .andExpect(jsonPath("$.totalPages", equalTo(totalPages)))
                .andExpect(jsonPath("$.pageNumber", equalTo(pageNumber)))
                .andExpect(jsonPath("$.isFirst", equalTo(isFirst)))
                .andExpect(jsonPath("$.isLast", equalTo(isLast)))
                .andExpect(jsonPath("$.hasNext", equalTo(hasNext)))
                .andExpect(jsonPath("$.hasPrevious", equalTo(hasPrevious)));
    }

    @ParameterizedTest
    @CsvSource({"spring-boot,1,8,1,1,true,true,false,false"})
    void shouldFetchLinksByTag(
            String tag,
            int pageNo,
            int totalElements,
            int totalPages,
            int pageNumber,
            boolean isFirst,
            boolean isLast,
            boolean hasNext,
            boolean hasPrevious)
            throws Exception {
        this.mockMvc
                .perform(get("/api/links?tag={tag}&page={page}", tag, pageNo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", equalTo(totalElements)))
                .andExpect(jsonPath("$.totalPages", equalTo(totalPages)))
                .andExpect(jsonPath("$.pageNumber", equalTo(pageNumber)))
                .andExpect(jsonPath("$.isFirst", equalTo(isFirst)))
                .andExpect(jsonPath("$.isLast", equalTo(isLast)))
                .andExpect(jsonPath("$.hasNext", equalTo(hasNext)))
                .andExpect(jsonPath("$.hasPrevious", equalTo(hasPrevious)));
    }

    @ParameterizedTest
    @CsvSource({"spring,1,9,1,1,true,true,false,false"})
    void shouldSearchLinks(
            String query,
            int pageNo,
            int totalElements,
            int totalPages,
            int pageNumber,
            boolean isFirst,
            boolean isLast,
            boolean hasNext,
            boolean hasPrevious)
            throws Exception {
        this.mockMvc
                .perform(get("/api/links?query={query}&page={page}", query, pageNo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", equalTo(totalElements)))
                .andExpect(jsonPath("$.totalPages", equalTo(totalPages)))
                .andExpect(jsonPath("$.pageNumber", equalTo(pageNumber)))
                .andExpect(jsonPath("$.isFirst", equalTo(isFirst)))
                .andExpect(jsonPath("$.isLast", equalTo(isLast)))
                .andExpect(jsonPath("$.hasNext", equalTo(hasNext)))
                .andExpect(jsonPath("$.hasPrevious", equalTo(hasPrevious)));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com")
    void shouldCreateLinkSuccessfully() throws Exception {
        this.mockMvc
                .perform(
                        post("/api/links")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                        {
                            "title": "SivaLabs Blog",
                            "url": "https://sivalabs.in",
                            "tags": ["java", "spring-boot"]
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("SivaLabs Blog")))
                .andExpect(jsonPath("$.url", is("https://sivalabs.in")));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com")
    void shouldCreateLinkSuccessfully_WithTitle() throws Exception {
        this.mockMvc
                .perform(
                        post("/api/links")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                        {
                            "url": "https://sivalabs.in"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("SivaLabs - My Experiments with Technology")))
                .andExpect(jsonPath("$.url", is("https://sivalabs.in")));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com")
    void shouldFailToCreateLinkWhenUrlIsNotPresent() throws Exception {
        this.mockMvc
                .perform(
                        post("/api/links")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                        {
                            "title": "SivaLabs Blog"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", is("application/problem+json")))
                .andExpect(
                        jsonPath(
                                "$.type",
                                is("https://zalando.github.io/problem/constraint-violation")))
                .andExpect(jsonPath("$.title", is("Constraint Violation")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field", is("url")))
                .andExpect(jsonPath("$.violations[0].message", is("URL cannot be blank")))
                .andReturn();
    }
}
