package com.sumerge.program.inputModels;

public class AddRemoveGroupInputModel {

    private String username;
    private int groupId;

    public AddRemoveGroupInputModel(String username, int groupId) {
        this.username = username;
        this.groupId = groupId;
    }

    public AddRemoveGroupInputModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
