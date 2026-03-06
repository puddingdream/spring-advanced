package org.example.expert.config;

import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.common.exception.ServerException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void invalid_request_exception을_400으로_변환한다() {
        // when
        ResponseEntity<Map<String, Object>> response =
                handler.invalidRequestExceptionException(new InvalidRequestException("invalid"));

        // then
        assertEquals(400, response.getStatusCode().value());
        assertEquals("invalid", response.getBody().get("message"));
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void auth_exception을_401로_변환한다() {
        // when
        ResponseEntity<Map<String, Object>> response =
                handler.handleAuthException(new AuthException("auth"));

        // then
        assertEquals(401, response.getStatusCode().value());
        assertEquals("auth", response.getBody().get("message"));
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void server_exception을_500으로_변환한다() {
        // when
        ResponseEntity<Map<String, Object>> response =
                handler.handleServerException(new ServerException("server"));

        // then
        assertEquals(500, response.getStatusCode().value());
        assertEquals("server", response.getBody().get("message"));
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void get_error_response는_상태_코드_메시지를_담는다() {
        // when
        ResponseEntity<Map<String, Object>> response =
                handler.getErrorResponse(HttpStatus.BAD_REQUEST, "message");

        // then
        assertEquals("BAD_REQUEST", response.getBody().get("status"));
        assertEquals(400, response.getBody().get("code"));
        assertEquals("message", response.getBody().get("message"));
    }
}
