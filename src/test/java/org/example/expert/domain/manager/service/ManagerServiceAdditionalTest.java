package org.example.expert.domain.manager.service;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.example.expert.fixture.ManagerFixture;
import org.example.expert.fixture.TodoFixture;
import org.example.expert.fixture.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ManagerServiceAdditionalTest {

    @Mock
    private ManagerRepository managerRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TodoRepository todoRepository;
    @InjectMocks
    private ManagerService managerService;

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 담당자_등록_중_담당자_유저가_없으면_에러가_발생한다() {
        // given
        AuthUser authUser = new AuthUser(1L, "owner@example.com", UserRole.USER);
        User owner = User.fromAuthUser(authUser);
        Todo todo = TodoFixture.createTodoWithId(1L, owner);
        given(todoRepository.findById(1L)).willReturn(Optional.of(todo));
        given(userRepository.findById(ManagerFixture.DEFAULT_MANAGER_USER_ID)).willReturn(Optional.empty());

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class,
                () -> managerService.saveManager(authUser, 1L, ManagerFixture.createManagerSaveRequest()));

        // then
        assertEquals("등록하려고 하는 담당자 유저가 존재하지 않습니다.", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 담당자_등록_중_todo_작성자와_요청자가_다르면_에러가_발생한다() {
        // given
        AuthUser authUser = new AuthUser(1L, "requester@example.com", UserRole.USER);
        User todoOwner = UserFixture.createUserWithId(2L);
        Todo todo = TodoFixture.createTodoWithId(1L, todoOwner);
        given(todoRepository.findById(1L)).willReturn(Optional.of(todo));

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class,
                () -> managerService.saveManager(authUser, 1L, ManagerFixture.createManagerSaveRequest()));

        // then
        assertEquals("일정을 생성한 유저만 담당자를 지정할 수 있습니다.", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 담당자_등록_중_작성자_본인을_담당자로_등록하면_에러가_발생한다() {
        // given
        AuthUser authUser = new AuthUser(1L, "owner@example.com", UserRole.USER);
        User owner = User.fromAuthUser(authUser);
        Todo todo = TodoFixture.createTodoWithId(1L, owner);
        User sameUser = UserFixture.createUserWithId(1L);
        ReflectionTestUtils.setField(sameUser, "email", "owner@example.com");
        given(todoRepository.findById(1L)).willReturn(Optional.of(todo));
        given(userRepository.findById(1L)).willReturn(Optional.of(sameUser));

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class,
                () -> managerService.saveManager(authUser, 1L, new org.example.expert.domain.manager.dto.request.ManagerSaveRequest(1L)));

        // then
        assertEquals("일정 작성자는 본인을 담당자로 등록할 수 없습니다.", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 담당자_삭제_중_user를_찾지_못하면_에러가_발생한다() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class,
                () -> managerService.deleteManager(1L, 1L, 1L));

        // then
        assertEquals("User not found", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 담당자_삭제_중_todo를_찾지_못하면_에러가_발생한다() {
        // given
        User user = UserFixture.createUserWithId(1L);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(todoRepository.findById(1L)).willReturn(Optional.empty());

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class,
                () -> managerService.deleteManager(1L, 1L, 1L));

        // then
        assertEquals("Todo not found", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 담당자_삭제_중_todo_작성자가_요청자와_다르면_에러가_발생한다() {
        // given
        User requester = UserFixture.createUserWithId(1L);
        User owner = UserFixture.createUserWithId(2L);
        Todo todo = TodoFixture.createTodoWithId(1L, owner);

        given(userRepository.findById(1L)).willReturn(Optional.of(requester));
        given(todoRepository.findById(1L)).willReturn(Optional.of(todo));

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class,
                () -> managerService.deleteManager(1L, 1L, 1L));

        // then
        assertEquals("해당 일정을 만든 유저가 유효하지 않습니다.", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 담당자_삭제_중_todo_작성자가_null이면_에러가_발생한다() {
        // given
        User requester = UserFixture.createUserWithId(1L);
        Todo todo = new Todo();
        ReflectionTestUtils.setField(todo, "id", 1L);
        ReflectionTestUtils.setField(todo, "user", null);

        given(userRepository.findById(1L)).willReturn(Optional.of(requester));
        given(todoRepository.findById(1L)).willReturn(Optional.of(todo));

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class,
                () -> managerService.deleteManager(1L, 1L, 1L));

        // then
        assertEquals("해당 일정을 만든 유저가 유효하지 않습니다.", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 담당자_삭제_중_manager를_찾지_못하면_에러가_발생한다() {
        // given
        User owner = UserFixture.createUserWithId(1L);
        Todo todo = TodoFixture.createTodoWithId(1L, owner);

        given(userRepository.findById(1L)).willReturn(Optional.of(owner));
        given(todoRepository.findById(1L)).willReturn(Optional.of(todo));
        given(managerRepository.findById(1L)).willReturn(Optional.empty());

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class,
                () -> managerService.deleteManager(1L, 1L, 1L));

        // then
        assertEquals("Manager not found", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 담당자_삭제_중_다른_todo의_manager이면_에러가_발생한다() {
        // given
        User owner = UserFixture.createUserWithId(1L);
        Todo todo = TodoFixture.createTodoWithId(1L, owner);
        Todo otherTodo = TodoFixture.createTodoWithId(2L, owner);
        Manager manager = ManagerFixture.createManagerWithId(1L, owner, otherTodo);

        given(userRepository.findById(1L)).willReturn(Optional.of(owner));
        given(todoRepository.findById(1L)).willReturn(Optional.of(todo));
        given(managerRepository.findById(1L)).willReturn(Optional.of(manager));

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class,
                () -> managerService.deleteManager(1L, 1L, 1L));

        // then
        assertEquals("해당 일정에 등록된 담당자가 아닙니다.", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 담당자_삭제에_성공한다() {
        // given
        User owner = UserFixture.createUserWithId(1L);
        Todo todo = TodoFixture.createTodoWithId(1L, owner);
        Manager manager = ManagerFixture.createManagerWithId(1L, owner, todo);

        given(userRepository.findById(1L)).willReturn(Optional.of(owner));
        given(todoRepository.findById(1L)).willReturn(Optional.of(todo));
        given(managerRepository.findById(1L)).willReturn(Optional.of(manager));

        // when
        managerService.deleteManager(1L, 1L, 1L);

        // then
        then(managerRepository).should().delete(manager);
    }
}
