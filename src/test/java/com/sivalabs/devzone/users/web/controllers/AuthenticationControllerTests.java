package com.sivalabs.devzone.users.web.controllers;

import static io.restassured.RestAssured.given;

import com.sivalabs.devzone.common.AbstractIntegrationTest;
import com.sivalabs.devzone.users.entities.User;
import com.sivalabs.devzone.users.models.AuthenticationRequest;
import com.sivalabs.devzone.users.models.CreateUserRequest;
import com.sivalabs.devzone.users.models.UserDTO;
import com.sivalabs.devzone.users.services.UserService;
import io.restassured.http.ContentType;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthenticationControllerTests extends AbstractIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void shouldLoginSuccessfullyWithValidCredentials() {
        User user = createUser();
        var authenticationRequest = new AuthenticationRequest(user.getEmail(), user.getPassword());

        given().contentType(ContentType.JSON)
                .body(authenticationRequest)
                .post("/api/login")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldNotLoginWithInvalidCredentials() {
        var authenticationRequest = new AuthenticationRequest("nonexisting@gmail.com", "secret");

        given().contentType(ContentType.JSON)
                .body(authenticationRequest)
                .post("/api/login")
                .then()
                .statusCode(401);
    }

    private User createUser() {
        String uuid = UUID.randomUUID().toString();
        CreateUserRequest request = new CreateUserRequest(uuid, uuid + "@gmail.com", uuid);
        UserDTO userDTO = userService.createUser(request);
        User user = new User();
        user.setId(userDTO.id());
        user.setName(userDTO.name());
        user.setEmail(userDTO.email());
        user.setPassword(request.password());
        user.setRole(userDTO.role());
        return user;
    }
}
