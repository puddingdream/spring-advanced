package org.example.expert.domain.auth.dto;

import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthDtoTest {

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void signin_request를_생성한다() {
        // given
        String email = "user@example.com";
        String password = "Password1";

        // when
        SigninRequest request = new SigninRequest(email, password);

        // then
        assertEquals(email, request.getEmail());
        assertEquals(password, request.getPassword());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void signup_request를_생성한다() {
        // given
        String email = "user@example.com";
        String password = "Password1";
        String role = "USER";

        // when
        SignupRequest request = new SignupRequest(email, password, role);

        // then
        assertEquals(email, request.getEmail());
        assertEquals(password, request.getPassword());
        assertEquals(role, request.getUserRole());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void signin_response를_생성한다() {
        // given
        String token = "Bearer token";

        // when
        SigninResponse response = new SigninResponse(token);

        // then
        assertEquals(token, response.getBearerToken());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void signup_response를_생성한다() {
        // given
        String token = "Bearer token";

        // when
        SignupResponse response = new SignupResponse(token);

        // then
        assertEquals(token, response.getBearerToken());
    }
}
