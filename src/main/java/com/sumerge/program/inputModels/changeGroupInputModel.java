package com.sumerge.program.inputModels;

public class changeGroupInputModel {
    private String username;
    private int oldGroup;
    private int newGroup;

    public changeGroupInputModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getOldGroup() {
        return oldGroup;
    }

    public void setOldGroup(int oldGroup) {
        this.oldGroup = oldGroup;
    }

    public int getNewGroup() {
        return newGroup;
    }

    public void setNewGroup(int newGroup) {
        this.newGroup = newGroup;
    }
}
