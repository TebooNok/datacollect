package com.pginfo.datacollect.dao;

public class User {

    public User(){
    }

    public User(String id, String name, String password, String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    private String id;
    private String name;
    private String password;
    private String role;
    public String getid(){
        return id;
    }
    public String getname(){
        return name;
    }
    public String getpassword(){
        return password;
    }
    public String getrole(){
        return role;
    }

}
