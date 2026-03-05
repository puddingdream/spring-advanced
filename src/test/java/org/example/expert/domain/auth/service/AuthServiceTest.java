package org.example.expert.domain.auth.service;

import org.example.expert.config.JwtUtil;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.example.expert.fixture.AuthFixture;
import org.example.expert.fixture.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private AuthService authService;

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 회원가입_중_이미_존재하는_이메일이면_에러가_발생한다() {
        // given
        SignupRequest request = AuthFixture.createSignupRequest();
        given(userRepository.existsByEmail(request.getEmail())).willReturn(true);

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> authService.signup(request));

        // then
        assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 회원가입_중_role이_유효하지_않으면_에러가_발생한다() {
        // given
        SignupRequest request = new SignupRequest(AuthFixture.DEFAULT_EMAIL, AuthFixture.DEFAULT_PASSWORD, "INVALID");
        given(userRepository.existsByEmail(request.getEmail())).willReturn(false);
        given(passwordEncoder.encode(request.getPassword())).willReturn("encoded");

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> authService.signup(request));

        // then
        assertEquals("유효하지 않은 UerRole", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 회원가입에_성공한다() {
        // given
        SignupRequest request = AuthFixture.createSignupRequest();
        User savedUser = new User(request.getEmail(), "encoded", UserRole.USER);
        ReflectionTestUtils.setField(savedUser, "id", 1L);

        given(userRepository.existsByEmail(request.getEmail())).willReturn(false);
        given(passwordEncoder.encode(request.getPassword())).willReturn("encoded");
        given(userRepository.save(any(User.class))).willReturn(savedUser);
        given(jwtUtil.createToken(anyLong(), anyString(), any(UserRole.class))).willReturn(AuthFixture.DEFAULT_TOKEN);

        // when
        SignupResponse response = authService.signup(request);

        // then
        assertEquals(AuthFixture.DEFAULT_TOKEN, response.getBearerToken());
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        then(userRepository).should().save(captor.capture());
        assertEquals(request.getEmail(), captor.getValue().getEmail());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 로그인_중_가입되지_않은_유저면_에러가_발생한다() {
        // given
        SigninRequest request = AuthFixture.createSigninRequest();
        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.empty());

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> authService.signin(request));

        // then
        assertEquals("가입되지 않은 유저입니다.", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 로그인_중_비밀번호가_틀리면_에러가_발생한다() {
        // given
        SigninRequest request = AuthFixture.createSigninRequest();
        User user = UserFixture.createUserWithId(1L);
        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getPassword(), user.getPassword())).willReturn(false);

        // when
        AuthException exception = assertThrows(AuthException.class, () -> authService.signin(request));

        // then
        assertEquals("잘못된 비밀번호입니다.", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 로그인에_성공한다() {
        // given
        SigninRequest request = AuthFixture.createSigninRequest();
        User user = UserFixture.createUserWithId(1L);
        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getPassword(), user.getPassword())).willReturn(true);
        given(jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole())).willReturn(AuthFixture.DEFAULT_TOKEN);

        // when
        SigninResponse response = authService.signin(request);

        // then
        assertEquals(AuthFixture.DEFAULT_TOKEN, response.getBearerToken());
    }
}
