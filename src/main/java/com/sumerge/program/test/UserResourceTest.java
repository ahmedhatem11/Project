package com.sumerge.program.test;


import static org.junit.Assert.assertTrue;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.sumerge.program.entities.User;
import com.sumerge.program.inputModels.AddRemoveGroupInputModel;
import com.sumerge.program.inputModels.ChangeGroupInputModel;
import com.sumerge.program.inputModels.ChangePasswordInputModel;
import com.sumerge.program.inputModels.ForgetPasswordInputModel;
import com.sumerge.program.rest.UserResourceInterface;
import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;

import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.ws.rs.core.Response;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserResourceTest {

    private static UserResourceInterface client;

    @BeforeClass
    public static void init() {
        List<Object> providers = new ArrayList<Object>();
        providers.add(new JacksonJaxbJsonProvider());
        client = JAXRSClientFactory.create("http://localhost:8880/",
                UserResourceInterface.class, providers);

    }

    @Test
    public void aatestNewUser(){
        String auth = "Basic " + Base64Utility.encode(("default_admin:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth);
        Response response = client.newUser(new User("user", "123", "User 1", "ahatem@sumerge.com", "123-056-44", "maadi", "user"));
        assertTrue("Error in function", response.getStatus() == 200);

        Response response2 = client.newUser(new User("user", "123", "User 1", "ahatem@sumerge.com", "123-056-44", "maadi", "user"));
        assertTrue("Error in function", response2.getStatus() == 500);
    }

    @Test
    public void abtestGetAllUsers(){

        String auth = "Basic " + Base64Utility.encode(("default_admin:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth);
        Response response = client.getAllUsers();
        assertTrue("Error in function getAllUsers()", response.getStatus() == 200);
    }

    @Test
    public void btestGetAllUsersWithDeleted(){
        String auth = "Basic " + Base64Utility.encode(("default_admin:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth);
        Response response = client.getAllUsersWithDeleted();
        assertTrue("Error in function", response.getStatus() == 200);

        String auth2 = "Basic " + Base64Utility.encode(("user:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth2);
        Response response2 = client.getAllUsersWithDeleted();
        assertTrue("Error in function", response2.getStatus() == 403);
    }

    @Test
    public void ctestGetUserByUsername(){
        String auth = "Basic " + Base64Utility.encode(("default_admin:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth);
        Response response = client.getUserByUsername("user");
        assertTrue("Error in function", response.getStatus() == 200);
    }

    @Test
    public void dtestGetUserById(){
        String auth = "Basic " + Base64Utility.encode(("default_admin:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth);
        Response response = client.getUserById(1);
        assertTrue("Error in function", response.getStatus() == 200);
    }

    @Test
    public void etestChangeUserPassword(){
        String auth = "Basic " + Base64Utility.encode(("user:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth);
        Response response = client.changeUserPassword(new ChangePasswordInputModel("123", "123"));
        assertTrue("Error in function", response.getStatus() == 200);

        String auth2 = "Basic " + Base64Utility.encode(("default_admin:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth2);
        Response response2 = client.changeUserPassword(new ChangePasswordInputModel("123", "123"));
        assertTrue("Error in function", response2.getStatus() == 500);
    }

    @Test
    public void ftestForgetPassword(){
        WebClient.client(client).reset();
        Response response = client.forgetPassword(new User(0,"user", "", "", "", "", "", "", false, new ArrayList<>(), new ArrayList<>()));
        assertTrue("Error in function", response.getStatus() == 200);
    }

    @Test
    public void gtestChangeForgottenPassowrd(){
        String auth = "Basic " + Base64Utility.encode(("default_admin:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth);
        String password = client.getPassword("user");

        Response response = client.changeForgottenPassowrd(new ForgetPasswordInputModel("user", password, "123"));
        assertTrue("Error in function", response.getStatus() == 200);

        Response response2 = client.changeForgottenPassowrd(new ForgetPasswordInputModel("user", "1", "123"));
        assertTrue("Error in function", response2.getStatus() == 500);

    }

    @Test
    public void htestAddUserToGroup(){
        String auth = "Basic " + Base64Utility.encode(("default_admin:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth);
        Response response = client.addUserToGroup(new AddRemoveGroupInputModel("user", 1));
        assertTrue("Error in function", response.getStatus() == 200);

        Response response2 = client.addUserToGroup(new AddRemoveGroupInputModel("user", 1));
        assertTrue("Error in function", response2.getStatus() == 500);

        Response response3 = client.addUserToGroup(new AddRemoveGroupInputModel("user", 0));
        assertTrue("Error in function", response3.getStatus() == 500);
    }

    @Test
    public void itestRemoveUserFromGroup(){
        String auth = "Basic " + Base64Utility.encode(("default_admin:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth);
        Response response = client.removeUserFromGroup(new AddRemoveGroupInputModel("user", 1));
        assertTrue("Error in function", response.getStatus() == 200);

        Response response2 = client.removeUserFromGroup(new AddRemoveGroupInputModel("user", 1));
        assertTrue("Error in function", response2.getStatus() == 500);

        Response response3 = client.removeUserFromGroup(new AddRemoveGroupInputModel("user", 0));
        assertTrue("Error in function", response3.getStatus() == 500);

        Response response4 = client.removeUserFromGroup(new AddRemoveGroupInputModel("default_admin", 1));
        assertTrue("Error in function", response4.getStatus() == 500);
    }

    @Test
    public void jtestChangeUserGroup(){
        String auth = "Basic " + Base64Utility.encode(("default_admin:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth);

        client.addUserToGroup(new AddRemoveGroupInputModel("user", 1));

        Response response = client.changeUserGroup(new ChangeGroupInputModel("user",1, 2));
        assertTrue("Error in function 1", response.getStatus() == 200);

        Response response5 = client.changeUserGroup(new ChangeGroupInputModel("default_admin",1,2));
        assertTrue("Error in function 2", response5.getStatus() == 500);

        Response response2 = client.changeUserGroup(new ChangeGroupInputModel("user",1,2));
        assertTrue("Error in function 3", response2.getStatus() == 500);

        Response response3 = client.changeUserGroup(new ChangeGroupInputModel("user",0,2));
        assertTrue("Error in function 4", response3.getStatus() == 500);

        Response response4 = client.changeUserGroup(new ChangeGroupInputModel("user",2,0));
        assertTrue("Error in function 5", response4.getStatus() == 500);

    }

    @Test
    public void ktestUpdateUserInfo(){
        String auth = "Basic " + Base64Utility.encode(("user:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth);
        Response response = client.updateUserInfo(new User("user",null, null, null, null, "shorouk", "user"));
        assertTrue("Error in function", response.getStatus() == 200);

        Response response2 = client.updateUserInfo(new User("user",null, null, "default_admin@usermail.com", null, "shorouk", "user"));
        assertTrue("Error in function", response2.getStatus() == 500);

        String auth2 = "Basic " + Base64Utility.encode(("default_admin:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth2);
        Response response3 = client.updateUserInfo(new User("default_admin",null, null, null, null, "shorouk", "user"));
        assertTrue("Error in function", response3.getStatus() == 500);

    }

    @Test
    public void ltestDeleteUser(){
        String auth = "Basic " + Base64Utility.encode(("default_admin:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth);
        Response response = client.deleteUser(new User("user",null, null, null, null, null, "user"));
        assertTrue("Error in function", response.getStatus() == 200);

        Response response2 = client.deleteUser(new User("user",null, null, null, null, null, "user"));
        assertTrue("Error in function", response2.getStatus() == 500);

        Response response3 = client.deleteUser(new User("default_admin",null, null, null, null, null, null));
        assertTrue("Error in function", response3.getStatus() == 500);

        String auth2 = "Basic " + Base64Utility.encode(("user:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth2);
        Response response4 = client.deleteUser(new User("user",null, null, null, null, null, "user"));
        assertTrue("Error in function", response4.getStatus() == 403);

    }

    @Test
    public void mtestUndoDeleteUser(){
        String auth = "Basic " + Base64Utility.encode(("default_admin:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth);
        Response response = client.undoDeleteUser(new User("user",null, null, null, null, null, "user"));
        assertTrue("Error in function", response.getStatus() == 200);

        Response response2 = client.undoDeleteUser(new User("user",null, null, null, null, null, "user"));
        assertTrue("Error in function", response2.getStatus() == 500);

        String auth2 = "Basic " + Base64Utility.encode(("user:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth2);
        Response response3 = client.undoDeleteUser(new User("user",null, null, null, null, null, "user"));
        assertTrue("Error in function", response3.getStatus() == 403);

    }


}
