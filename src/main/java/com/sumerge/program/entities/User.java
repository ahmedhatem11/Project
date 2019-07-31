package com.sumerge.program.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.tools.javac.util.Pair;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USER", schema = "USERMANAGEMENT")
@NamedQueries({
        @NamedQuery(name = "User.findAllAvaialbleUsers", query = "Select u from User u where u.isDeleted = 0"),
        @NamedQuery(name = "User.findAllUsersWithDeleted", query = "Select u from User u"),
        @NamedQuery(name = "User.changePassword", query = "Update User Set password = :newPassword Where username = :username and password = :oldPassword"),
        @NamedQuery(name = "User.findUserWithUsername", query = "Select u from User u where u.username = :username"),
        @NamedQuery(name = "User.findById", query = "Select u from User u where u.id = :id")
//        @NamedQuery(name = "User.changeGroup", query = "Update UserGroup Set GroupID = :newGroup Where username = :username and groupID = :oldGroup")
})
public class User implements Serializable {

    @Id
    @Column(name = "ID", unique = true)
    private int id;

    @Column(name = "USERNAME", unique = true, nullable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    @Column(name = "PHONENUMBER")
    private String phoneNumber;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "ROLE", nullable = false)
    private String role;

    @JsonIgnore
    @Column(name = "ISDELETED")
    private boolean isDeleted;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USERGROUP", joinColumns = @JoinColumn(name = "USERID"), inverseJoinColumns = @JoinColumn(name = "GROUPID"))
    private List<Group> groups;

    @Transient
    private List<String[]> groupNames;

    public User() {
    }

    public User(int id, String username, String password, String name, String email, String phoneNumber, String address, String role, boolean isDeleted, List<Group> groups, List<String[]> groupNames) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
        this.isDeleted = isDeleted;
        this.groups = groups;
        this.groupNames = groupNames;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @JsonIgnore
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<String[]> getGroupNames() {

        List<String[] > result = new ArrayList<>();
        for(Group group : groups){
            if (!group.isDeleted()){
                String[] pair = new String[2];
                pair[0] = group.getId() + "";
                pair[1] = group.getName();
                result.add(pair);
            }
        }
        return result;
    }

    public void setGroupNames(List<String[]> groupNames) {
        this.groupNames = groupNames;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", role='" + role + '\'' +
                ", isDeleted=" + isDeleted +
                ", groups=" + groups +
                ", groupNames=" + groupNames +
                '}';
    }
}
