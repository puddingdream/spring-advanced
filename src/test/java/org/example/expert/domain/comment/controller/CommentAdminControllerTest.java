package org.example.expert.domain.comment.controller;

import org.example.expert.domain.comment.service.CommentAdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CommentAdminControllerTest {

    @Mock
    private CommentAdminService commentAdminService;
    @InjectMocks
    private CommentAdminController commentAdminController;

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void comment_삭제_요청_시_서비스를_호출한다() {
        // given
        long commentId = 1L;

        // when
        commentAdminController.deleteComment(commentId);

        // then
        then(commentAdminService).should().deleteComment(commentId);
    }
}
