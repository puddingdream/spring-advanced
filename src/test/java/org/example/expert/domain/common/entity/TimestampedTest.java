package org.example.expert.domain.common.entity;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimestampedTest {

    private static class TimestampedStub extends Timestamped {
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void timestamped_created_at_modified_at_값을_조회한다() {
        // given
        TimestampedStub stub = new TimestampedStub();
        LocalDateTime createdAt = LocalDateTime.now().minusHours(1);
        LocalDateTime modifiedAt = LocalDateTime.now();
        ReflectionTestUtils.setField(stub, "createdAt", createdAt);
        ReflectionTestUtils.setField(stub, "modifiedAt", modifiedAt);

        // when
        LocalDateTime created = stub.getCreatedAt();
        LocalDateTime modified = stub.getModifiedAt();

        // then
        assertEquals(createdAt, created);
        assertEquals(modifiedAt, modified);
    }
}
