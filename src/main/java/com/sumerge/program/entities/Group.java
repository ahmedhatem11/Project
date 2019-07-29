package com.sumerge.program.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;



import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "GROUP", schema = "USERMANAGEMENT")
@NamedQueries(
        @NamedQuery(name = "Group.findAll", query = "Select g from Group g where g.isDeleted = 0")
)
public class Group implements Serializable {

    @Id
    @Column(name = "ID", unique = true)
    private int id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @JsonIgnore
    @Column(name = "ISDELETED")
    private boolean isDeleted;

    @ManyToMany
    @JoinTable(name = "USERGROUP", joinColumns = @JoinColumn(name = "GROUPID"), inverseJoinColumns = @JoinColumn(name = "USERID"))
    private List<User> users;

    public Group() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
