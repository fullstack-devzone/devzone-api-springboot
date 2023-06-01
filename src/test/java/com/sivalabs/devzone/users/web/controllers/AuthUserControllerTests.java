package com.sivalabs.devzone.users.web.controllers;

import static io.restassured.RestAssured.given;

import com.sivalabs.devzone.common.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

class AuthUserControllerTests extends AbstractIntegrationTest {

    @Test
    void shouldGetLoginUserDetails() {
        String jwtToken = this.getAuthToken("admin@gmail.com", "admin");
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
