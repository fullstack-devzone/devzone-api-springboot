package com.sivalabs.devzone.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

class JwtAuthToken extends AbstractAuthenticationToken {
    private final String token;
    private final UserDetails principle;

    public JwtAuthToken(String token, UserDetails principle) {
        super(principle.getAuthorities());
        this.token = token;
        this.principle = principle;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public UserDetails getPrincipal() {
        return principle;
    }
}
