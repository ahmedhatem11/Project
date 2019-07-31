package com.sumerge.program.rest;

import javax.faces.bean.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;


import com.sumerge.program.entities.User;
import com.sumerge.program.inputModels.changeGroupInputModel;
import com.sumerge.program.inputModels.changePasswordInputModel;

@Path("/user")
@RequestScoped
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public interface UserResourceInterface {

    @GET
    @Path("/getallusers")
    Response getAllUsers();

    @GET
    @Path("/getalluserswithdeleted")
    Response getAllUsersWithDeleted();

    @GET
    @Path("/getuserbyusername/{username}")
    Response getUserByUsername(@PathParam("username") String username);

    @PUT
    @Path("/resetuserpassword")
    Response changeUserPassword(changePasswordInputModel model);


    @PUT
    @Path("/changeusergroup")
    Response changeUserGroup(changeGroupInputModel model);

    @POST
    @Path("/addnewuser")
    Response newUser(User user);

    @DELETE
    @Path("/deleteuser")
    Response deleteUser(User user);

    @PUT
    @Path("/undodeleteuser")
    Response undoDeleteUser(User user);

}
