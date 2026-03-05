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
        try (MockedStatic<SpringApplication> springApplicationMock = Mockito.mockStatic(SpringApplication.class)) {
            ExpertApplication.main(args);

            // then
            springApplicationMock.verify(() -> SpringApplication.run(ExpertApplication.class, args));
        }
    }
}
