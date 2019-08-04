package com.sumerge.program.entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.sumerge.program.inputModels.AddRemoveGroupInputModel;
import com.sumerge.program.inputModels.ChangeGroupInputModel;
import com.sumerge.program.inputModels.ChangePasswordInputModel;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class UserRepo {

    @PersistenceContext
    private EntityManager em;

    static MongoClient mongoClient = new MongoClient("localhost", 27017);
    static DB db =  mongoClient.getDB("auditDb");
    static DBCollection collection = db.getCollection("auditCollection");



    public List<AdminUserView> getAllUsersAdminView() throws Exception {
        try {
            List<User> users = new ArrayList<>();
            users = em.createNamedQuery("User.findAllAvaialbleUsers", User.class).getResultList();
            List<AdminUserView> userViews = new ArrayList<>();
            for (User user : users){
                userViews.add(new AdminUserView(user.getId(), user.getUsername(),user.getName(),user.getEmail(),user.getPhoneNumber(),user.getAddress(),user.getRole(),user.isDeleted(),user.getGroupNames()));
            }
            return userViews;
        } catch (Exception e) {
            throw new Exception("No users available at the database");
        }
    }

    public List<UserUserView> getAllUsersUserView() throws Exception {
        try {
            List<User> users = new ArrayList<>();
            users = em.createNamedQuery("User.findAllAvaialbleUsers", User.class).getResultList();
            List<UserUserView> userViews = new ArrayList<>();
            for (User user : users){
                userViews.add(new UserUserView(user.getName(),user.getEmail(),user.getPhoneNumber(),user.getAddress(),user.getRole(),user.getGroupNames()));
            }
            return userViews;
        } catch (Exception e) {
            throw new Exception("No users available at the database");
        }
    }

    public List<AdminUserView> getAllUsersWithDeleted() throws Exception {
        try {
            List<User> users = new ArrayList<>();
            users = em.createNamedQuery("User.findAllUsersWithDeleted", User.class).getResultList();
            List<AdminUserView> userViews = new ArrayList<>();
            for (User user : users){
                userViews.add(new AdminUserView(user.getId(), user.getUsername(),user.getName(),user.getEmail(),user.getPhoneNumber(),user.getAddress(),user.getRole(),user.isDeleted(),user.getGroupNames()));
            }
            return userViews;
        } catch (Exception e) {
            throw new Exception("No users available at the database");
        }
    }

    public User getUserWithUsername(String username) throws Exception {
        try {
            User user = null;
            user = em.createNamedQuery("User.findUserWithUsername", User.class)
                    .setParameter("username", username)
                    .getSingleResult();

            return user;
        } catch (Exception e) {
            throw new Exception("No user exists with such username");
        }
    }

    public UserUserView getUserWithUsernameUserView(String username) throws Exception {
        try {
            User user = null;
            user = em.createNamedQuery("User.findUserWithUsername", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            UserUserView userView = new UserUserView(user.getName(),user.getEmail(),user.getPhoneNumber(),user.getAddress(),user.getRole(),user.getGroupNames());
            return userView;
        } catch (Exception e) {
            throw new Exception("No user exists with such username");
        }
    }

    public AdminUserView getUserWithUsernameAdminView(String username) throws Exception {
        try {
            User user = null;
            user = em.createNamedQuery("User.findUserWithUsername", User.class)
                        .setParameter("username", username)
                        .getSingleResult();
            AdminUserView userView = new AdminUserView(user.getId(), user.getUsername(),user.getName(),user.getEmail(),user.getPhoneNumber(),user.getAddress(),user.getRole(),user.isDeleted(),user.getGroupNames());
            return userView;
        } catch (Exception e) {
            throw new Exception("No user exists with such username");
        }
    }

    public User getUserWithId(int id) throws Exception {
        try {
            User user = null;
            user = em.find(User.class, id);
            return user;
        } catch (Exception e) {
            throw new Exception("No user exists with such id");
        }
    }

    public UserUserView getUserWithIdUserView(int id) throws Exception {
        try {
            User user = null;
            user = em.find(User.class, id);
            UserUserView userView = new UserUserView(user.getName(),user.getEmail(),user.getPhoneNumber(),user.getAddress(),user.getRole(),user.getGroupNames());
            return userView;
        } catch (Exception e) {
            throw new Exception("No user exists with such id");
        }
    }

    public AdminUserView getUserWithIdAdminView(int id) throws Exception {
        try {
            User user = null;
            user = em.find(User.class, id);
            AdminUserView userView = new AdminUserView(user.getId(), user.getUsername(),user.getName(),user.getEmail(),user.getPhoneNumber(),user.getAddress(),user.getRole(),user.isDeleted(),user.getGroupNames());
            return userView;
        } catch (Exception e) {
            throw new Exception("No user exists with such id");
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void addNewUser(User user, String author) throws Exception {
        try {
            em.persist(user);
            insertAudit(user.toString(), "Adding New User", author, user.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("User already exists");
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void changePassword(ChangePasswordInputModel model, User user, String author) throws Exception {
        try {
            User originalUser = getUserWithUsername(user.getUsername());
            originalUser.setPassword(model.getNewPassword());
            em.merge(originalUser);

            Gson gson = new Gson();
            model.setOldPassword("");
            model.setNewPassword("");
            insertAudit(gson.toJson(model), "Changing Password", author, user.toString());
        } catch (Exception e) {
            throw new Exception("Error in changing user password");
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteUser(String username, String author) throws Exception {
        try {
            User user = getUserWithUsername(username);
            if (user.isDeleted())
                throw new Exception("User is already deleted");

            user.setDeleted(true);
            em.merge(user);
            JsonObject entityState = new JsonObject();
            entityState.addProperty("username", user.getUsername());
            insertAudit(entityState.toString(), "Deleting User", author, user.toString());
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void undoDeleteUser(String username, String author) throws Exception {
        try {
            User user = getUserWithUsername(username);
            if (!user.isDeleted())
                throw new Exception("User is not deleted");

            user.setDeleted(false);
            em.merge(user);
            JsonObject entityState = new JsonObject();
            entityState.addProperty("username", user.getUsername());
            insertAudit(entityState.toString(), "Undo Deleting User", author, user.toString());
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void addUserToGroup(AddRemoveGroupInputModel model, String author) throws Exception {
        try {
            User user = getUserWithUsername(model.getUsername());
            List<Group> userGroups = user.getGroups();

            for(Group group : userGroups){
                if (group.getId() == model.getGroupId()){
                    throw new Exception("User "+ model.getUsername() +" is already in the group with id: " + model.getGroupId());
                }
            }

            Group newGroup = null;
            newGroup = em.find(Group.class, model.getGroupId());

            if (newGroup == null){
                throw new Exception("No groups exists with id " + model.getGroupId());
            }

            userGroups.add(newGroup);
            user.setGroups(userGroups);
            em.merge(user);

            Gson gson = new Gson();
            insertAudit(gson.toJson(model), "Adding User to Group", author, user.toString());


        } catch (Exception e){
            throw e;
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void removeUserFromGroup(AddRemoveGroupInputModel model, String author) throws Exception {
        try {
            User user = getUserWithUsername(model.getUsername());
            List<Group> userGroups = user.getGroups();

            Group oldGroup = null;
            for(Group group : userGroups){
                if (group.getId() == model.getGroupId()){
                    oldGroup = group;
                }
            }


            if (oldGroup == null){
                throw new Exception("No groups exists with id " + model.getGroupId());
            }

            userGroups.remove(oldGroup);
            user.setGroups(userGroups);
            em.merge(user);

            Gson gson = new Gson();
            insertAudit(gson.toJson(model), "Removing User from Group", author, user.toString());


        } catch (Exception e){
            throw e;
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void changeUserGroup(ChangeGroupInputModel model, String author) throws Exception {
        try {
            User user = getUserWithUsername(model.getUsername());
            List<Group> userGroups = user.getGroups();

            for(Group group : userGroups){
                if (group.getId() == model.getNewGroup()){
                    throw new Exception("User "+ model.getUsername() +" is already in the group with id: " + model.getNewGroup());
                }
            }

            Group oldGroup = null;
            for(Group group : userGroups){
                if (group.getId() == model.getOldGroup()){
                    oldGroup = group;
                    break;
                }
            }
            if (oldGroup == null){
                throw new Exception("User "+ model.getUsername() +" was not in this group initially");
            }
            userGroups.remove(oldGroup);
            Group newGroup = null;
            newGroup = em.find(Group.class, model.getNewGroup());


            if (newGroup == null){
                throw new Exception("No groups exists with id " + model.getNewGroup());
            }

            userGroups.add(newGroup);
            user.setGroups(userGroups);

            em.merge(user);

            Gson gson = new Gson();
            insertAudit(gson.toJson(model), "Changing User Group", author, user.toString());

        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void updateUserInfo(User user) throws Exception {
        User originalUser = getUserWithUsername(user.getUsername());
        JsonObject entityState = new JsonObject();
        entityState.addProperty("id", user.getId());
        entityState.addProperty("username", user.getUsername());
        if(user.getEmail() != null){
            originalUser.setEmail(user.getEmail());
            entityState.addProperty("email", user.getEmail());
        }
        if(user.getName() != null){
            originalUser.setName(user.getName());
            entityState.addProperty("name", user.getName());
        }
        if(user.getPhoneNumber() != null){
            originalUser.setPhoneNumber(user.getPhoneNumber());
            entityState.addProperty("phoneNumber", user.getPhoneNumber());
        }
        if(user.getAddress() != null){
            originalUser.setAddress(user.getAddress());
            entityState.addProperty("address", user.getAddress());
        }

        try {
            em.merge(originalUser);
            insertAudit(entityState.toString(), "Information Update", originalUser.getUsername(), originalUser.toString());
        }catch (Exception e){
            throw new Exception("Email already exists");
        }
    }


    public static void insertAudit(String entityState, String actionName, String actionAuthor, String entityDetails) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        BasicDBObject document = new BasicDBObject();
        document.put("Entity State", entityState);
        document.put("Action Name", actionName);
        document.put("Action Time", dateFormat.format(date));
        document.put("Action Author", actionAuthor);
        document.put("Entity Details", entityDetails);
        collection.insert(document);
    }

}
