package com.sumerge.program.rest;

import com.sumerge.program.entities.Group;
import com.sumerge.program.entities.GroupRepo;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Stateless
public class GroupResource implements GroupResourceInterface{

    @Context
    private SecurityContext securityContext;

    @EJB
    private GroupRepo groupRepo;

    final static Logger logger = Logger.getLogger(UserResource.class);

    @Override
    public Response getAllGroups() {
        try {
            logger.info("Entering getAllGroups()");
            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }

            logger.info("getAllGroups() returned list of groups");
            return Response.ok().
                    entity(groupRepo.getAllGroups()).
                    build();
        } catch (Exception e) {
            logger.info("Exception in function getAllGroups(): " + e.getMessage());

            if (e.getClass().equals(NotAuthorizedException.class))
                return Response.status(401).entity("Please log in to enter this page").build();

            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }

    @Override
    public Response getAllGroupsWithDeleted() {
        try {
            logger.info("Entering getAllGroupsWithDeleted()");

            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }
            if (!securityContext.isUserInRole("admin"))
                throw new ForbiddenException("no access");

            logger.info("getAllGroupsWithDeleted() returned list of groups");
            return Response.ok().
                    entity(groupRepo.getAllGroupsWithDeleted()).
                    build();
        } catch (Exception e) {

            logger.info("Exception in function getAllGroupsWithDeleted(): " + e.getMessage());

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
    public Response getGroupById(int id) {
        try {
            logger.info("Entering getGroupById()");

            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }

            logger.info("getGroupById() returned a group");
            return Response.ok().
                    entity(groupRepo.getGroupById(id)).
                    build();
        } catch (Exception e) {
            logger.info("Exception in function getGroupById(): " + e.getMessage());

            if (e.getClass().equals(NotAuthorizedException.class))
                return Response.status(401).entity("Please log in to enter this page").build();

            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }

    @Override
    public Response addNewGroup(Group group) {
        try {
            logger.info("Entering addNewGroup()");

            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }
            if (!securityContext.isUserInRole("admin"))
                throw new ForbiddenException("no access");

            if(group.getName() == null)
                throw new Exception("No name entered");

            groupRepo.addGroup(group, securityContext.getUserPrincipal().toString());

            logger.info("addNewGroup() added a new group");
            return Response.ok().
                    build();
        } catch (Exception e) {
            logger.info("Exception in function addNewGroup(): " + e.getMessage());

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
    public Response deleteGroup(Group group) {
        try {
            logger.info("Entering deleteGroup()");

            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }
            if (!securityContext.isUserInRole("admin"))
                throw new ForbiddenException("no access");

            if (group.getId() == 1){
                throw new Exception("default group can't be deleted");
            }

            groupRepo.deleteGroup(group.getId(), securityContext.getUserPrincipal().toString());

            logger.info("deleteGroup() deleted a group");
            return Response.ok().build();
        }catch (Exception e){
            logger.info("Exception in function deleteGroup(): " + e.getMessage());

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
    public Response undoDeleteGroup(Group group) {
        try {
            logger.info("Entering undoDeleteGroup()");

            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }
            if (!securityContext.isUserInRole("admin"))
                throw new ForbiddenException("no access");

            groupRepo.UndoDeleteGroup(group.getId(), securityContext.getUserPrincipal().toString());

            logger.info("undoDeleteGroup() got a group back");
            return Response.ok().build();
        }catch (Exception e){
            logger.info("Exception in function undoDeleteGroup(): " + e.getMessage());

            if (e.getClass().equals(NotAuthorizedException.class))
                return Response.status(401).entity("Please log in to enter this page").build();
            if (e.getClass().equals(ForbiddenException.class))
                return Response.status(403).entity("You can't access this page").build();

            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }
}
