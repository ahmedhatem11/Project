package com.sumerge.program.entities;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class GroupRepo {

    @PersistenceContext
    private EntityManager em;

    public List<Group> getAllGroups() {
        try {
            List<Group> groups =  em.createNamedQuery("Group.findAll", Group.class).getResultList();
            return groups;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Group> getAllGroupsWithDeleted() {
        try {
            List<Group> groups =  em.createNamedQuery("Group.findAllWithDeleted", Group.class).getResultList();
            return groups;
        } catch (Exception e) {
            throw e;
        }
    }

    public Group getGroupById(int id){
        try {
            Group group = em.createNamedQuery("Group.findGroupById", Group.class).setParameter("id", id).getSingleResult();
            return group;
        } catch (Exception e){
            throw e;
        }
    }

    public void addGroup(Group group){
        try {
            em.persist(group);
        } catch (Exception e){
            throw e;
        }
    }

    public void deleteGroup(Group group){
        try {
            group.setDeleted(true);
            em.merge(group);
        } catch (Exception e){
            throw e;
        }
    }

    public void UndoDeleteGroup(Group group){
        try {
            group.setDeleted(false);
            em.merge(group);
        } catch (Exception e){
            throw e;
        }
    }
}
