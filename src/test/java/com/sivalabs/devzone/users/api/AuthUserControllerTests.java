package com.sivalabs.devzone.users.api;

import static com.sivalabs.devzone.TestConstants.ADMIN_EMAIL;
import static io.restassured.RestAssured.given;

import com.sivalabs.devzone.BaseIT;
import com.sivalabs.devzone.security.TokenHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthUserControllerTests extends BaseIT {

    @Autowired
    private TokenHelper tokenHelper;

    @Test
    void shouldGetLoginUserDetails() {
        String jwtToken = tokenHelper.generateToken(ADMIN_EMAIL);
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
