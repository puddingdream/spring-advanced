package org.example.expert.domain.manager.dto;

import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ManagerDtoTest {

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void manager_save_request를_생성한다() {
        // given
        long managerUserId = 2L;

        // when
        ManagerSaveRequest request = new ManagerSaveRequest(managerUserId);

        // then
        assertEquals(managerUserId, request.getManagerUserId());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void manager_response를_생성한다() {
        // given
        UserResponse user = new UserResponse(2L, "manager@example.com");

        // when
        ManagerResponse response = new ManagerResponse(1L, user);

        // then
        assertEquals(1L, response.getId());
        assertEquals("manager@example.com", response.getUser().getEmail());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void manager_save_response를_생성한다() {
        // given
        UserResponse user = new UserResponse(2L, "manager@example.com");

        // when
        ManagerSaveResponse response = new ManagerSaveResponse(1L, user);

        // then
        assertEquals(1L, response.getId());
        assertEquals("manager@example.com", response.getUser().getEmail());
    }
}
