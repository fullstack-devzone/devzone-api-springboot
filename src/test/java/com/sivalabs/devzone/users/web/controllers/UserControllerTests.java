package com.sivalabs.devzone.users.web.controllers;

import static io.restassured.RestAssured.given;

import com.sivalabs.devzone.common.AbstractIntegrationTest;
import com.sivalabs.devzone.users.models.CreateUserRequest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

class UserControllerTests extends AbstractIntegrationTest {

    @Test
    void shouldFindUserById() {
        Long userId = 1L;
        given().get("/api/users/{id}", userId).then().statusCode(200);
    }

    @Test
    void shouldCreateNewUserWithValidData() {
        CreateUserRequest createUserRequestDTO = new CreateUserRequest("myname", "myemail@gmail.com", "secret");
        given().contentType(ContentType.JSON)
                .body(createUserRequestDTO)
                .post("/api/users")
                .then()
                .statusCode(201);
    }

    @Test
    void shouldFailToCreateNewUserWithExistingEmail() {
        CreateUserRequest createUserRequestDTO = new CreateUserRequest("admin@gmail.com", "secret", "myname");

        given().contentType(ContentType.JSON)
                .body(createUserRequestDTO)
                .post("/api/users")
                .then()
                .statusCode(400);
    }
}
