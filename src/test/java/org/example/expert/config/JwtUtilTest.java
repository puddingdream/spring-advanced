package org.example.expert.config;

import io.jsonwebtoken.Claims;
import org.example.expert.domain.common.exception.ServerException;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        // JwtUtil.init()은 Base64 인코딩된 secretKey를 기대하므로 테스트에서도 동일하게 맞춘다.
        String secret = Base64.getEncoder().encodeToString("01234567890123456789012345678901".getBytes());
        // ReflectionTestUtils: private 필드(secretKey) 주입용 테스트 유틸
        ReflectionTestUtils.setField(jwtUtil, "secretKey", secret);
        jwtUtil.init();
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void token을_생성하고_claims를_추출한다() {
        // given
        Long userId = 1L;
        String email = "user@example.com";
        UserRole role = UserRole.USER;

        // when
        String token = jwtUtil.createToken(userId, email, role);
        // substringToken: "Bearer " 접두어 제거
        Claims claims = jwtUtil.extractClaims(jwtUtil.substringToken(token));

        // then
        assertTrue(token.startsWith("Bearer "));
        assertEquals("1", claims.getSubject());
        assertEquals(email, claims.get("email"));
        assertEquals("USER", claims.get("userRole").toString());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void bearer_prefix가_있는_토큰은_prefix를_제거한다() {
        // given
        String token = "Bearer abc.def.ghi";

        // when
        String result = jwtUtil.substringToken(token);

        // then
        assertEquals("abc.def.ghi", result);
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 잘못된_형식의_토큰이면_에러가_발생한다() {
        // given
        String token = "abc.def.ghi";

        // when
        ServerException exception = assertThrows(ServerException.class, () -> jwtUtil.substringToken(token));

        // then
        assertEquals("Not Found Token", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 토큰이_null이면_에러가_발생한다() {
        // when
        ServerException exception = assertThrows(ServerException.class, () -> jwtUtil.substringToken(null));

        // then
        assertEquals("Not Found Token", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void init_호출_후_키가_초기화된다() {
        // when
        Object key = ReflectionTestUtils.getField(jwtUtil, "key");

        // then
        assertNotNull(key);
    }
}
