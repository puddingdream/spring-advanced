package org.example.expert.domain.user.service;

import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.example.expert.fixture.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserAdminServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserAdminService userAdminService;

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void user_role_변경_중_유저를_찾지_못해_에러가_발생한다() {
        // given
        long userId = 1L;
        UserRoleChangeRequest request = UserFixture.createUserRoleChangeRequest("ADMIN");
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> userAdminService.changeUserRole(userId, request));

        // then
        assertEquals("User not found", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void user_role_요청값이_유효하지_않아_에러가_발생한다() {
        // given
        long userId = 1L;
        User user = UserFixture.createUserWithId(userId);
        UserRoleChangeRequest request = UserFixture.createUserRoleChangeRequest("NOT_VALID");
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> userAdminService.changeUserRole(userId, request));

        // then
        assertEquals("유효하지 않은 UerRole", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void user_role_변경에_성공한다() {
        // given
        long userId = 1L;
        User user = UserFixture.createUserWithId(userId);
        UserRoleChangeRequest request = UserFixture.createUserRoleChangeRequest("ADMIN");
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        userAdminService.changeUserRole(userId, request);

        // then
        assertEquals(UserRole.ADMIN, user.getUserRole());
    }
}
