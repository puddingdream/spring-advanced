package org.example.expert.domain.user.controller;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.service.UserService;
import org.example.expert.fixture.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void user_조회_요청_시_응답을_반환한다() {
        // given
        long userId = 1L;
        UserResponse userResponse = new UserResponse(userId, UserFixture.DEFAULT_EMAIL);
        given(userService.getUser(userId)).willReturn(userResponse);

        // when
        ResponseEntity<UserResponse> response = userController.getUser(userId);

        // then
        assertEquals(200, response.getStatusCode().value());
        assertEquals(userId, response.getBody().getId());
        assertEquals(UserFixture.DEFAULT_EMAIL, response.getBody().getEmail());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void user_비밀번호_변경_요청_시_서비스를_호출한다() {
        // given
        AuthUser authUser = UserFixture.createAuthUser();
        UserChangePasswordRequest request = UserFixture.createUserChangePasswordRequest();

        // when
        userController.changePassword(authUser, request);

        // then
        then(userService).should().changePassword(authUser.getId(), request);
    }
}
