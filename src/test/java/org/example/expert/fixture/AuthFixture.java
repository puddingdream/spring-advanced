package org.example.expert.fixture;

import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;

public class AuthFixture {

    public static final String DEFAULT_EMAIL = "user1@example.com";
    public static final String DEFAULT_PASSWORD = "Password1";
    public static final String DEFAULT_ROLE = "USER";
    public static final String DEFAULT_TOKEN = "Bearer token";

    private AuthFixture() {
    }

    public static SignupRequest createSignupRequest() {
        return new SignupRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD, DEFAULT_ROLE);
    }

    public static SigninRequest createSigninRequest() {
        return new SigninRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
    }
}
