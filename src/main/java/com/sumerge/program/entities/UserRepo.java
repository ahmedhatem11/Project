package com.sumerge.program.entities;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import javax.ws.rs.core.Response;
import java.util.List;

import static java.util.logging.Level.SEVERE;

@Stateless
public class UserRepo {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyPU");

    private EntityManager em = emf.createEntityManager();

//    @Resource private UserTransaction utx;

    public List<User> getAllUsers() {
        try {
            return em.createNamedQuery("User.findAllAvaialbleUsers", User.class).getResultList();
        } catch (Exception e) {
            throw e;
        }
    }

    public List<User> getAllUsersWithDeleted(){
        try {
            return em.createNamedQuery("User.findAllUsersWithDeleted", User.class).getResultList();
        } catch (Exception e) {
            throw e;
        }
    }

//    public void changePassword(String username){
//        try {
//            return em.createNamedQuery("User.findAllUsersWithDeleted", User.class).getResultList();
//        } catch (Exception e) {
//            throw e;
//        }
//    }



}
