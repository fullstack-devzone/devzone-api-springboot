package com.sivalabs.devzone.posts.web.controllers;

import static com.sivalabs.devzone.common.TestConstants.ADMIN_EMAIL;
import static com.sivalabs.devzone.common.TestConstants.ADMIN_PASSWORD;
import static com.sivalabs.devzone.common.TestConstants.NORMAL_USER_EMAIL;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.devzone.common.AbstractIntegrationTest;
import com.sivalabs.devzone.config.security.TokenHelper;
import com.sivalabs.devzone.posts.models.CreatePostRequest;
import com.sivalabs.devzone.posts.models.PostDTO;
import com.sivalabs.devzone.posts.services.PostService;
import com.sivalabs.devzone.posts.services.PostsImportService;
import com.sivalabs.devzone.users.entities.User;
import com.sivalabs.devzone.users.services.UserService;
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
    UserService userService;

    @Autowired
    PostsImportService postsImportService;

    @Autowired
    TokenHelper tokenHelper;

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
        String jwtToken = tokenHelper.generateToken(ADMIN_EMAIL);
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

    @Test
    void shouldGetPostByIdSuccessfully() {
        User user = userService.getUserByEmail(NORMAL_USER_EMAIL).orElseThrow();
        var request = new CreatePostRequest("Sample title", "https://sivalabs.in", "Sample content", user.getId());
        PostDTO post = postService.createPost(request);
        given().contentType("application/json")
                .get("/api/posts/{id}", post.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(post.getId().intValue()))
                .body("title", equalTo(post.getTitle()))
                .body("url", equalTo(post.getUrl()))
                .body("createdBy.id", equalTo(user.getId().intValue()));
    }

    @Test
    void shouldGetNotFoundWhenPostIdNotExist() {
        given().contentType("application/json")
                .get("/api/posts/{id}", 9999)
                .then()
                .statusCode(404);
    }

    @Test
    void shouldBeAbleToDeleteOwnPosts() {
        User user = userService.getUserByEmail(NORMAL_USER_EMAIL).orElseThrow();
        String jwtToken = tokenHelper.generateToken(NORMAL_USER_EMAIL);
        var request = new CreatePostRequest("Sample title", "https://sivalabs.in", "Sample content", user.getId());
        PostDTO post = postService.createPost(request);
        given().contentType("application/json")
                .header(properties.getJwt().getHeader(), "Bearer " + jwtToken)
                .delete("/api/posts/{id}", post.getId())
                .then()
                .statusCode(200);
    }

    @Test
    void shouldGetNotFoundWhenPostIdNotExistToDelete() {
        String jwtToken = this.getAuthToken(ADMIN_EMAIL, ADMIN_PASSWORD);
        given().contentType("application/json")
                .header(properties.getJwt().getHeader(), "Bearer " + jwtToken)
                .delete("/api/posts/{id}", 9999)
                .then()
                .statusCode(404);
    }

    @Test
    void adminShouldBeAbleToDeletePostCreatedByOtherUsers() {
        User user = userService.getUserByEmail(NORMAL_USER_EMAIL).orElseThrow();
        String jwtToken = tokenHelper.generateToken(ADMIN_EMAIL);
        var request = new CreatePostRequest("Sample title", "https://sivalabs.in", "Sample content", user.getId());
        PostDTO post = postService.createPost(request);
        given().contentType("application/json")
                .header(properties.getJwt().getHeader(), "Bearer " + jwtToken)
                .delete("/api/posts/{id}", post.getId())
                .then()
                .statusCode(200);
    }

    @Test
    void normalUserShouldNotBeAbleToDeletePostCreatedByOtherUsers() {
        User user = userService.getUserByEmail(ADMIN_EMAIL).orElseThrow();
        String jwtToken = tokenHelper.generateToken(NORMAL_USER_EMAIL);
        var request = new CreatePostRequest("Sample title", "https://sivalabs.in", "Sample content", user.getId());
        PostDTO post = postService.createPost(request);
        given().contentType("application/json")
                .header(properties.getJwt().getHeader(), "Bearer " + jwtToken)
                .delete("/api/posts/{id}", post.getId())
                .then()
                .statusCode(403);
    }
}
