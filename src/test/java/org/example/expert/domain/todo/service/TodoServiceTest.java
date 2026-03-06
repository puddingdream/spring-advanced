package org.example.expert.domain.todo.service;

import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.fixture.TodoFixture;
import org.example.expert.fixture.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;
    @Mock
    private WeatherClient weatherClient;
    @InjectMocks
    private TodoService todoService;

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void todo_등록에_성공한다() {
        // given
        AuthUser authUser = UserFixture.createAuthUser();
        TodoSaveRequest request = TodoFixture.createTodoSaveRequest();
        given(weatherClient.getTodayWeather()).willReturn("sunny");

        User user = User.fromAuthUser(authUser);
        Todo todo = new Todo(request.getTitle(), request.getContents(), "sunny", user);
        org.springframework.test.util.ReflectionTestUtils.setField(todo, "id", 1L);
        given(todoRepository.save(any(Todo.class))).willReturn(todo);

        // when
        TodoSaveResponse response = todoService.saveTodo(authUser, request);

        // then
        assertEquals(1L, response.getId());
        assertEquals(request.getTitle(), response.getTitle());
        assertEquals(request.getContents(), response.getContents());
        assertEquals("sunny", response.getWeather());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void todo_목록_조회에_성공한다() {
        // given
        User user = UserFixture.createUserWithId(1L);
        Todo todo = TodoFixture.createTodoWithTimestamps(1L, user);
        // PageImpl: Spring Data Page 인터페이스의 테스트용 구현체
        Page<Todo> page = new PageImpl<>(List.of(todo));
        given(todoRepository.findAllByOrderByModifiedAtDesc(any(Pageable.class))).willReturn(page);

        // when
        Page<TodoResponse> response = todoService.getTodos(1, 10);

        // then
        assertEquals(1, response.getTotalElements());
        assertEquals(todo.getTitle(), response.getContent().get(0).getTitle());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void todo_단건_조회_중_todo를_찾지_못해_에러가_발생한다() {
        // given
        long todoId = 1L;
        given(todoRepository.findByIdWithUser(todoId)).willReturn(Optional.empty());

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> todoService.getTodo(todoId));

        // then
        assertEquals("Todo not found", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void todo_단건_조회에_성공한다() {
        // given
        long todoId = 1L;
        User user = UserFixture.createUserWithId(1L);
        Todo todo = TodoFixture.createTodoWithTimestamps(todoId, user);
        given(todoRepository.findByIdWithUser(todoId)).willReturn(Optional.of(todo));

        // when
        TodoResponse response = todoService.getTodo(todoId);

        // then
        assertEquals(todoId, response.getId());
        assertEquals(todo.getTitle(), response.getTitle());
        assertEquals(user.getEmail(), response.getUser().getEmail());
    }
}
