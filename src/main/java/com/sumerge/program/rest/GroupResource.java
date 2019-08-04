package com.sumerge.program.rest;

import com.sumerge.program.entities.Group;
import com.sumerge.program.entities.GroupRepo;

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

    @Override
    public Response getAllGroups() {
        try {
            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }

            return Response.ok().
                    entity(groupRepo.getAllGroups()).
                    build();
        } catch (Exception e) {
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
            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }
            if (!securityContext.isUserInRole("admin"))
                throw new ForbiddenException("no access");

            return Response.ok().
                    entity(groupRepo.getAllGroupsWithDeleted()).
                    build();
        } catch (Exception e) {
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
            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }

            return Response.ok().
                    entity(groupRepo.getGroupById(id)).
                    build();
        } catch (Exception e) {
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
            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }
            if (!securityContext.isUserInRole("admin"))
                throw new ForbiddenException("no access");

            if(group.getName() == null)
                throw new Exception("No name entered");

            groupRepo.addGroup(group, securityContext.getUserPrincipal().toString());
            return Response.ok().
                    build();
        } catch (Exception e) {
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
            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }
            if (!securityContext.isUserInRole("admin"))
                throw new ForbiddenException("no access");

            groupRepo.deleteGroup(group.getId(), securityContext.getUserPrincipal().toString());
            return Response.ok().build();
        }catch (Exception e){
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
            if (securityContext.getUserPrincipal() == null){
                throw new NotAuthorizedException("not logged in");
            }
            if (!securityContext.isUserInRole("admin"))
                throw new ForbiddenException("no access");

            groupRepo.UndoDeleteGroup(group.getId(), securityContext.getUserPrincipal().toString());
            return Response.ok().build();
        }catch (Exception e){
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
