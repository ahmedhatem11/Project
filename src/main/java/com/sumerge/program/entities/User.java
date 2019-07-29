package com.sumerge.program.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "USER")
@NamedQueries({
        @NamedQuery(name = "User.findAllAvaialbleUsers", query = "Select u from User u where u.isDeleted = 0"),
        @NamedQuery(name = "User.findAllUsersWithDeleted", query = "Select u from User u"),
        @NamedQuery(name = "User.changePassword", query = "Update User Set password = :newPassword Where username = :username and password = :oldPassword")
})
public class User implements Serializable {

    @Id
    @Column(name = "ID", unique = true)
    private int id;

    @Column(name = "USERNAME", unique = true, nullable = false)
    private String username;

    @JsonIgnore
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
    @ManyToMany
    @JoinTable(name = "USERGROUP", joinColumns = @JoinColumn(name = "USERID"), inverseJoinColumns = @JoinColumn(name = "GROUPID"))
    private List<Group> groups;

    public User() {
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
}
