package org.example.expert.domain.user.controller;

import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.service.UserAdminService;
import org.example.expert.fixture.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserAdminControllerTest {

    @Mock
    private UserAdminService userAdminService;
    @InjectMocks
    private UserAdminController userAdminController;

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void user_role_변경_요청_시_서비스를_호출한다() {
        // given
        long userId = 1L;
        UserRoleChangeRequest request = UserFixture.createUserRoleChangeRequest("ADMIN");

        // when
        userAdminController.changeUserRole(userId, request);

        // then
        then(userAdminService).should().changeUserRole(userId, request);
    }
}
