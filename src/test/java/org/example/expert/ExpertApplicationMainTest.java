package org.example.expert;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

class ExpertApplicationMainTest {

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void main은_spring_application_run을_호출한다() {
        // given
        String[] args = new String[]{"--spring.profiles.active=test"};

        // when
        // MockedStatic: static 메서드(SpringApplication.run)를 검증하기 위한 Mockito 기능
        try (MockedStatic<SpringApplication> springApplicationMock = Mockito.mockStatic(SpringApplication.class)) {
            ExpertApplication.main(args);

            // then
            springApplicationMock.verify(() -> SpringApplication.run(ExpertApplication.class, args));
        }
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 기본_생성자로_애플리케이션_클래스를_생성할_수_있다() {
        // when
        ExpertApplication application = new ExpertApplication();

        // then
        org.junit.jupiter.api.Assertions.assertNotNull(application);
    }
}
