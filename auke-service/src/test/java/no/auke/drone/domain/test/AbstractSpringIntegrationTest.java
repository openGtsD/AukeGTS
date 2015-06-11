package no.auke.drone.domain.test;

/**
 * Created by huyduong on 6/9/2015.
 */

import org.junit.Before;
import org.junit.Ignore;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.HandlerMapping;

@Ignore
public class AbstractSpringIntegrationTest extends AbstractIntegrationTest {

    protected MockHttpServletRequest mockHttpServletRequest;
    protected MockHttpServletResponse mockHttpServletResponse;

    @Before
    public final void init() {
        mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setAttribute(HandlerMapping.INTROSPECT_TYPE_LEVEL_MAPPING, true);
        mockHttpServletResponse = new MockHttpServletResponse();

    }

}
