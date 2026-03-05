package org.example.expert.domain.comment.controller;

import org.example.expert.domain.comment.dto.request.CommentSaveRequest;
import org.example.expert.domain.comment.dto.response.CommentResponse;
import org.example.expert.domain.comment.dto.response.CommentSaveResponse;
import org.example.expert.domain.comment.service.CommentService;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.fixture.CommentFixture;
import org.example.expert.fixture.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock
    private CommentService commentService;
    @InjectMocks
    private CommentController commentController;

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void comment_등록_요청_시_응답을_반환한다() {
        // given
        AuthUser authUser = UserFixture.createAuthUser();
        long todoId = 1L;
        CommentSaveRequest request = CommentFixture.createCommentSaveRequest();
        CommentSaveResponse response = new CommentSaveResponse(1L, request.getContents(), new UserResponse(1L, UserFixture.DEFAULT_EMAIL));
        given(commentService.saveComment(authUser, todoId, request)).willReturn(response);

        // when
        ResponseEntity<CommentSaveResponse> result = commentController.saveComment(authUser, todoId, request);

        // then
        assertEquals(200, result.getStatusCode().value());
        assertEquals(request.getContents(), result.getBody().getContents());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void comment_목록_조회_요청_시_응답을_반환한다() {
        // given
        long todoId = 1L;
        List<CommentResponse> response = List.of(new CommentResponse(1L, "c1", new UserResponse(1L, UserFixture.DEFAULT_EMAIL)));
        given(commentService.getComments(todoId)).willReturn(response);

        // when
        ResponseEntity<List<CommentResponse>> result = commentController.getComments(todoId);

        // then
        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, result.getBody().size());
    }
}
