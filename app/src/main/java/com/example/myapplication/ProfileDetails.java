package com.example.myapplication;

public class ProfileDetails {

    public String username;
    public String userfname;
    public String userEmail;
    public String userlname;
    public String userGender;
    public String userAge;
    public String userHeight,userWeight;
    public String userBMI;

    public ProfileDetails() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserfname() {
        return userfname;
    }

    public void setUserfname(String userfname) {
        this.userfname = userfname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserlname() {
        return userlname;
    }

    public void setUserlname(String userlname) {
        this.userlname = userlname;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserHeight() {
        return userHeight;
    }

    public void setUserHeight(String userHeight) {
        this.userHeight = userHeight;
    }

    public String getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(String userWeight) {
        this.userWeight = userWeight;
    }

    public String getUserBMI() {
        return userBMI;
    }

    public void setUserBMI(String userBMI) {
        this.userBMI = userBMI;
    }

    public ProfileDetails(String userEmail, String userfname, String userlname, String userAge, String userGender,
                          String userHeight, String userWeight, String userBMI, String username) {
        this.userfname = userfname;
        this.userEmail = userEmail;
        this.userlname = userlname;
        this.userGender = userGender;
        this.userAge = userAge;
        this.userHeight = userHeight;
        this.userWeight = userWeight;
        this.userBMI = userBMI;
        this.username = username;


    }
}