package no.auke.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

public class ControllerBase {
    @Context
    protected HttpServletRequest request;
    @Context
    protected HttpServletResponse response;

   
}
