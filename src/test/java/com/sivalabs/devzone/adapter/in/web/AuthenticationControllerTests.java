package com.sivalabs.devzone.adapter.in.web;

import static io.restassured.RestAssured.given;

import com.sivalabs.devzone.application.port.in.LoginRequest;
import com.sivalabs.devzone.application.port.in.RegisterUserRequest;
import com.sivalabs.devzone.application.port.in.RegisterUserUseCase;
import com.sivalabs.devzone.common.AbstractIntegrationTest;
import com.sivalabs.devzone.domain.User;
import io.restassured.http.ContentType;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthenticationControllerTests extends AbstractIntegrationTest {

    @Autowired
    private RegisterUserUseCase registerUserUseCase;

    @Test
    void shouldLoginSuccessfullyWithValidCredentials() {
        User user = createUser();
        var loginRequest = new LoginRequest(user.getEmail(), user.getPassword());

        given().contentType(ContentType.JSON)
                .body(loginRequest)
                .post("/api/login")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldNotLoginWithInvalidCredentials() {
        var loginRequest = new LoginRequest("nonexisting@gmail.com", "secret");

        given().contentType(ContentType.JSON)
                .body(loginRequest)
                .post("/api/login")
                .then()
                .statusCode(401);
    }

    private User createUser() {
        String uuid = UUID.randomUUID().toString();
        RegisterUserRequest request = new RegisterUserRequest(uuid, uuid + "@gmail.com", uuid);
        User savedUser = registerUserUseCase.register(request);
        return new User(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                request.password(),
                savedUser.getRole(),
                savedUser.getCreatedAt(),
                savedUser.getUpdatedAt());
    }
}
