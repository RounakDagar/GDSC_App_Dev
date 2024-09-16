package com.example.chatapp;

public class Users {
    private String profilepic;
    private String mail;
    private String username;
    private String password;
    private String userid;
    private String lastmssg;
    private String status;

    // Default constructor required for Firebase
    public Users() {
    }

    public Users(String profilepic, String mail, String username, String password, String userid, String status) {
        this.profilepic = profilepic;
        this.mail = mail;
        this.username = username;
        this.password = password;
        this.userid = userid;
        this.status = status;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLastmssg() {
        return lastmssg;
    }

    public void setLastmssg(String lastmssg) {
        this.lastmssg = lastmssg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
