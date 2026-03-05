package org.example.expert.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FilterConfigTest {

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void jwt_filter_registration_bean을_생성한다() {
        // given
        JwtUtil jwtUtil = Mockito.mock(JwtUtil.class);
        ObjectMapper objectMapper = new ObjectMapper();
        FilterConfig filterConfig = new FilterConfig(jwtUtil, objectMapper);

        // when
        FilterRegistrationBean<JwtFilter> bean = filterConfig.jwtFilter();

        // then
        assertNotNull(bean.getFilter());
        assertEquals("/*", bean.getUrlPatterns().iterator().next());
    }
}
