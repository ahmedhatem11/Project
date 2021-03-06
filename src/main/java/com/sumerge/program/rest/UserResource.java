package com.sumerge.program.rest;

import com.sumerge.program.entities.AdminUserView;
import com.sumerge.program.entities.User;
import com.sumerge.program.entities.UserRepo;
import com.sumerge.program.entities.UserUserView;
import com.sumerge.program.inputModels.AddRemoveGroupInputModel;
import com.sumerge.program.inputModels.ChangeGroupInputModel;
import com.sumerge.program.inputModels.ChangePasswordInputModel;
import com.sumerge.program.inputModels.ForgetPasswordInputModel;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Properties;
import java.util.Random;


@Stateless
public class UserResource implements UserResourceInterface {

    @Context
    private SecurityContext securityContext;

    @EJB
    private UserRepo userRepo;

    final static Logger logger = Logger.getLogger(UserResource.class);

    @Override
    public Response getAllUsers() {
        try {
            logger.info("Entering getAllUser()");
            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }

            if(securityContext.isUserInRole("admin")){
                List<AdminUserView> users = userRepo.getAllUsersAdminView();

                logger.info("List of users returned to client");
                return Response.ok().
                        entity(users).
                        build();
            }
            else {
                List<UserUserView> users = userRepo.getAllUsersUserView();

                logger.info("List of users returned to client");
                return Response.ok().
                        entity(users).
                        build();
            }

        } catch (Exception e) {
            logger.info("Exception in function getAllUsers(): " + e.getMessage());
            if (e.getClass().equals(NotAuthorizedException.class))
                return Response.status(401).entity("Please log in to enter this page").build();
            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }

    @Override
    public Response getAllUsersWithDeleted() {
        try {
            logger.info("entering function getAllUsersWithDeleted()");
            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }
            if (!securityContext.isUserInRole("admin"))
                throw new ForbiddenException("no access");


            List<AdminUserView> users = userRepo.getAllUsersWithDeleted();
            logger.info("getAllUsersWithDeleted() returned list of users");
            return Response.ok().
                    entity(users).
                    build();
        } catch (Exception e) {
            logger.info("Exception in function getAllUsersWithDeleted(): " + e.getMessage());
            if (e.getClass().equals(NotAuthorizedException.class))
                return Response.status(401).entity("Please log in to enter this page").build();
            if (e.getClass().equals(ForbiddenException.class))
                return Response.status(403).entity("You can't access this page").build();

            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }

    @Override
    public Response getUserByUsername(String username) {
        try {
            logger.info("entering function getUserByUsername()");
            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }

            if(securityContext.isUserInRole("admin")){
                AdminUserView user = userRepo.getUserWithUsernameAdminView(username);

                logger.info("getUserByUsername() returned a user");
                return Response.ok().
                        entity(user).
                        build();
            }
            else {
                UserUserView user = userRepo.getUserWithUsernameUserView(username);
                logger.info("getUserByUsername() returned a user");
                return Response.ok().
                        entity(user).
                        build();
            }
        } catch (Exception e){
            logger.info("Exception in function getUserByUsername(): " + e.getMessage());

            if (e.getClass().equals(NotAuthorizedException.class))
                return Response.status(401).entity("Please log in to enter this page").build();

            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @Override
    public Response getUserById(int id) {
        try {
            logger.info("entering function getUserByID()");

            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }

            if(securityContext.isUserInRole("admin")){
                AdminUserView user = userRepo.getUserWithIdAdminView(id);
                logger.info("getUserByID() reutrned user");
                return Response.ok().
                        entity(user).
                        build();
            }
            else {
                UserUserView user = userRepo.getUserWithIdUserView(id);
                logger.info("getUserByID() reutrned user");
                return Response.ok().
                        entity(user).
                        build();
            }
        } catch (Exception e){
            logger.info("Exception in function getUserByID(): " + e.getMessage());
            if (e.getClass().equals(NotAuthorizedException.class))
                return Response.status(401).entity("Please log in to enter this page").build();

            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @Override
    public Response changeUserPassword(ChangePasswordInputModel model) {
        try {
            logger.info("entering function changeUserPassword()");
            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }

            String username = securityContext.getUserPrincipal().toString();
            if (username.equals("default_admin")){
                throw new Exception("default admin password can't be changed");
            }

            if (model.getNewPassword().equals("") || model.getNewPassword() == null){
                throw new Exception("Please enter a new password");
            }

            model.setOldPassword(hashString(model.getOldPassword()));
            model.setNewPassword(hashString(model.getNewPassword()));

            User user = userRepo.getUserWithUsername(username);
            if(user.getPassword().equals(model.getOldPassword())){
                userRepo.changePassword(model, user, username);
                logger.info("changeUserPassword() changed password successfully");
                return Response.ok().
                        build();
            }
            else {
                throw new Exception("Incorrect Old Password");
            }

        } catch (Exception e) {
            logger.info("Exception in function changeUserPassword(): " + e.getMessage());
            if (e.getClass().equals(NotAuthorizedException.class))
                return Response.status(401).entity("Please log in to enter this page").build();

            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }

    @Override
    public Response forgetPassword(User user) {

        try {
            logger.info("entering function forgetPassword()");
            if (user.getUsername() == null){
                throw new Exception("Please enter a username");
            } else if (user.getUsername().equals("default_admin")){
                throw new Exception("default admin password can't be changed");
            }

            User user1 = userRepo.getUserWithUsername(user.getUsername());


            byte[] array = new byte[4];
            new Random().nextBytes(array);
            String newPassword = new String(array, Charset.forName("UTF-8"));
            String newPasswordHashed = hashString(newPassword);

            final String username = "ufcbots1@gmail.com";
            final String password = "P@ss0rd1";

            Properties prop = new Properties();
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "587");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(prop,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("ufcbots1@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(user1.getEmail())
            );
            message.setSubject("Reset Password");
            message.setText("New Password: " + newPasswordHashed + " \n" + "Enter this password in this as the old password in this link and change it. \n " +
                    "localhost:8880/user/changepasswordwithoutlogin");

            Transport.send(message);
            userRepo.changePassword(new ChangePasswordInputModel(user1.getPassword(), newPasswordHashed), user, "Reset Password System");

            logger.info("forgetPassword() has sent a mail to the user");

            return Response.ok().
                    build();
        } catch (Exception e){
            logger.info("Exception in function forgetPassword(): " + e.getMessage());

            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }

    @Override
    public Response changeForgottenPassowrd(ForgetPasswordInputModel model) {
        try {
            logger.info("entering function changeForgottenPassowrd()");

            if (model.getUsername().equals("default_admin")){
                throw new Exception("default admin password can't be changed");
            }

            if (model.getNewPassword().equals("") || model.getNewPassword() == null){
                throw new Exception("Please enter a new password");
            }

            model.setNewPassword(hashString(model.getNewPassword()));
            User user = userRepo.getUserWithUsername(model.getUsername());
            if(user.getPassword().equals(model.getOldPassword())){
                userRepo.changePassword(new ChangePasswordInputModel(model.getOldPassword(), model.getNewPassword()), user, model.getUsername());

                logger.info("changeForgottenPassowrd() user password changed");
                return Response.ok().
                        build();
            }
            else {
                throw new Exception("Incorrect Old Password");
            }

        } catch (Exception e) {
            logger.info("Exception in function changeForgottenPassowrd(): " + e.getMessage());

            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }

    @Override
    public Response addUserToGroup(AddRemoveGroupInputModel model) {
        try {
            logger.info("entering function addUserToGroup()");

            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }
            if (!securityContext.isUserInRole("admin"))
                throw new ForbiddenException("no access");

            if (model.getUsername() == null)
                throw new Exception("No username entered");
            userRepo.addUserToGroup(model, securityContext.getUserPrincipal().toString());
            logger.info("addUserToGroup() added user to group");
            return Response.ok().
                    build();

        } catch (Exception e) {
            logger.info("Exception in function addUserToGroup(): " + e.getMessage());

            if (e.getClass().equals(NotAuthorizedException.class))
                return Response.status(401).entity("Please log in to enter this page").build();
            if (e.getClass().equals(ForbiddenException.class))
                return Response.status(403).entity("You can't access this page").build();

            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }

    @Override
    public Response removeUserFromGroup(AddRemoveGroupInputModel model) {
        try {
            logger.info("entering function removeUserFromGroup()");

            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }
            if (!securityContext.isUserInRole("admin"))
                throw new ForbiddenException("no access");

            if (model.getUsername() == null)
                throw new Exception("No username entered");

            if (model.getUsername().equals("default_admin") && model.getGroupId() == 1){
                throw new Exception("default admin can't be removed from default group");
            }

            userRepo.removeUserFromGroup(model, securityContext.getUserPrincipal().toString());


            return Response.ok().
                    build();

        } catch (Exception e) {
            logger.info("Exception in function removeUserFromGroup(): " + e.getMessage());

            if (e.getClass().equals(NotAuthorizedException.class))
                return Response.status(401).entity("Please log in to enter this page").build();
            if (e.getClass().equals(ForbiddenException.class))
                return Response.status(403).entity("You can't access this page").build();

            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }

    @Override
    public Response changeUserGroup(ChangeGroupInputModel model) {
        try {
            logger.info("entering function changeUserGroup()");

            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }
            if (!securityContext.isUserInRole("admin"))
                throw new ForbiddenException("no access");

            if (model.getUsername() == null)
                throw new Exception("No username entered");

            if (model.getUsername().equals("default_admin") && model.getOldGroup() == 1){
                throw new Exception("default admin can't be removed from default group");
            }

            userRepo.changeUserGroup(model, securityContext.getUserPrincipal().toString());

            logger.info("changeUserGroup() changed user group");
            return Response.ok().
                    build();

        } catch (Exception e) {
            logger.info("Exception in function changeUserGroup(): " + e.getMessage());

            if (e.getClass().equals(NotAuthorizedException.class))
                return Response.status(401).entity("Please log in to enter this page").build();
            if (e.getClass().equals(ForbiddenException.class))
                return Response.status(403).entity("You can't access this page").build();

            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }

    @Override
    public Response updateUserInfo(User user) {
        try {
            logger.info("entering function updateUserInfo()");

            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }

            if (user.getUsername() == null)
                throw new Exception("Please enter username");

            if (user.getUsername().equals("default_admin")){
                throw new Exception("default admin info can't be updated");
            }

            userRepo.updateUserInfo(user);

            logger.info("updateUserInfo() updated the info of the user");
            return Response.ok().build();
        }catch (Exception e){
            logger.info("Exception in function updateUserInfo(): " + e.getMessage());

            if (e.getClass().equals(NotAuthorizedException.class))
                return Response.status(401).entity("Please log in to enter this page").build();

            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }

    @Override
    public Response newUser(User user) {
        try {
            logger.info("entering function newUser()");

            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }
            if (!securityContext.isUserInRole("admin"))
                throw new ForbiddenException("no access");

            if (user.getUsername() == null)
                throw new Exception("No username entered");
            if (user.getEmail() == null)
                throw new Exception("No email entered");
            if (user.getPassword() == null)
                throw new Exception("No password entered");
            if (user.getName() == null)
                throw new Exception("No name entered");
            if (user.getRole() == null)
                throw new Exception("No role entered");

            user.setPassword(hashString(user.getPassword()));
            userRepo.addNewUser(user, securityContext.getUserPrincipal().toString());

            logger.info("newUser() added new user");
            return Response.ok().build();
        }catch (Exception e){
            logger.info("Exception in function newUser(): " + e.getMessage());

            if (e.getClass().equals(NotAuthorizedException.class))
                return Response.status(401).entity("Please log in to enter this page").build();
            if (e.getClass().equals(ForbiddenException.class))
                return Response.status(403).entity("You can't access this page").build();

            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }

    @Override
    public Response deleteUser(User user) {
        try {
            logger.info("entering function deleteUser()");

            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }
            if (!securityContext.isUserInRole("admin"))
                throw new ForbiddenException("no access");

            if (user.getUsername().equals("default_admin")){
                throw new Exception("default admin can't be deleted");
            }
            userRepo.deleteUser(user.getUsername(), securityContext.getUserPrincipal().toString());

            logger.info("deleteUser() deleted the user");
            return Response.ok().build();
        }catch (Exception e){
            logger.info("Exception in function deleteUser(): " + e.getMessage());

            if (e.getClass().equals(NotAuthorizedException.class))
                return Response.status(401).entity("Please log in to enter this page").build();
            if (e.getClass().equals(ForbiddenException.class))
                return Response.status(403).entity("You can't access this page").build();

            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }

    @Override
    public Response undoDeleteUser(User user) {
        try {
            logger.info("entering function undoDeleteUser()");

            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }
            if (!securityContext.isUserInRole("admin"))
                throw new ForbiddenException("no access");

            userRepo.undoDeleteUser(user.getUsername(), securityContext.getUserPrincipal().toString());

            logger.info("undoDeleteUser() got the user back");
            return Response.ok().build();
        }catch (Exception e){
            logger.info("Exception in function undoDeleteUser(): " + e.getMessage());

            if (e.getClass().equals(NotAuthorizedException.class))
                return Response.status(401).entity("Please log in to enter this page").build();
            if (e.getClass().equals(ForbiddenException.class))
                return Response.status(403).entity("You can't access this page").build();

            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }

    @Override
    public String getPassword(String username) {
        if(!securityContext.getUserPrincipal().toString().equals("default_admin")){
            throw new ForbiddenException("you can't access this page");
        }
        try {
            return userRepo.getUserWithUsername(username).getPassword();
        } catch (Exception e){
            return null;
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
