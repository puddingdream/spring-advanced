package org.example.expert.domain.common;

import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.entity.Timestamped;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.common.exception.ServerException;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonDomainTest {

    private static class TimestampedTestEntity extends Timestamped {
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void auth_user를_생성한다() {
        // given
        Long userId = 1L;
        String email = "user@example.com";

        // when
        AuthUser authUser = new AuthUser(userId, email, UserRole.USER);

        // then
        assertEquals(userId, authUser.getId());
        assertEquals(email, authUser.getEmail());
        assertEquals(UserRole.USER, authUser.getUserRole());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void invalid_request_exception_메시지를_보관한다() {
        // given
        String message = "invalid";

        // when
        InvalidRequestException exception = new InvalidRequestException(message);

        // then
        assertEquals(message, exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void server_exception_메시지를_보관한다() {
        // given
        String message = "server";

        // when
        ServerException exception = new ServerException(message);

        // then
        assertEquals(message, exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void auth_어노테이션_target과_retention이_정의되어있다() {
        // given
        Target target = Auth.class.getAnnotation(Target.class);
        Retention retention = Auth.class.getAnnotation(Retention.class);

        // when
        ElementType[] elementTypes = target.value();
        RetentionPolicy retentionPolicy = retention.value();

        // then
        assertEquals(1, elementTypes.length);
        assertEquals(ElementType.PARAMETER, elementTypes[0]);
        assertEquals(RetentionPolicy.RUNTIME, retentionPolicy);
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void timestamped_created_at_modified_at_getter가_동작한다() {
        // given
        TimestampedTestEntity entity = new TimestampedTestEntity();
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime modifiedAt = LocalDateTime.now();
        ReflectionTestUtils.setField(entity, "createdAt", createdAt);
        ReflectionTestUtils.setField(entity, "modifiedAt", modifiedAt);

        // when
        LocalDateTime created = entity.getCreatedAt();
        LocalDateTime modified = entity.getModifiedAt();

        // then
        assertEquals(createdAt, created);
        assertEquals(modifiedAt, modified);
        assertTrue(modified.isAfter(created));
    }
}
