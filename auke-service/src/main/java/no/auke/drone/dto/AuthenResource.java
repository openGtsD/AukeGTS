package no.auke.drone.dto;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import no.auke.drone.domain.Account;

import org.springframework.stereotype.Component;

/**
 * @author thaihuynh
 */
@Path("/authen")
@Component
@Produces(MediaType.APPLICATION_JSON)
public class AuthenResource {

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public AukeResponse login(Account account) {
        //TODO move to service for authen
        AukeResponse response = new AukeResponse(account != null && account.isAdmin(), account);
        return response;
    }

}
