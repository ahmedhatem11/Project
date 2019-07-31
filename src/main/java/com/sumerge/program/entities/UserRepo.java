package com.sumerge.program.entities;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.parse;

@Stateless
public class UserRepo {

    @PersistenceContext
    private EntityManager em;

    public List<AdminUserView> getAllUsersAdminView() {
        try {
            List<User> users =  em.createNamedQuery("User.findAllAvaialbleUsers", User.class).getResultList();
            List<AdminUserView> userViews = new ArrayList<>();
            for (User user : users){
                userViews.add(new AdminUserView(user.getId(), user.getUsername(),user.getName(),user.getEmail(),user.getPhoneNumber(),user.getAddress(),user.getRole(),user.isDeleted(),user.getGroupNames()));
            }
            return userViews;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<UserUserView> getAllUsersUserView() {
        try {
            List<User> users =  em.createNamedQuery("User.findAllAvaialbleUsers", User.class).getResultList();
            List<UserUserView> userViews = new ArrayList<>();
            for (User user : users){
                userViews.add(new UserUserView(user.getName(),user.getEmail(),user.getPhoneNumber(),user.getAddress(),user.getRole(),user.getGroupNames()));
            }
            return userViews;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<AdminUserView> getAllUsersWithDeleted(){
        try {
            List<User> users = em.createNamedQuery("User.findAllUsersWithDeleted", User.class).getResultList();
            List<AdminUserView> userViews = new ArrayList<>();
            for (User user : users){
                userViews.add(new AdminUserView(user.getId(), user.getUsername(),user.getName(),user.getEmail(),user.getPhoneNumber(),user.getAddress(),user.getRole(),user.isDeleted(),user.getGroupNames()));
            }
            return userViews;
        } catch (Exception e) {
            throw e;
        }
    }

    public User getUserWithUsername(String username){
        try {
            User user = em.createNamedQuery("User.findUserWithUsername", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return user;
        } catch (Exception e) {
            throw e;
        }
    }

    public UserUserView getUserWithUsernameUserView(String username){
        try {
            User user = em.createNamedQuery("User.findUserWithUsername", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            UserUserView userView = new UserUserView(user.getName(),user.getEmail(),user.getPhoneNumber(),user.getAddress(),user.getRole(),user.getGroupNames());
            return userView;
        } catch (Exception e) {
            throw e;
        }
    }

    public AdminUserView getUserWithUsernameAdminView(String username){
        try {
            User user = em.createNamedQuery("User.findUserWithUsername", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            AdminUserView userView = new AdminUserView(user.getId(), user.getUsername(),user.getName(),user.getEmail(),user.getPhoneNumber(),user.getAddress(),user.getRole(),user.isDeleted(),user.getGroupNames());
            return userView;
        } catch (Exception e) {
            throw e;
        }
    }


    public void addNewUser(User user){
        try {
            em.persist(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void changePassword(User user, String newPassword){
        try {
            user.setPassword(newPassword);
            em.merge(user);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteUser(User user) {
        try {
            user.setDeleted(true);
            em.merge(user);
        } catch (Exception e) {
            throw e;
        }
    }

    public void undoDeleteUser(User user){
        try {
            user.setDeleted(false);
            em.merge(user);
        } catch (Exception e) {
            throw e;
        }
    }

    public void changeUserGroup(User user, int oldGroupId, int newGroupID) throws Exception {
        try {
            List<Group> userGroups = user.getGroups();

            Group oldGroup = null;
            for(Group group : userGroups){
                if (group.getId() == oldGroupId){
                    oldGroup = group;
                    break;
                }
            }
            if (oldGroup == null){
                throw new Exception("User was not in this group initially");
            }
            userGroups.remove(oldGroup);
            Group newGroup = null;
            newGroup = em.find(Group.class, newGroupID);

            if (newGroup == null){
                throw new Exception("No groups exists with id " + newGroup);
            }

            userGroups.add(newGroup);
            user.setGroups(userGroups);

            em.merge(user);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void updateUserInfo(User user){
        User originalUser = getUserWithUsername(user.getUsername());
        if(user.getEmail() != null){
            originalUser.setEmail(user.getEmail());
        }
        if(user.getName() != null){
            originalUser.setName(user.getName());
        }
        if(user.getPhoneNumber() != null){
            originalUser.setPhoneNumber(user.getPhoneNumber());
        }
        if(user.getAddress() != null){
            originalUser.setAddress(user.getAddress());
        }

        try {
            em.merge(user);
        }catch (Exception e){
            throw e;
        }
    }


}
