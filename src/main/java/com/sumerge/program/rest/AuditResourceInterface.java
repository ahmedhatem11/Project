package com.sumerge.program.rest;

import javax.faces.bean.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/audit")
@RequestScoped
@Produces(APPLICATION_JSON)
public interface AuditResourceInterface {

    @GET
    @Path("/getauditlog")
    Response getAuditLog();

}
