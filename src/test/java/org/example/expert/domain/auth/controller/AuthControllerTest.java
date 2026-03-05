package org.example.expert.domain.auth.controller;

import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.auth.service.AuthService;
import org.example.expert.fixture.AuthFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;
    @InjectMocks
    private AuthController authController;

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 회원가입_요청_시_응답을_반환한다() {
        // given
        SignupRequest request = AuthFixture.createSignupRequest();
        SignupResponse response = new SignupResponse(AuthFixture.DEFAULT_TOKEN);
        given(authService.signup(request)).willReturn(response);

        // when
        SignupResponse result = authController.signup(request);

        // then
        assertEquals(AuthFixture.DEFAULT_TOKEN, result.getBearerToken());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 로그인_요청_시_응답을_반환한다() {
        // given
        SigninRequest request = AuthFixture.createSigninRequest();
        SigninResponse response = new SigninResponse(AuthFixture.DEFAULT_TOKEN);
        given(authService.signin(request)).willReturn(response);

        // when
        SigninResponse result = authController.signin(request);

        // then
        assertEquals(AuthFixture.DEFAULT_TOKEN, result.getBearerToken());
        then(authService).should().signin(request);
    }
}
