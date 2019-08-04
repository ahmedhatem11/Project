package com.sumerge.program.inputModels;

public class ChangePasswordInputModel {
    private String oldPassword;
    private String newPassword;

    public ChangePasswordInputModel() {
    }

    public ChangePasswordInputModel(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
