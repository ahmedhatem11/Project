package com.sumerge.program.rest;

import com.sumerge.program.entities.AdminUserView;
import com.sumerge.program.entities.User;
import com.sumerge.program.entities.UserRepo;
import com.sumerge.program.entities.UserUserView;
import com.sumerge.program.inputModels.changeGroupInputModel;
import com.sumerge.program.inputModels.changePasswordInputModel;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Stateless
public class UserResource implements UserResourceInterface {

    @Context
    private SecurityContext securityContext;

    @EJB
    private UserRepo userRepo;

    @Override
    public Response getAllUsers() {
        try {
            if(securityContext.isUserInRole("admin")){
                List<AdminUserView> users = userRepo.getAllUsersAdminView();
                return Response.ok().
                        entity(users).
                        build();
            }
            else {
                List<UserUserView> users = userRepo.getAllUsersUserView();
                return Response.ok().
                        entity(users).
                        build();
            }

        } catch (Exception e) {
            return Response.serverError().
                    entity(e).
                    build();
        }
    }

    @Override
    public Response getAllUsersWithDeleted() {
        try {
            System.out.println(securityContext.getUserPrincipal().toString());
            List<AdminUserView> users = userRepo.getAllUsersWithDeleted();
            return Response.ok().
                    entity(users).
                    build();
        } catch (Exception e) {
            return Response.serverError().
                    entity(e).
                    build();
        }
    }

    @Override
    public Response getUserByUsername(String username) {
        try {
            if(securityContext.isUserInRole("admin")){
                AdminUserView user = userRepo.getUserWithUsernameAdminView(username);
                return Response.ok().
                        entity(user).
                        build();
            }
            else {
                UserUserView user = userRepo.getUserWithUsernameUserView(username);
                return Response.ok().
                        entity(user).
                        build();
            }
        } catch (EJBTransactionRolledbackException e){
            return Response.serverError().entity("No User Found").build();
        }
    }

    @Override
    public Response changeUserPassword(changePasswordInputModel model) {
        try {
            String oldPassword = model.getOldPassword();
            String newPassword = model.getNewPassword();

            oldPassword = hashString(oldPassword);
            newPassword =hashString(newPassword);
            String username = securityContext.getUserPrincipal().toString();
            User user = userRepo.getUserWithUsername(username);
            if(user.getPassword().equals(oldPassword)){
                userRepo.changePassword(user, newPassword);
                return Response.ok().
                        build();
            }
            else {
                throw new Exception("Wrong Password");
            }

        } catch (Exception e) {
            return Response.serverError().
                    entity(e).
                    build();
        }
    }

    @Override
    public Response changeUserGroup(changeGroupInputModel model) {
        try {
            String username = model.getUsername();
            int oldGroup = model.getOldGroup();
            int newGroup = model.getNewGroup();
            User user = userRepo.getUserWithUsername(username);
            userRepo.changeUserGroup(user,oldGroup,newGroup);
            return Response.ok().
                    build();

        } catch (Exception e) {
            return Response.serverError().
                    entity(e).
                    build();
        }
    }

    @Override
    public Response newUser(User user) {
        try {
            user.setPassword(hashString(user.getPassword()));
            userRepo.addNewUser(user);
            return Response.ok().build();
        }catch (Exception e){
            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }

    @Override
    public Response deleteUser(User user) {
        try {
            String username = user.getUsername();
            User user2 = userRepo.getUserWithUsername(username);
            userRepo.deleteUser(user2);
            return Response.ok().build();
        }catch (Exception e){
            return Response.serverError().
                    entity(e).
                    build();
        }
    }

    @Override
    public Response undoDeleteUser(User user) {
        try {
            String username = user.getUsername();
            User user2 = userRepo.getUserWithUsername(username);
            userRepo.undoDeleteUser(user2);
            return Response.ok().build();
        }catch (Exception e){
            return Response.serverError().
                    entity(e).
                    build();
        }
    }

    private static String hashString(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] digest = messageDigest.digest(input.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; ++i) {
            sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();

    }
}
