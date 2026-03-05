package org.example.expert.fixture;

import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.springframework.test.util.ReflectionTestUtils;

public class ManagerFixture {

    public static final Long DEFAULT_MANAGER_ID = 1L;
    public static final Long DEFAULT_MANAGER_USER_ID = 2L;

    private ManagerFixture() {
    }

    public static ManagerSaveRequest createManagerSaveRequest() {
        return new ManagerSaveRequest(DEFAULT_MANAGER_USER_ID);
    }

    public static Manager createManager(User user, Todo todo) {
        return new Manager(user, todo);
    }

    public static Manager createManagerWithId(Long id, User user, Todo todo) {
        Manager manager = createManager(user, todo);
        ReflectionTestUtils.setField(manager, "id", id);
        return manager;
    }
}
