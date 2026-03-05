package org.example.expert.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WebMvcAndPersistenceConfigTest {

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void web_mvc_config는_auth_user_argument_resolver를_추가한다() {
        // given
        WebMvcConfig webMvcConfig = new WebMvcConfig();
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

        // when
        webMvcConfig.addArgumentResolvers(resolvers);

        // then
        assertEquals(1, resolvers.size());
        assertTrue(resolvers.get(0) instanceof AuthUserArgumentResolver);
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void persistence_config_인스턴스_생성에_성공한다() {
        // when
        PersistenceConfig persistenceConfig = new PersistenceConfig();

        // then
        assertTrue(persistenceConfig instanceof PersistenceConfig);
    }
}
