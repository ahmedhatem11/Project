package com.sumerge.program.entities;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

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
public class GroupRepo {

    @PersistenceContext
    private EntityManager em;

    static MongoClient mongoClient = new MongoClient("localhost", 27017);
    static DB db =  mongoClient.getDB("auditDb");
    static DBCollection collection = db.getCollection("auditCollection");

    public List<Group> getAllGroups() throws Exception {
        try {
            List<Group> groups =  new ArrayList<>();
            groups =  em.createNamedQuery("Group.findAll", Group.class).getResultList();

            if(groups.size() == 0 || groups == null)
                throw new Exception("No Groups available at the database");

            return groups;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Group> getAllGroupsWithDeleted() throws Exception {
        try {
            List<Group> groups =  new ArrayList<>();
            groups =  em.createNamedQuery("Group.findAllWithDeleted", Group.class).getResultList();

            if(groups.size() == 0 || groups == null)
                throw new Exception("No Groups available at the database");

            return groups;
        } catch (Exception e) {
            throw e;
        }
    }

    public Group getGroupById(int id) throws Exception {
        try {
            Group group = null;
            group = em.find(Group.class, id);

            if (group == null)
                throw new Exception("No group exists with such id");

            return group;
        } catch (Exception e){
            throw e;
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void addGroup(Group group, String author) throws Exception {
        try {
            em.persist(group);

            insertAudit(group.toString(), "Adding New Group", author, group.toString());
        } catch (Exception e){
            throw new Exception("Group already exists");
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteGroup(int id, String author) throws Exception {
        try {
            Group group = getGroupById(id);
            if (group.isDeleted())
                throw new Exception("Group is already deleted");

            group.setDeleted(true);
            em.merge(group);

            JsonObject entityState = new JsonObject();
            entityState.addProperty("group id", id);
            insertAudit(entityState.toString(), "Deleting Group", author, group.toString());
        } catch (Exception e){
            throw e;
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void UndoDeleteGroup(int id, String author) throws Exception {
        try {
            Group group = getGroupById(id);
            if (!group.isDeleted())
                throw new Exception("Group is not deleted");

            group.setDeleted(false);
            em.merge(group);

            JsonObject entityState = new JsonObject();
            entityState.addProperty("group id", id);
            insertAudit(entityState.toString(), "Undo Deleting Group", author, group.toString());
        } catch (Exception e){
            throw e;
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
