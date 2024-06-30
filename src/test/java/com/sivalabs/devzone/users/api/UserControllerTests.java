package com.sivalabs.devzone.users.api;

import static com.sivalabs.devzone.TestConstants.ADMIN_EMAIL;
import static io.restassured.RestAssured.given;
import static org.instancio.Select.field;

import com.sivalabs.devzone.BaseIT;
import com.sivalabs.devzone.users.domain.CreateUserRequest;
import io.restassured.http.ContentType;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

class UserControllerTests extends BaseIT {

    @Test
    void shouldFindUserById() {
        Long userId = 1L;
        given().get("/api/users/{id}", userId).then().statusCode(200);
    }

    @Test
    void shouldCreateNewUserWithValidData() {

        var request = Instancio.of(CreateUserRequest.class).create();
        given().contentType(ContentType.JSON)
                .body(request)
                .post("/api/users")
                .then()
                .statusCode(201);
    }

    @Test
    void shouldFailToCreateNewUserWithExistingEmail() {
        var request = Instancio.of(CreateUserRequest.class)
                .set(field(CreateUserRequest::email), ADMIN_EMAIL)
                .create();

        given().contentType(ContentType.JSON)
                .body(request)
                .post("/api/users")
                .then()
                .statusCode(400);
    }
}
