package org.example.expert.domain.manager.controller;


import io.jsonwebtoken.Claims;
import org.example.expert.config.JwtUtil;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.manager.service.ManagerService;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.fixture.ManagerFixture;
import org.example.expert.fixture.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ManagerControllerTest {

    @Mock
    private ManagerService managerService;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private ManagerController managerController;

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void manager_등록_요청_시_응답을_반환한다() {
        // given
        AuthUser authUser = UserFixture.createAuthUser();
        long todoId = 1L;
        ManagerSaveRequest request = ManagerFixture.createManagerSaveRequest();
        ManagerSaveResponse response = new ManagerSaveResponse(1L, new UserResponse(2L, "manager@example.com"));
        given(managerService.saveManager(authUser, todoId, request)).willReturn(response);

        // when
        ResponseEntity<ManagerSaveResponse> result = managerController.saveManager(authUser, todoId, request);

        // then
        assertEquals(200, result.getStatusCode().value());
        assertEquals(1L, result.getBody().getId());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void manager_목록_조회_요청_시_응답을_반환한다() {
        // given
        long todoId = 1L;
        given(managerService.getManagers(todoId)).willReturn(List.of(new ManagerResponse(1L, new UserResponse(2L, "manager@example.com"))));

        // when
        ResponseEntity<List<ManagerResponse>> result = managerController.getMembers(todoId);

        // then
        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, result.getBody().size());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void manager_삭제_요청_시_토큰에서_user_id를_추출해_서비스를_호출한다() {
        // given
        Claims claims = org.mockito.Mockito.mock(Claims.class);
        given(jwtUtil.extractClaims(anyString())).willReturn(claims);
        given(claims.getSubject()).willReturn("1");
        String bearerToken = "Bearer sample-token";

        // when
        managerController.deleteManager(bearerToken, 10L, 20L);

        // then
        then(managerService).should().deleteManager(1L, 10L, 20L);
    }
}
