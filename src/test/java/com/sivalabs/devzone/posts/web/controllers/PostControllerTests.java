package com.sivalabs.devzone.posts.web.controllers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.devzone.common.AbstractIntegrationTest;
import com.sivalabs.devzone.posts.services.PostService;
import com.sivalabs.devzone.posts.services.PostsImportService;
import io.restassured.http.ContentType;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

class PostControllerTests extends AbstractIntegrationTest {

    @Autowired
    PostService postService;

    @Autowired
    PostsImportService postsImportService;

    @BeforeEach
    void setUp() throws CsvValidationException, IOException {
        postService.deleteAllPosts();
        postsImportService.importPosts("/data/test-posts.csv");
    }

    @ParameterizedTest
    @CsvSource({"1,25,3,1,true,false,true,false", "2,25,3,2,false,false,true,true", "3,25,3,3,false,true,false,true"})
    void shouldFetchPostsByPageNumber(
            int pageNo,
            int totalElements,
            int totalPages,
            int pageNumber,
            boolean isFirst,
            boolean isLast,
            boolean hasNext,
            boolean hasPrevious) {

        given().get("/api/posts?page={page}", pageNo)
                .then()
                .statusCode(200)
                .body("totalElements", equalTo(totalElements))
                .body("totalPages", equalTo(totalPages))
                .body("pageNumber", equalTo(pageNumber))
                .body("isFirst", equalTo(isFirst))
                .body("isLast", equalTo(isLast))
                .body("hasNext", equalTo(hasNext))
                .body("hasPrevious", equalTo(hasPrevious));
    }

    @ParameterizedTest
    @CsvSource({"spring,1,9,1,1,true,true,false,false"})
    void shouldSearchPosts(
            String query,
            int pageNo,
            int totalElements,
            int totalPages,
            int pageNumber,
            boolean isFirst,
            boolean isLast,
            boolean hasNext,
            boolean hasPrevious) {

        given().get("/api/posts?query={query}&page={page}", query, pageNo)
                .then()
                .statusCode(200)
                .body("totalElements", equalTo(totalElements))
                .body("totalPages", equalTo(totalPages))
                .body("pageNumber", equalTo(pageNumber))
                .body("isFirst", equalTo(isFirst))
                .body("isLast", equalTo(isLast))
                .body("hasNext", equalTo(hasNext))
                .body("hasPrevious", equalTo(hasPrevious));
    }

    @Test
    void shouldCreatePostSuccessfully() {
        String jwtToken = this.getAuthToken("admin@gmail.com", "admin");
        given().contentType("application/json")
                .header(properties.getJwt().getHeader(), "Bearer " + jwtToken)
                .body(
                        """
                        {
                            "title": "SivaLabs Blog",
                            "url": "https://sivalabs.in",
                            "content": "java blog"
                        }
                        """)
                .post("/api/posts")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", is("SivaLabs Blog"))
                .body("url", is("https://sivalabs.in"));
    }

    @Test
    void shouldFailToCreatePostWhenUrlIsNotPresent() {
        given().contentType(ContentType.JSON)
                .body(
                        """
                        {
                            "title": "SivaLabs Blog"
                        }
                        """)
                .post("/api/posts")
                .then()
                .statusCode(400);
    }
}
