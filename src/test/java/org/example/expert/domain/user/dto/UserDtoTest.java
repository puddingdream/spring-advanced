package org.example.expert.domain.user.dto;

import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.dto.response.UserSaveResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDtoTest {

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void user_change_password_request를_생성한다() {
        // given
        String oldPassword = "OldPassword1";
        String newPassword = "NewPassword1";

        // when
        UserChangePasswordRequest request = new UserChangePasswordRequest(oldPassword, newPassword);

        // then
        assertEquals(oldPassword, request.getOldPassword());
        assertEquals(newPassword, request.getNewPassword());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void user_role_change_request를_생성한다() {
        // given
        String role = "ADMIN";

        // when
        UserRoleChangeRequest request = new UserRoleChangeRequest(role);

        // then
        assertEquals(role, request.getRole());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void user_save_response를_생성한다() {
        // given
        String bearerToken = "Bearer token";

        // when
        UserSaveResponse response = new UserSaveResponse(bearerToken);

        // then
        assertEquals(bearerToken, response.getBearerToken());
    }
}
