package com.sumerge.program.test;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.sumerge.program.entities.Group;
import com.sumerge.program.rest.GroupResourceInterface;
import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GroupResourceTest {

    private static GroupResourceInterface client;

    @BeforeClass
    public static void init() {
        List<Object> providers = new ArrayList<Object>();
        providers.add(new JacksonJaxbJsonProvider());
        client = JAXRSClientFactory.create("http://localhost:8880/",
                GroupResourceInterface.class, providers);

    }

    @Test
    public void atestAddNewGroup(){
        String auth = "Basic " + Base64Utility.encode(("default_admin:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth);

        Response response = client.addNewGroup(new Group("Test Group", "Group for integration testing"));
        assertTrue("Error in function", response.getStatus() == 200);

        String auth2 = "Basic " + Base64Utility.encode(("user:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth2);
        Response response2 = client.addNewGroup(new Group("Test Group", "Group for integration testing"));
        assertTrue("Error in function", response2.getStatus() == 403);

    }

    @Test
    public void btestGetAllGroups(){
        String auth = "Basic " + Base64Utility.encode(("default_admin:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth);

        Response response = client.getAllGroups();
        assertTrue("Error in function", response.getStatus() == 200);

        String auth2 = "Basic " + Base64Utility.encode(("user:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth2);
        Response response2 = client.getAllGroups();
        assertTrue("Error in function", response2.getStatus() == 200);
    }

    @Test
    public void ctestGetAllGroupsWithDeleted(){
        String auth = "Basic " + Base64Utility.encode(("default_admin:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth);

        Response response = client.getAllGroupsWithDeleted();
        assertTrue("Error in function", response.getStatus() == 200);

        String auth2 = "Basic " + Base64Utility.encode(("user:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth2);
        Response response2 = client.getAllGroupsWithDeleted();
        assertTrue("Error in function", response2.getStatus() == 403);
    }

    @Test
    public void dtestGetGroupById(){
        String auth = "Basic " + Base64Utility.encode(("default_admin:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth);

        Response response = client.getGroupById(1);
        assertTrue("Error in function", response.getStatus() == 200);

        String auth2 = "Basic " + Base64Utility.encode(("user:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth2);
        Response response2 = client.getGroupById(1);
        assertTrue("Error in function", response2.getStatus() == 200);
    }

    @Test
    public void etestDeleteGroup(){
        String auth = "Basic " + Base64Utility.encode(("default_admin:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth);

        Response response = client.deleteGroup(new Group(2, null, null));
        assertTrue("Error in function", response.getStatus() == 200);

        Response response2 = client.deleteGroup(new Group(2, null, null));
        assertTrue("Error in function", response2.getStatus() == 500);

        String auth2 = "Basic " + Base64Utility.encode(("user:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth2);
        Response response3 = client.deleteGroup(new Group(2, null, null));
        assertTrue("Error in function", response3.getStatus() == 403);
    }

    @Test
    public void etestUndoDeleteGroup(){
        String auth = "Basic " + Base64Utility.encode(("default_admin:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth);

        Response response = client.undoDeleteGroup(new Group(2, null, null));
        assertTrue("Error in function", response.getStatus() == 200);

        Response response2 = client.undoDeleteGroup(new Group(2, null, null));
        assertTrue("Error in function", response2.getStatus() == 500);

        String auth2 = "Basic " + Base64Utility.encode(("user:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth2);
        Response response3 = client.undoDeleteGroup(new Group(2, null, null));
        assertTrue("Error in function", response3.getStatus() == 403);
    }
}
