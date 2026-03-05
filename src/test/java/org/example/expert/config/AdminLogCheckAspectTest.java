package org.example.expert.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

class AdminLogCheckAspectTest {

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void aop_성공_시_응답을_그대로_반환한다() throws Throwable {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        AdminLogCheckAspect aspect = new AdminLogCheckAspect(objectMapper);
        ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);
        Signature signature = Mockito.mock(Signature.class);
        given(joinPoint.getArgs()).willReturn(new Object[]{"req"});
        given(joinPoint.getSignature()).willReturn(signature);
        given(signature.toShortString()).willReturn("AdminController.method()");
        given(joinPoint.proceed()).willReturn("ok");

        // when
        Object result = aspect.logAdminApi(joinPoint);

        // then
        assertEquals("ok", result);
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void aop_예외_발생_시_예외를_재던진다() throws Throwable {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        AdminLogCheckAspect aspect = new AdminLogCheckAspect(objectMapper);
        ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);
        Signature signature = Mockito.mock(Signature.class);
        given(joinPoint.getArgs()).willReturn(new Object[]{1L});
        given(joinPoint.getSignature()).willReturn(signature);
        given(signature.toShortString()).willReturn("AdminController.method()");
        given(joinPoint.proceed()).willThrow(new IllegalStateException("boom"));

        // when
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> aspect.logAdminApi(joinPoint));

        // then
        assertEquals("boom", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void json_직렬화_실패_상황에서도_aop는_동작한다() throws Throwable {
        // given
        ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
        given(objectMapper.writeValueAsString(Mockito.any())).willThrow(new JsonProcessingException("fail") {
        });
        AdminLogCheckAspect aspect = new AdminLogCheckAspect(objectMapper);
        ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);
        Signature signature = Mockito.mock(Signature.class);
        given(joinPoint.getArgs()).willReturn(new Object[]{new Object()});
        given(joinPoint.getSignature()).willReturn(signature);
        given(signature.toShortString()).willReturn("AdminController.method()");
        given(joinPoint.proceed()).willReturn("ok");

        // when
        Object result = aspect.logAdminApi(joinPoint);

        // then
        assertEquals("ok", result);
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void args가_null이어도_aop는_동작한다() throws Throwable {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        AdminLogCheckAspect aspect = new AdminLogCheckAspect(objectMapper);
        ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);
        Signature signature = Mockito.mock(Signature.class);
        given(joinPoint.getArgs()).willReturn(null);
        given(joinPoint.getSignature()).willReturn(signature);
        given(signature.toShortString()).willReturn("AdminController.method()");
        given(joinPoint.proceed()).willReturn("ok");

        // when
        Object result = aspect.logAdminApi(joinPoint);

        // then
        assertEquals("ok", result);
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void args가_비어있어도_aop는_동작한다() throws Throwable {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        AdminLogCheckAspect aspect = new AdminLogCheckAspect(objectMapper);
        ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);
        Signature signature = Mockito.mock(Signature.class);
        given(joinPoint.getArgs()).willReturn(new Object[0]);
        given(joinPoint.getSignature()).willReturn(signature);
        given(signature.toShortString()).willReturn("AdminController.method()");
        given(joinPoint.proceed()).willReturn("ok");

        // when
        Object result = aspect.logAdminApi(joinPoint);

        // then
        assertEquals("ok", result);
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void arg에_null이_포함되어도_aop는_동작한다() throws Throwable {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        AdminLogCheckAspect aspect = new AdminLogCheckAspect(objectMapper);
        ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);
        Signature signature = Mockito.mock(Signature.class);
        given(joinPoint.getArgs()).willReturn(new Object[]{null, new Object()});
        given(joinPoint.getSignature()).willReturn(signature);
        given(signature.toShortString()).willReturn("AdminController.method()");
        given(joinPoint.proceed()).willReturn("ok");

        // when
        Object result = aspect.logAdminApi(joinPoint);

        // then
        assertEquals("ok", result);
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void object가_null일때_json_직렬화_실패하면_fallback을_사용한다() throws Throwable {
        // given
        ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
        given(objectMapper.writeValueAsString(null)).willThrow(new JsonProcessingException("fail") {
        });
        AdminLogCheckAspect aspect = new AdminLogCheckAspect(objectMapper);
        ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);
        Signature signature = Mockito.mock(Signature.class);
        given(joinPoint.getArgs()).willReturn(null);
        given(joinPoint.getSignature()).willReturn(signature);
        given(signature.toShortString()).willReturn("AdminController.method()");
        given(joinPoint.proceed()).willReturn(null);

        // when
        Object result = aspect.logAdminApi(joinPoint);

        // then
        assertNull(result);
    }
}
