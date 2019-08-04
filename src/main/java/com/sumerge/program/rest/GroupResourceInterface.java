package com.sumerge.program.rest;

import com.sumerge.program.entities.Group;

import javax.faces.bean.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/group")
@RequestScoped
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public interface GroupResourceInterface {

    @GET
    @Path("/getallgroups")
    Response getAllGroups();

    @GET
    @Path("/getallgroupsWithDeleted")
    Response getAllGroupsWithDeleted();

    @GET
    @Path("/getgroupbyid/{id}")
    Response getGroupById(@PathParam("id") int id);

    @POST
    @Path("/addnewgroup")
    Response addNewGroup(Group group);

    @DELETE
    @Path("/deletegroup")
    Response deleteGroup(Group group);

    @PUT
    @Path("/undodeletegroup")
    Response undoDeleteGroup(Group group);
}
