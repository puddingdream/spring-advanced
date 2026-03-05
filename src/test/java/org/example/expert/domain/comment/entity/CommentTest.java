package org.example.expert.domain.comment.entity;

import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.example.expert.fixture.TodoFixture;
import org.example.expert.fixture.UserFixture;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentTest {

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void comment를_생성한다() {
        // given
        User user = UserFixture.createUser();
        Todo todo = TodoFixture.createTodo(user);

        // when
        Comment comment = new Comment("contents", user, todo);

        // then
        assertEquals("contents", comment.getContents());
        assertEquals(user, comment.getUser());
        assertEquals(todo, comment.getTodo());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void comment_내용을_수정한다() {
        // given
        User user = UserFixture.createUser();
        Todo todo = TodoFixture.createTodo(user);
        Comment comment = new Comment("before", user, todo);

        // when
        comment.update("after");

        // then
        assertEquals("after", comment.getContents());
    }
}
