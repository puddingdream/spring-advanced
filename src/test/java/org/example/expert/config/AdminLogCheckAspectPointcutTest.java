package org.example.expert.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.domain.aoptest.controller.SampleAdminController;
import org.example.expert.domain.aoptest.controller.SampleController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

class AdminLogCheckAspectPointcutTest {

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void AdminController_패턴_메서드에만_AOP가_적용된다() throws Throwable {
        // given
        Runnable delegate = Mockito.mock(Runnable.class);
        AdminLogCheckAspect aspect = Mockito.spy(new AdminLogCheckAspect(new ObjectMapper()));

        SampleAdminController adminTarget = new SampleAdminController(delegate);
        AspectJProxyFactory adminFactory = new AspectJProxyFactory(adminTarget);
        adminFactory.addAspect(aspect);
        SampleAdminController adminProxy = adminFactory.getProxy();

        SampleController userTarget = new SampleController(delegate);
        AspectJProxyFactory userFactory = new AspectJProxyFactory(userTarget);
        userFactory.addAspect(aspect);
        SampleController userProxy = userFactory.getProxy();

        // when
        adminProxy.deleteSomething(1L);
        userProxy.getSomething(1L);

        // then
        then(delegate).should(times(2)).run();
        then(aspect).should(times(1)).logAdminApi(any());
    }
}
