package org.example.expert.domain.manager.entity;

import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.example.expert.fixture.TodoFixture;
import org.example.expert.fixture.UserFixture;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ManagerTest {

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void manager를_생성한다() {
        // given
        User user = UserFixture.createUser();
        Todo todo = TodoFixture.createTodo(user);

        // when
        Manager manager = new Manager(user, todo);

        // then
        assertEquals(user, manager.getUser());
        assertEquals(todo, manager.getTodo());
    }
}
