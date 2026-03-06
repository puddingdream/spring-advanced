package org.example.expert.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.config.GlobalExceptionHandler;
import org.example.expert.config.WebMvcConfig;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import({WebMvcConfig.class, GlobalExceptionHandler.class})
class UserControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 비밀번호_변경_요청시_Auth_리졸버로_주입된_userId를_사용한다() throws Exception {
        // given
        UserChangePasswordRequest request = new UserChangePasswordRequest("OldPassword1", "NewPassword1");

        // when & then
        mockMvc.perform(put("/users")
                        .requestAttr("userId", 7L)
                        .requestAttr("email", "user@example.com")
                        .requestAttr("userRole", "USER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        ArgumentCaptor<UserChangePasswordRequest> captor = ArgumentCaptor.forClass(UserChangePasswordRequest.class);
        then(userService).should().changePassword(eq(7L), captor.capture());
        assertEquals("OldPassword1", captor.getValue().getOldPassword());
        assertEquals("NewPassword1", captor.getValue().getNewPassword());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 비밀번호_변경_요청시_입력_검증_실패면_400을_반환한다() throws Exception {
        // given
        UserChangePasswordRequest invalidRequest = new UserChangePasswordRequest("OldPassword1", "short");

        // when & then
        mockMvc.perform(put("/users")
                        .requestAttr("userId", 7L)
                        .requestAttr("email", "user@example.com")
                        .requestAttr("userRole", "USER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다."));

        then(userService).should(never()).changePassword(eq(7L), any(UserChangePasswordRequest.class));
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 비밀번호_변경_요청시_Auth_정보가_없으면_400을_반환한다() throws Exception {
        // given
        UserChangePasswordRequest request = new UserChangePasswordRequest("OldPassword1", "NewPassword1");

        // when & then
        mockMvc.perform(put("/users")
                        .requestAttr("userId", 7L)
                        .requestAttr("email", "user@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("유효하지 않은 UerRole"));

        then(userService).should(never()).changePassword(eq(7L), any(UserChangePasswordRequest.class));
    }
}
