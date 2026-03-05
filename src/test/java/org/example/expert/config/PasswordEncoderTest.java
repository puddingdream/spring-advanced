package org.example.expert.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
class PasswordEncoderTest {

    @InjectMocks
    private PasswordEncoder passwordEncoder;

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void matches_메서드가_정상적으로_동작한다() {
        // given
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // when
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);

        // then
        assertTrue(matches);
        assertNotEquals(rawPassword, encodedPassword);
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 다른_비밀번호는_matches가_false를_반환한다() {
        // given
        String rawPassword = "testPassword";
        String otherPassword = "otherPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // when
        boolean matches = passwordEncoder.matches(otherPassword, encodedPassword);

        // then
        assertFalse(matches);
    }
}
