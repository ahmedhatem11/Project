package com.sumerge.program.entities;

import java.util.List;

public class UserUserView {

    private String name;

    private String email;

    private String phoneNumber;

    private String address;

    private String role;

    private List<String[]> groupNames;

    public UserUserView() {
    }

    public UserUserView(String name, String email, String phoneNumber, String address, String role, List<String[]> groupNames) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
        this.groupNames = groupNames;
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

    public List<String[]> getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(List<String[]> groupNames) {
        this.groupNames = groupNames;
    }
}
