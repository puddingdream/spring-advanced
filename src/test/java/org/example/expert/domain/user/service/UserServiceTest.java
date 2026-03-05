package org.example.expert.domain.user.service;

import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void user_조회_중_유저를_찾지_못해_에러가_발생한다() {
        // given
        long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> userService.getUser(userId));

        // then
        assertEquals("User not found", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void user_조회에_성공한다() {
        // given
        long userId = 1L;
        User user = UserFixture.createUserWithId(userId);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        UserResponse response = userService.getUser(userId);

        // then
        assertEquals(userId, response.getId());
        assertEquals(UserFixture.DEFAULT_EMAIL, response.getEmail());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 비밀번호_변경_중_유저를_찾지_못해_에러가_발생한다() {
        // given
        long userId = 1L;
        UserChangePasswordRequest request = UserFixture.createUserChangePasswordRequest();
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> userService.changePassword(userId, request));

        // then
        assertEquals("User not found", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 새_비밀번호가_기존_비밀번호와_같으면_에러가_발생한다() {
        // given
        long userId = 1L;
        User user = UserFixture.createUserWithId(userId);
        UserChangePasswordRequest request = UserFixture.createUserChangePasswordRequest();

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getNewPassword(), user.getPassword())).willReturn(true);

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> userService.changePassword(userId, request));

        // then
        assertEquals("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 기존_비밀번호가_일치하지_않으면_에러가_발생한다() {
        // given
        long userId = 1L;
        User user = UserFixture.createUserWithId(userId);
        UserChangePasswordRequest request = UserFixture.createUserChangePasswordRequest();

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getNewPassword(), user.getPassword())).willReturn(false);
        given(passwordEncoder.matches(request.getOldPassword(), user.getPassword())).willReturn(false);

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> userService.changePassword(userId, request));

        // then
        assertEquals("잘못된 비밀번호입니다.", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 비밀번호_변경에_성공한다() {
        // given
        long userId = 1L;
        User user = UserFixture.createUserWithId(userId);
        UserChangePasswordRequest request = UserFixture.createUserChangePasswordRequest();

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getNewPassword(), user.getPassword())).willReturn(false);
        given(passwordEncoder.matches(request.getOldPassword(), user.getPassword())).willReturn(true);
        given(passwordEncoder.encode(request.getNewPassword())).willReturn("encoded-new-password");

        // when
        userService.changePassword(userId, request);

        // then
        assertEquals("encoded-new-password", user.getPassword());
    }
}
