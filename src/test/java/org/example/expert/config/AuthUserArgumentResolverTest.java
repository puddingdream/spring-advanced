package org.example.expert.config;

import jakarta.servlet.http.HttpServletRequest;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthUserArgumentResolverTest {

    private final AuthUserArgumentResolver resolver = new AuthUserArgumentResolver();

    @SuppressWarnings("unused")
    private static class SampleController {
        public void valid(@Auth AuthUser authUser) {
        }

        public void invalidMissingAnnotation(AuthUser authUser) {
        }

        public void invalidType(@Auth String authUser) {
        }
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void supports_parameter는_auth와_auth_user_조합이면_true를_반환한다() throws NoSuchMethodException {
        // given
        // MethodParameter: 컨트롤러 파라미터 메타데이터(타입/어노테이션)를 런타임에 다루는 객체
        MethodParameter parameter = new MethodParameter(
                SampleController.class.getMethod("valid", AuthUser.class), 0
        );

        // when
        boolean supported = resolver.supportsParameter(parameter);

        // then
        assertTrue(supported);
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void supports_parameter는_auth_어노테이션이_없으면_에러가_발생한다() throws NoSuchMethodException {
        // given
        MethodParameter parameter = new MethodParameter(
                SampleController.class.getMethod("invalidMissingAnnotation", AuthUser.class), 0
        );

        // when
        AuthException exception = assertThrows(AuthException.class, () -> resolver.supportsParameter(parameter));

        // then
        assertEquals("@Auth와 AuthUser 타입은 함께 사용되어야 합니다.", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void supports_parameter는_auth_user_타입이_아니면_에러가_발생한다() throws NoSuchMethodException {
        // given
        MethodParameter parameter = new MethodParameter(
                SampleController.class.getMethod("invalidType", String.class), 0
        );

        // when
        AuthException exception = assertThrows(AuthException.class, () -> resolver.supportsParameter(parameter));

        // then
        assertEquals("@Auth와 AuthUser 타입은 함께 사용되어야 합니다.", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void resolve_argument는_request_attribute로부터_auth_user를_생성한다() {
        // given
        HttpServletRequest request = new MockHttpServletRequest();
        // 실제 JwtFilter가 넣어주는 attribute 키와 동일하게 세팅해야 resolver 로직을 그대로 검증할 수 있다.
        request.setAttribute("userId", 1L);
        request.setAttribute("email", "user@example.com");
        request.setAttribute("userRole", "USER");
        NativeWebRequest webRequest = new ServletWebRequest(request);

        // when
        AuthUser authUser = (AuthUser) resolver.resolveArgument(null, null, webRequest, null);

        // then
        assertEquals(1L, authUser.getId());
        assertEquals("user@example.com", authUser.getEmail());
        assertEquals(UserRole.USER, authUser.getUserRole());
    }
}
