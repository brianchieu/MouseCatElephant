package com.example.brian.mousecatelephant;

/**
 * Created by Brian on 4/21/2017.
 */

public class User {
    String _username;
    String _password;

    public User(){}

    public User(String name, String password){
        this._username = name;
        this._password = password;
    }

    // getting name
    public String getName(){
        return this._username;
    }

    // setting name
    public void setName(String name){
        this._username = name;
    }

    // getting phone number
    public String getPassword(){
        return this._password;
    }

    // setting phone number
    public void setPassword(String password){
        this._password = password;
    }
}
