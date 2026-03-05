package org.example.expert.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AdminLogCheckAspect {

    private final ObjectMapper objectMapper;

    // domain 하위의 모든 *AdminController 클래스 메서드를 대상으로 AOP 적용
    @Around("execution(* org.example.expert.domain..controller.*AdminController.*(..))")
    public Object logAdminApi(ProceedingJoinPoint joinPoint) throws Throwable {
        // 컨트롤러 인자 중 실제 요청 DTO(@RequestBody)를 추출
        Object requestBody = extractRequestBody(joinPoint.getArgs());

        // 요청 로그: RequestBody를 JSON으로 기록
        log.info(
                "[ADMIN_API_REQUEST] method={}, requestBody={}",
                joinPoint.getSignature().toShortString(),
                toJson(requestBody)
        );

        try {
            // 실제 컨트롤러 메서드 실행
            Object responseBody = joinPoint.proceed();
            // 응답 로그: ResponseBody를 JSON으로 기록
            log.info(
                    "[ADMIN_API_RESPONSE] method={}, responseBody={}",
                    joinPoint.getSignature().toShortString(),
                    toJson(responseBody)
            );
            return responseBody;
        } catch (Throwable throwable) {
            // 예외가 발생해도 로그를 남긴 뒤 다시 던져 기존 예외 처리 흐름 유지
            log.error(
                    "[ADMIN_API_ERROR] method={}, message={}",
                    joinPoint.getSignature().toShortString(),
                    throwable.getMessage(),
                    throwable
            );
            throw throwable;
        }
    }

    private Object extractRequestBody(Object[] args) {
        if (args == null || args.length == 0) {
            return null;
        }

        for (Object arg : args) {
            // PathVariable, primitive, wrapper, String 등 단순 타입은 제외
            if (arg == null || BeanUtils.isSimpleValueType(arg.getClass())) {
                continue;
            }
            // 첫 번째 복합 타입 객체를 요청 본문으로 간주
            return arg;
        }
        return null;
    }

    private String toJson(Object object) {
        try {
            // 객체를 JSON 문자열로 변환해서 로그에 기록
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            // 직렬화 실패 시에도 API 흐름은 깨지지 않게 fallback 문자열 사용
            log.warn("JSON 직렬화 실패. type={}", object == null ? "null" : object.getClass().getName(), e);
            return "\"<json_serialize_failed>\"";
        }
    }
}
