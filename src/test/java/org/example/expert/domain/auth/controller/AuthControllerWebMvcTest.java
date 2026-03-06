package org.example.expert.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.config.GlobalExceptionHandler;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.auth.service.AuthService;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(GlobalExceptionHandler.class)
class AuthControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 회원가입_요청은_json_응답과_상태코드를_반환한다() throws Exception {
        // given
        SignupRequest request = new SignupRequest("user@example.com", "Password1", "USER");
        given(authService.signup(any(SignupRequest.class))).willReturn(new SignupResponse("Bearer token"));

        // when & then
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bearerToken").value("Bearer token"));
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 회원가입_요청시_비밀번호_검증_실패면_400을_반환한다() throws Exception {
        // given
        SignupRequest invalidRequest = new SignupRequest("user@example.com", "short", "USER");

        // when & then
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        then(authService).should(never()).signup(any(SignupRequest.class));
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 회원가입_중_서비스_예외가_발생하면_예외_응답을_반환한다() throws Exception {
        // given
        SignupRequest request = new SignupRequest("user@example.com", "Password1", "USER");
        given(authService.signup(any(SignupRequest.class))).willThrow(new InvalidRequestException("중복 이메일입니다."));

        // when & then
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("중복 이메일입니다."));
    }
}
