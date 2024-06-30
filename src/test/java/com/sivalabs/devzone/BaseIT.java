package com.sivalabs.devzone;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.devzone.users.domain.LoginRequest;
import com.sivalabs.devzone.users.domain.LoginResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(TestcontainersConfig.class)
public abstract class BaseIT {

    @LocalServerPort
    int port;

    @Autowired
    protected ApplicationProperties properties;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    void setUpBase() {
        RestAssured.port = port;
    }

    protected String getAuthToken(String email, String password) {
        LoginRequest loginRequestDTO = new LoginRequest(email, password);
        LoginResponse response = given().contentType(ContentType.JSON)
                .body(loginRequestDTO)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract()
                .as(LoginResponse.class);
        return response.accessToken();
    }
}
