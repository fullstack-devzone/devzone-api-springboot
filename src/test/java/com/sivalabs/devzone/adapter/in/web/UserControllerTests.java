package com.sivalabs.devzone.adapter.in.web;

import static com.sivalabs.devzone.common.TestConstants.ADMIN_EMAIL;
import static io.restassured.RestAssured.given;
import static org.instancio.Select.field;

import com.sivalabs.devzone.application.port.in.RegisterUserRequest;
import com.sivalabs.devzone.common.AbstractIntegrationTest;
import io.restassured.http.ContentType;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

class UserControllerTests extends AbstractIntegrationTest {

    @Test
    void shouldFindUserById() {
        Long userId = 1L;
        given().get("/api/users/{id}", userId).then().statusCode(200);
    }

    @Test
    void shouldCreateNewUserWithValidData() {

        var request = Instancio.of(RegisterUserRequest.class).create();
        given().contentType(ContentType.JSON)
                .body(request)
                .post("/api/users")
                .then()
                .statusCode(201);
    }

    @Test
    void shouldFailToCreateNewUserWithExistingEmail() {
        var request = Instancio.of(RegisterUserRequest.class)
                .set(field(RegisterUserRequest::email), ADMIN_EMAIL)
                .create();

        given().contentType(ContentType.JSON)
                .body(request)
                .post("/api/users")
                .then()
                .statusCode(400);
    }
}
