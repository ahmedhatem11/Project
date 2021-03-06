package com.sumerge.program.rest;

import com.mongodb.*;
import org.apache.log4j.Logger;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public class AuditLogResource implements AuditResourceInterface {

    @Context
    private SecurityContext securityContext;


    final static Logger logger = Logger.getLogger(UserResource.class);

    @Override
    public Response getAuditLog() {
        try {
            logger.info("Entering getAuditLog()");

            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }
            if (!securityContext.isUserInRole("admin"))
                throw new ForbiddenException("no access");

            MongoClient mongoClient = new MongoClient("localhost", 27017);
            DB db =  mongoClient.getDB("auditDb");
            DBCollection collection = db.getCollection("auditCollection");
            DBCursor cursor = collection.find();
            String log = "";
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                log += obj + "\n";
            }
            logger.info("getAuditLog() returned the audit");

            return Response.ok().entity(log).build();
        }catch (Exception e){
            logger.info("Exception in function getAuditLog(): " + e.getMessage());

            if (e.getClass().equals(NotAuthorizedException.class))
                return Response.status(401).entity("Please log in to enter this page").build();
            if (e.getClass().equals(ForbiddenException.class))
                return Response.status(403).entity("You can't access this page").build();

            return Response.serverError().
                    entity("Error in getting audit log").
                    build();
        }
    }

}
