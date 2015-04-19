package no.auke.drone.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.security.access.AccessDeniedException;

/**
 * @author thaihuynh
 *
 */
@Provider
public class AccessDeniedMapper implements ExceptionMapper<AccessDeniedException> {
    
    public Response toResponse(AccessDeniedException e) {
        return Response
                .status(Status.UNAUTHORIZED)
                .entity(e.getMessage())
                .type("text/plain").build();
    }
}
