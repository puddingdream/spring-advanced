package org.example.expert.domain.user.enums;

import org.example.expert.domain.common.exception.InvalidRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserRoleTest {

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 문자열_role을_user_role로_변환한다() {
        // given
        String role = "admin";

        // when
        UserRole userRole = UserRole.of(role);

        // then
        assertEquals(UserRole.ADMIN, userRole);
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 유효하지_않은_role이면_에러가_발생한다() {
        // given
        String role = "invalid";

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> UserRole.of(role));

        // then
        assertEquals("유효하지 않은 UerRole", exception.getMessage());
    }
}
