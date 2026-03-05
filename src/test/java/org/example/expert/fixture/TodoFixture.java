package org.example.expert.fixture;

import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

public class TodoFixture {

    public static final Long DEFAULT_TODO_ID = 1L;
    public static final String DEFAULT_TITLE = "title";
    public static final String DEFAULT_CONTENTS = "contents";
    public static final String DEFAULT_WEATHER = "sunny";

    private TodoFixture() {
    }

    public static Todo createTodo(User user) {
        return new Todo(DEFAULT_TITLE, DEFAULT_CONTENTS, DEFAULT_WEATHER, user);
    }

    public static Todo createTodoWithId(Long id, User user) {
        Todo todo = createTodo(user);
        ReflectionTestUtils.setField(todo, "id", id);
        return todo;
    }

    public static Todo createTodoWithTimestamps(Long id, User user) {
        Todo todo = createTodoWithId(id, user);
        ReflectionTestUtils.setField(todo, "createdAt", LocalDateTime.now().minusDays(1));
        ReflectionTestUtils.setField(todo, "modifiedAt", LocalDateTime.now());
        return todo;
    }

    public static TodoSaveRequest createTodoSaveRequest() {
        return new TodoSaveRequest(DEFAULT_TITLE, DEFAULT_CONTENTS);
    }
}
