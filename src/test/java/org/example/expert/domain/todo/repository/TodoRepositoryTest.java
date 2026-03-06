package org.example.expert.domain.todo.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void findAllByOrderByModifiedAtDesc는_user를_즉시_로딩한다() {
        // given
        User user = userRepository.save(new User("user@example.com", "Password1", UserRole.USER));
        todoRepository.save(new Todo("title", "contents", "sunny", user));
        entityManager.flush();
        entityManager.clear();

        // when
        Page<Todo> page = todoRepository.findAllByOrderByModifiedAtDesc(PageRequest.of(0, 10));

        // then
        Todo todo = page.getContent().get(0);
        boolean loaded = entityManagerFactory.getPersistenceUnitUtil().isLoaded(todo, "user");
        assertThat(loaded).isTrue();
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void findAllByOrderByModifiedAtDesc_조회후_user_접근시_N플러스1_추가_쿼리가_발생하지_않는다() {
        // given
        User user = userRepository.save(new User("user2@example.com", "Password1", UserRole.USER));
        todoRepository.saveAll(List.of(
                new Todo("title1", "contents1", "sunny", user),
                new Todo("title2", "contents2", "sunny", user),
                new Todo("title3", "contents3", "sunny", user)
        ));
        entityManager.flush();
        entityManager.clear();

        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        Statistics statistics = sessionFactory.getStatistics();
        statistics.setStatisticsEnabled(true);
        statistics.clear();

        // when
        Page<Todo> page = todoRepository.findAllByOrderByModifiedAtDesc(PageRequest.of(0, 10));
        page.getContent().forEach(todo -> todo.getUser().getEmail());

        // then
        // pageable 조회는 content + count 쿼리 2개가 기본이며, user 접근으로 인한 추가 select가 없어야 한다.
        assertThat(statistics.getPrepareStatementCount()).isLessThanOrEqualTo(2L);
    }
}
