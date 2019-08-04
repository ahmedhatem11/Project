package com.sumerge.program.rest;

import javax.faces.bean.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;


import com.sumerge.program.entities.User;
import com.sumerge.program.inputModels.AddRemoveGroupInputModel;
import com.sumerge.program.inputModels.ChangeGroupInputModel;
import com.sumerge.program.inputModels.ChangePasswordInputModel;
import com.sumerge.program.inputModels.ForgetPasswordInputModel;

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

    @GET
    @Path("/getuserbyid/{id}")
    Response getUserById(@PathParam("id") int id);

    @PUT
    @Path("/resetuserpassword")
    Response changeUserPassword(ChangePasswordInputModel model);

    @PUT
    @Path("/forgotpassword")
    Response forgetPassword(User user);

    @PUT
    @Path("/changepasswordwithoutlogin")
    Response changeForgottenPassowrd(ForgetPasswordInputModel model);

    @PUT
    @Path("/addusertogroup")
    Response addUserToGroup(AddRemoveGroupInputModel model);

    @PUT
    @Path("/removeuserfromgroup")
    Response removeUserFromGroup(AddRemoveGroupInputModel model);

    @PUT
    @Path("/changeusergroup")
    Response changeUserGroup(ChangeGroupInputModel model);

    @PUT
    @Path("/updateuserinfo")
    Response updateUserInfo(User user);

    @POST
    @Path("/addnewuser")
    Response newUser(User user);

    @DELETE
    @Path("/deleteuser")
    Response deleteUser(User user);

    @PUT
    @Path("/undodeleteuser")
    Response undoDeleteUser(User user);

    @GET
    @Path("/getPassword/{username}")
    String getPassword(@PathParam("username") String username);

}
