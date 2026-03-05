package org.example.expert.domain.user.entity;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.fixture.UserFixture;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void auth_user로_user를_생성한다() {
        // given
        AuthUser authUser = UserFixture.createAuthUser();

        // when
        User user = User.fromAuthUser(authUser);

        // then
        assertEquals(authUser.getId(), user.getId());
        assertEquals(authUser.getEmail(), user.getEmail());
        assertEquals(authUser.getUserRole(), user.getUserRole());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void user_비밀번호를_변경한다() {
        // given
        User user = UserFixture.createUser();
        String changedPassword = "changed-password";

        // when
        user.changePassword(changedPassword);

        // then
        assertEquals(changedPassword, user.getPassword());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void user_role을_변경한다() {
        // given
        User user = UserFixture.createUser();

        // when
        user.updateRole(UserRole.ADMIN);

        // then
        assertEquals(UserRole.ADMIN, user.getUserRole());
    }
}
