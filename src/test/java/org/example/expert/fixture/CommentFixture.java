package org.example.expert.fixture;

import org.example.expert.domain.comment.dto.request.CommentSaveRequest;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.springframework.test.util.ReflectionTestUtils;

public class CommentFixture {

    public static final Long DEFAULT_COMMENT_ID = 1L;
    public static final String DEFAULT_CONTENTS = "comment-contents";

    private CommentFixture() {
    }

    public static CommentSaveRequest createCommentSaveRequest() {
        return new CommentSaveRequest(DEFAULT_CONTENTS);
    }

    public static Comment createComment(User user, Todo todo) {
        return new Comment(DEFAULT_CONTENTS, user, todo);
    }

    public static Comment createCommentWithId(Long id, User user, Todo todo) {
        Comment comment = createComment(user, todo);
        ReflectionTestUtils.setField(comment, "id", id);
        return comment;
    }
}
