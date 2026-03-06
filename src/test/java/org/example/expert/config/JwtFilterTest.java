package org.example.expert.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private FilterChain filterChain;

    private JwtFilter createFilter() {
        // JwtFilter는 ObjectMapper가 필요하므로 실제 인스턴스를 넣어 응답 JSON까지 검증한다.
        return new JwtFilter(jwtUtil, new ObjectMapper());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void auth_경로는_토큰_검증없이_통과한다() throws Exception {
        // given
        JwtFilter filter = createFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/auth/signin");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        filter.doFilter(request, response, filterChain);

        // then
        then(filterChain).should().doFilter(request, response);
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 인증_헤더가_없으면_401을_반환한다() throws Exception {
        // given
        JwtFilter filter = createFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/todos");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        filter.doFilter(request, response, filterChain);

        // then
        assertEquals(401, response.getStatus());
        assertTrue(response.getContentAsString().contains("인증이 필요합니다."));
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void claims가_null이면_401을_반환한다() throws Exception {
        // given
        JwtFilter filter = createFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/todos");
        request.addHeader("Authorization", "Bearer token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        // 1) Bearer 접두어 제거 성공
        given(jwtUtil.substringToken(anyString())).willReturn("token");
        // 2) claims 추출 실패(null) 분기로 유도
        given(jwtUtil.extractClaims("token")).willReturn(null);

        // when
        filter.doFilter(request, response, filterChain);

        // then
        assertEquals(401, response.getStatus());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void admin_경로에_user권한이면_403을_반환한다() throws Exception {
        // given
        JwtFilter filter = createFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/users/1");
        request.addHeader("Authorization", "Bearer token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        // /admin 경로 + USER 권한 조합이면 403 분기를 탄다.
        Claims claims = org.mockito.Mockito.mock(Claims.class);
        given(claims.get("userRole", String.class)).willReturn("USER");
        given(claims.getSubject()).willReturn("1");
        given(claims.get("email")).willReturn("user@example.com");
        given(claims.get("userRole")).willReturn("USER");
        given(jwtUtil.substringToken(anyString())).willReturn("token");
        given(jwtUtil.extractClaims("token")).willReturn(claims);

        // when
        filter.doFilter(request, response, filterChain);

        // then
        assertEquals(403, response.getStatus());
        assertTrue(response.getContentAsString().contains("접근 권한이 없습니다."));
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 유효한_토큰이면_요청을_통과시킨다() throws Exception {
        // given
        JwtFilter filter = createFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/todos");
        request.addHeader("Authorization", "Bearer token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        // admin 경로가 아닌 일반 경로에서는 권한과 무관하게 통과해야 한다.
        Claims claims = org.mockito.Mockito.mock(Claims.class);
        given(claims.get("userRole", String.class)).willReturn("ADMIN");
        given(claims.getSubject()).willReturn("1");
        given(claims.get("email")).willReturn("user@example.com");
        given(claims.get("userRole")).willReturn("ADMIN");
        given(jwtUtil.substringToken(anyString())).willReturn("token");
        given(jwtUtil.extractClaims("token")).willReturn(claims);

        // when
        filter.doFilter(request, response, filterChain);

        // then
        then(filterChain).should().doFilter(request, response);
        assertEquals(1L, request.getAttribute("userId"));
        assertEquals("user@example.com", request.getAttribute("email"));
        assertEquals("ADMIN", request.getAttribute("userRole"));
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void admin_경로에_admin권한이면_요청을_통과시킨다() throws Exception {
        // given
        JwtFilter filter = createFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/comments/1");
        request.addHeader("Authorization", "Bearer token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        // /admin 경로 + ADMIN 권한이면 통과해야 한다.
        Claims claims = org.mockito.Mockito.mock(Claims.class);
        given(claims.get("userRole", String.class)).willReturn("ADMIN");
        given(claims.getSubject()).willReturn("1");
        given(claims.get("email")).willReturn("admin@example.com");
        given(claims.get("userRole")).willReturn("ADMIN");
        given(jwtUtil.substringToken(anyString())).willReturn("token");
        given(jwtUtil.extractClaims("token")).willReturn(claims);

        // when
        filter.doFilter(request, response, filterChain);

        // then
        then(filterChain).should().doFilter(request, response);
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 만료된_jwt면_401을_반환한다() throws Exception {
        // given
        JwtFilter filter = createFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/todos");
        request.addHeader("Authorization", "Bearer token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        Claims claims = org.mockito.Mockito.mock(Claims.class);
        Header<?> header = org.mockito.Mockito.mock(Header.class);
        given(claims.getSubject()).willReturn("1");
        given(jwtUtil.substringToken(anyString())).willReturn("token");
        given(jwtUtil.extractClaims("token")).willThrow(new ExpiredJwtException(header, claims, "expired"));

        // when
        filter.doFilter(request, response, filterChain);

        // then
        assertEquals(401, response.getStatus());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void malformed_jwt면_400을_반환한다() throws Exception {
        // given
        JwtFilter filter = createFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/todos");
        request.addHeader("Authorization", "Bearer token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        given(jwtUtil.substringToken(anyString())).willReturn("token");
        given(jwtUtil.extractClaims("token")).willThrow(new MalformedJwtException("malformed"));

        // when
        filter.doFilter(request, response, filterChain);

        // then
        assertEquals(400, response.getStatus());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void unsupported_jwt면_400을_반환한다() throws Exception {
        // given
        JwtFilter filter = createFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/todos");
        request.addHeader("Authorization", "Bearer token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        given(jwtUtil.substringToken(anyString())).willReturn("token");
        given(jwtUtil.extractClaims("token")).willThrow(new UnsupportedJwtException("unsupported"));

        // when
        filter.doFilter(request, response, filterChain);

        // then
        assertEquals(400, response.getStatus());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void security_exception이면_400을_반환한다() throws Exception {
        // given
        JwtFilter filter = createFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/todos");
        request.addHeader("Authorization", "Bearer token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        given(jwtUtil.substringToken(anyString())).willReturn("token");
        given(jwtUtil.extractClaims("token")).willThrow(new SecurityException("security"));

        // when
        filter.doFilter(request, response, filterChain);

        // then
        assertEquals(400, response.getStatus());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 예상치못한_예외면_500을_반환한다() throws Exception {
        // given
        JwtFilter filter = createFilter();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/todos");
        request.addHeader("Authorization", "Bearer token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        given(jwtUtil.substringToken(anyString())).willReturn("token");
        given(jwtUtil.extractClaims("token")).willThrow(new RuntimeException("boom"));

        // when
        filter.doFilter(request, response, filterChain);

        // then
        assertEquals(500, response.getStatus());
        assertTrue(response.getContentAsString().contains("요청 처리 중 오류가 발생했습니다."));
    }
}
