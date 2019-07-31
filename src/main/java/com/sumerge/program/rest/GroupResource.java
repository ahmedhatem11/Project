package com.sumerge.program.rest;

import com.sumerge.program.entities.Group;
import com.sumerge.program.entities.GroupRepo;

import javax.ejb.EJB;
import javax.ejb.Stateless;
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
            return Response.ok().
                    entity(groupRepo.getAllGroups()).
                    build();
        } catch (Exception e) {
            return Response.serverError().
                    entity(e).
                    build();
        }
    }

    @Override
    public Response getAllGroupsWithDeleted() {
        try {
            return Response.ok().
                    entity(groupRepo.getAllGroupsWithDeleted()).
                    build();
        } catch (Exception e) {
            return Response.serverError().
                    entity(e).
                    build();
        }
    }

    @Override
    public Response getGroupById(int id) {
        try {
            return Response.ok().
                    entity(groupRepo.getGroupById(id)).
                    build();
        } catch (Exception e) {
            return Response.serverError().
                    entity(e).
                    build();
        }
    }

    @Override
    public Response addNewGroup(Group group) {
        try {
            groupRepo.addGroup(group);
            return Response.ok().
                    build();
        } catch (Exception e) {
            return Response.serverError().
                    entity(e).
                    build();
        }
    }

    @Override
    public Response deleteGroup(Group group) {
        try {
            int groupId = group.getId();
            Group group2 = groupRepo.getGroupById(groupId);
            groupRepo.deleteGroup(group2);
            return Response.ok().build();
        }catch (Exception e){
            return Response.serverError().
                    entity(e).
                    build();
        }
    }

    @Override
    public Response undoDeleteGroup(Group group) {
        try {
            int groupId = group.getId();
            Group group2 = groupRepo.getGroupById(groupId);
            groupRepo.UndoDeleteGroup(group2);
            return Response.ok().build();
        }catch (Exception e){
            return Response.serverError().
                    entity(e).
                    build();
        }
    }
}
