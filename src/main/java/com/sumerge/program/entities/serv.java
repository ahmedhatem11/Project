package com.sumerge.program.entities;

import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/")
@RequestScoped
public class serv {

    @Context
    private SecurityContext securityContext;

    @EJB
    private UserRepo userRepo;

    @EJB
    private GroupRepo groupRepo;


    @GET()
    @Path("/users")
    @Produces(APPLICATION_JSON)
    public Response getAllUsers() {
        try {
            System.out.println(securityContext.getUserPrincipal().toString());
            return Response.ok().
                    entity(userRepo.getAllUsers()).
                    build();
        } catch (Exception e) {
            return Response.serverError().
                    entity(e).
                    build();
        }
    }

    @GET()
    @Path("/groups")
    @Produces(APPLICATION_JSON)
    public Response getAllGroups() {
        try {
            return Response.ok().
                    entity(groupRepo.getAllGroups()).
                    build();
        } catch (Exception e) {
            return Response.serverError().
                    entity(e).
                    build();
        }
    }

}
