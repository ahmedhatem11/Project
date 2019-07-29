package com.sumerge.program.entities;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

@Stateless
public class GroupRepo {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyPU");

    private EntityManager em = emf.createEntityManager();

//    @Resource private UserTransaction utx;

    public List<Group> getAllGroups() {
        try {
            return em.createNamedQuery("Group.findAll", Group.class).getResultList();
        } catch (Exception e) {
            throw e;
        }
    }
}
