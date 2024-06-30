package com.sivalabs.devzone.users.api;

import static io.restassured.RestAssured.given;

import com.sivalabs.devzone.BaseIT;
import com.sivalabs.devzone.users.domain.CreateUserRequest;
import com.sivalabs.devzone.users.domain.LoginRequest;
import com.sivalabs.devzone.users.domain.User;
import com.sivalabs.devzone.users.domain.UserDTO;
import com.sivalabs.devzone.users.domain.UserService;
import io.restassured.http.ContentType;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthenticationControllerTests extends BaseIT {

    @Autowired
    private UserService userService;

    @Test
    void shouldLoginSuccessfullyWithValidCredentials() {
        User user = createUser();
        var authenticationRequest = new LoginRequest(user.getEmail(), user.getPassword());

        given().contentType(ContentType.JSON)
                .body(authenticationRequest)
                .post("/api/login")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldNotLoginWithInvalidCredentials() {
        var authenticationRequest = new LoginRequest("nonexisting@gmail.com", "secret");

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
