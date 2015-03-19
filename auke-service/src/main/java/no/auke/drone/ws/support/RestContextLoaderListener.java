// Copyright (c) 2012 Health Market Science, Inc.
package no.auke.drone.ws.support;

import javax.servlet.ServletContext;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author thaihuynh
 *
 */
public class RestContextLoaderListener extends ContextLoaderListener {
    private final XmlRestApplicationContext appContext;
    
    public RestContextLoaderListener(XmlRestApplicationContext appContext) {
        super(appContext);
        
        this.appContext = appContext;
    }
    
    @Override
    protected WebApplicationContext createWebApplicationContext(ServletContext sc) {
        appContext.setServletContext(sc);
        
        return super.createWebApplicationContext(sc);
    }
}
