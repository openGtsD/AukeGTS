// Copyright (c) 2012 Health Market Science, Inc.
package no.auke.drone.ws.support;

import org.springframework.beans.BeansException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.XmlWebApplicationContext;
/**
 * @author thaihuynh
 *
 */
public class XmlRestApplicationContext extends XmlWebApplicationContext {
    private boolean active;
    
    @Override
    protected Resource getResourceByPath(String path) {
        if (getServletContext() == null) {
            return new ClassPathResource(path);
        }
        
        return super.getResourceByPath(path);
    }
    
    @Override
    public synchronized void refresh() throws BeansException, IllegalStateException {
        if (!active) {
            super.refresh();
            active = true;
        }
    }
}
