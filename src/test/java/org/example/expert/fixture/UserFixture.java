package org.example.expert.fixture;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.test.util.ReflectionTestUtils;

public class UserFixture {

    public static final Long DEFAULT_USER_ID = 1L;
    public static final String DEFAULT_EMAIL = "user1@example.com";
    public static final String DEFAULT_PASSWORD = "password";
    public static final String DEFAULT_OLD_PASSWORD = "OldPassword1";
    public static final String DEFAULT_NEW_PASSWORD = "NewPassword1";

    private UserFixture() {
    }

    public static User createUser() {
        return new User(DEFAULT_EMAIL, DEFAULT_PASSWORD, UserRole.USER);
    }

    public static User createUserWithId(Long id) {
        User user = createUser();
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    public static AuthUser createAuthUser() {
        return new AuthUser(DEFAULT_USER_ID, DEFAULT_EMAIL, UserRole.USER);
    }

    public static UserChangePasswordRequest createUserChangePasswordRequest() {
        return new UserChangePasswordRequest(DEFAULT_OLD_PASSWORD, DEFAULT_NEW_PASSWORD);
    }

    public static UserRoleChangeRequest createUserRoleChangeRequest(String role) {
        return new UserRoleChangeRequest(role);
    }
}
