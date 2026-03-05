package org.example.expert.domain.todo.controller;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.service.TodoService;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.fixture.TodoFixture;
import org.example.expert.fixture.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TodoControllerTest {

    @Mock
    private TodoService todoService;
    @InjectMocks
    private TodoController todoController;

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void todo_등록_요청_시_응답을_반환한다() {
        // given
        AuthUser authUser = UserFixture.createAuthUser();
        TodoSaveRequest request = TodoFixture.createTodoSaveRequest();
        TodoSaveResponse response = new TodoSaveResponse(1L, request.getTitle(), request.getContents(), "sunny", new UserResponse(1L, UserFixture.DEFAULT_EMAIL));
        given(todoService.saveTodo(authUser, request)).willReturn(response);

        // when
        ResponseEntity<TodoSaveResponse> result = todoController.saveTodo(authUser, request);

        // then
        assertEquals(200, result.getStatusCode().value());
        assertEquals(1L, result.getBody().getId());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void todo_목록_조회_요청_시_응답을_반환한다() {
        // given
        TodoResponse item = new TodoResponse(1L, "title", "contents", "sunny", new UserResponse(1L, "u@u.com"), LocalDateTime.now(), LocalDateTime.now());
        Page<TodoResponse> page = new PageImpl<>(List.of(item));
        given(todoService.getTodos(1, 10)).willReturn(page);

        // when
        ResponseEntity<Page<TodoResponse>> result = todoController.getTodos(1, 10);

        // then
        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, result.getBody().getTotalElements());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void todo_단건_조회_요청_시_응답을_반환한다() {
        // given
        TodoResponse response = new TodoResponse(1L, "title", "contents", "sunny", new UserResponse(1L, "u@u.com"), LocalDateTime.now(), LocalDateTime.now());
        given(todoService.getTodo(1L)).willReturn(response);

        // when
        ResponseEntity<TodoResponse> result = todoController.getTodo(1L);

        // then
        assertEquals(200, result.getStatusCode().value());
        assertEquals(1L, result.getBody().getId());
    }
}
