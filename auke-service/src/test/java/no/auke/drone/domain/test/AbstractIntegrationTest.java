package no.auke.drone.domain.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by huyduong on 4/12/2015.
 */
@Ignore
@ContextConfiguration("/test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class AbstractIntegrationTest implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public ApplicationContext getApplicationContext() {
        if (applicationContext == null)
            throw new IllegalStateException("ApplicationContext has not yet been initialized");

        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Before
    public final void injectDependencies() {
        getApplicationContext().getAutowireCapableBeanFactory().autowireBean(this);
        initMocks(this);
    }

    @After
    public final void cleanUp() throws Exception {
        // do nothing now
    }

    public void authenticate(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
