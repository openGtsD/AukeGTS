package no.auke.drone.ws.support;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;

/**
 * @author thaihuynh
 *
 */
public class ContextLoaderListener extends org.springframework.web.context.ContextLoaderListener {
    private final RestApplicationContext appContext;
    
    public ContextLoaderListener(RestApplicationContext appContext) {
        super(appContext);
        
        this.appContext = appContext;
    }
    
    @Override
    protected WebApplicationContext createWebApplicationContext(ServletContext sc) {
        appContext.setServletContext(sc);
        
        return super.createWebApplicationContext(sc);
    }
}
