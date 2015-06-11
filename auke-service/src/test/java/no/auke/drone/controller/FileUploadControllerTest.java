package no.auke.drone.controller;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Application;

/**
 * Created by huyduong on 6/10/2015.
 */
public class FileUploadControllerTest extends JerseyTest {
    @Override
    protected Application configure() {
        return new ResourceConfig(FileUploadController.class);
    }

    @Test
    public void test() {
        final String hello = target("/file/upload").request().get(String.class);
    }
}
