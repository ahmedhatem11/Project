package com.sumerge.program.test;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.sumerge.program.rest.AuditResourceInterface;
import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class AuditResourceTest {

    private static AuditResourceInterface client;

    @BeforeClass
    public static void init() {
        List<Object> providers = new ArrayList<Object>();
        providers.add(new JacksonJaxbJsonProvider());
        client = JAXRSClientFactory.create("http://localhost:8880/",
                AuditResourceInterface.class, providers);

    }

    @Test
    public void testGetAuditLog(){
        String auth = "Basic " + Base64Utility.encode(("default_admin:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth);
        Response response = client.getAuditLog();
        assertTrue("Error in function getAllUsers()", response.getStatus() == 200);

        String auth2 = "Basic " + Base64Utility.encode(("user:123").getBytes());
        WebClient.client(client).reset();
        WebClient.client(client).header("Authorization", auth2);
        Response response2 = client.getAuditLog();
        assertTrue("Error in function getAllUsers()", response2.getStatus() == 403);
    }
}
