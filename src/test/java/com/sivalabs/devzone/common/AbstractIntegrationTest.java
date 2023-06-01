package com.sivalabs.devzone.common;

import static com.sivalabs.devzone.utils.TestConstants.PROFILE_IT;
import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.devzone.config.ApplicationProperties;
import com.sivalabs.devzone.users.models.AuthenticationRequest;
import com.sivalabs.devzone.users.models.AuthenticationResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(PROFILE_IT)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(TestcontainersConfig.class)
public abstract class AbstractIntegrationTest {

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
        AuthenticationRequest authenticationRequestDTO = new AuthenticationRequest(email, password);
        AuthenticationResponse response = given().contentType(ContentType.JSON)
                .body(authenticationRequestDTO)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract()
                .as(AuthenticationResponse.class);
        return response.accessToken();
    }
}
