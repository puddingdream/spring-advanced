package org.example.expert.domain.auth.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthExceptionTest {

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void auth_exception_메시지를_보관한다() {
        // given
        String message = "auth error";

        // when
        AuthException exception = new AuthException(message);

        // then
        assertEquals(message, exception.getMessage());
    }
}
