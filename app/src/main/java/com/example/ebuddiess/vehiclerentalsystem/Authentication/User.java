package com.example.ebuddiess.vehiclerentalsystem.Authentication;

public class User {
    String firstname,lastname,displayname,profileurl,mobileno,emailadress,userid;
    boolean isAdmin;

    public User(String firstname, String lastname, String displayname, String profileurl, String mobileno, String emailadress, String userid, boolean isAdmin) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.displayname = displayname;
        this.profileurl = profileurl;
        this.mobileno = mobileno;
        this.emailadress = emailadress;
        this.userid = userid;
        this.isAdmin = isAdmin;
    }

    public User(String mobileno, String emailadress, String userid, boolean isAdmin) {
        this.mobileno = mobileno;
        this.emailadress = emailadress;
        this.userid = userid;
        this.isAdmin = isAdmin;
        firstname = "";
        displayname = "";
        lastname = "";
        profileurl = "";
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getProfileurl() {
        return profileurl;
    }

    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getEmailadress() {
        return emailadress;
    }

    public void setEmailadress(String emailadress) {
        this.emailadress = emailadress;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
