package com.example.bookme.ObjectModels;

public class UserObject {

    private String userId;
    private String userFullName;
    private String userEmail;
    private String userType;

    public UserObject() {

    }

    public UserObject(String userId, String userFullName, String userEmail, String userType) {
        this.userId = userId;
        this.userFullName = userFullName;
        this.userEmail = userEmail;
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
