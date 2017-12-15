package com.example.susanne.myapp;

/**
 * Used to store the information of the user for the firebase.
 */

public class UserData {
    public String userName;
    public Integer highScore;
    public String userPassword;

    public UserData() {}

    public UserData(String name, String password, Integer high_score) {
        this.userName = name;
        this.userPassword = password;
        this.highScore = high_score;
    }


}
