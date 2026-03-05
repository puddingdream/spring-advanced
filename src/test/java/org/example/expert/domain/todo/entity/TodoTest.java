package org.example.expert.domain.todo.entity;

import org.example.expert.domain.user.entity.User;
import org.example.expert.fixture.UserFixture;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TodoTest {

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void todo를_생성하면_작성자가_manager로_자동_등록된다() {
        // given
        User user = UserFixture.createUser();

        // when
        Todo todo = new Todo("title", "contents", "sunny", user);

        // then
        assertEquals("title", todo.getTitle());
        assertEquals(user, todo.getUser());
        assertEquals(1, todo.getManagers().size());
        assertEquals(user, todo.getManagers().get(0).getUser());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void todo_내용을_수정한다() {
        // given
        User user = UserFixture.createUser();
        Todo todo = new Todo("before-title", "before-contents", "sunny", user);

        // when
        todo.update("after-title", "after-contents");

        // then
        assertEquals("after-title", todo.getTitle());
        assertEquals("after-contents", todo.getContents());
    }
}
