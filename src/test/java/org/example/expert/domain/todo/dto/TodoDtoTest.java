package org.example.expert.domain.todo.dto;

import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TodoDtoTest {

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void todo_save_request를_생성한다() {
        // given
        String title = "title";
        String contents = "contents";

        // when
        TodoSaveRequest request = new TodoSaveRequest(title, contents);

        // then
        assertEquals(title, request.getTitle());
        assertEquals(contents, request.getContents());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void todo_save_response를_생성한다() {
        // given
        UserResponse user = new UserResponse(1L, "user@example.com");

        // when
        TodoSaveResponse response = new TodoSaveResponse(1L, "title", "contents", "sunny", user);

        // then
        assertEquals(1L, response.getId());
        assertEquals("title", response.getTitle());
        assertEquals("user@example.com", response.getUser().getEmail());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void todo_response를_생성한다() {
        // given
        UserResponse user = new UserResponse(1L, "user@example.com");
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime modifiedAt = LocalDateTime.now();

        // when
        TodoResponse response = new TodoResponse(1L, "title", "contents", "sunny", user, createdAt, modifiedAt);

        // then
        assertEquals(1L, response.getId());
        assertEquals("title", response.getTitle());
        assertEquals(createdAt, response.getCreatedAt());
        assertEquals(modifiedAt, response.getModifiedAt());
    }
}
