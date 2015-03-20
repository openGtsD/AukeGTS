package no.auke.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.view.Viewable;

@Path("/")
public class MainController extends ControllerBase {

	
	@Context HttpServletRequest request;
	@Context HttpServletResponse response;

	@GET
	@Produces("text/html")
	@Path("/")
	public Response home() {
		return Response.ok(new Viewable("/home")).build();
	}
	
	
}
