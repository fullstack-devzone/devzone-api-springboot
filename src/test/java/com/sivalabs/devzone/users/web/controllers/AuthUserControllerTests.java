package com.sivalabs.devzone.users.web.controllers;

import static io.restassured.RestAssured.given;

import com.sivalabs.devzone.common.AbstractIntegrationTest;
import com.sivalabs.devzone.config.security.TokenHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthUserControllerTests extends AbstractIntegrationTest {

    @Autowired
    private TokenHelper tokenHelper;

    @Test
    void shouldGetLoginUserDetails() {
        String jwtToken = tokenHelper.generateToken("admin@gmail.com");
        given().header(properties.getJwt().getHeader(), "Bearer " + jwtToken)
                .get("/api/me")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldFailToGetLoginUserDetailsIfUnauthorized() {
        given().get("/api/me").then().statusCode(403);
    }
}
