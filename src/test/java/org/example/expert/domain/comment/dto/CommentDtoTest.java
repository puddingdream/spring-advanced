package org.example.expert.domain.comment.dto;

import org.example.expert.domain.comment.dto.request.CommentSaveRequest;
import org.example.expert.domain.comment.dto.response.CommentResponse;
import org.example.expert.domain.comment.dto.response.CommentSaveResponse;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentDtoTest {

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void comment_save_request를_생성한다() {
        // given
        String contents = "comment";

        // when
        CommentSaveRequest request = new CommentSaveRequest(contents);

        // then
        assertEquals(contents, request.getContents());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void comment_response를_생성한다() {
        // given
        UserResponse user = new UserResponse(1L, "user@example.com");

        // when
        CommentResponse response = new CommentResponse(1L, "comment", user);

        // then
        assertEquals(1L, response.getId());
        assertEquals("comment", response.getContents());
        assertEquals("user@example.com", response.getUser().getEmail());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void comment_save_response를_생성한다() {
        // given
        UserResponse user = new UserResponse(1L, "user@example.com");

        // when
        CommentSaveResponse response = new CommentSaveResponse(1L, "comment", user);

        // then
        assertEquals(1L, response.getId());
        assertEquals("comment", response.getContents());
        assertEquals("user@example.com", response.getUser().getEmail());
    }
}
