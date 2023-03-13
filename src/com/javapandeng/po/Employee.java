package com.javapandeng.po;

import java.io.Serializable;

public class Employee implements Serializable {

    private Integer id;
    private String username;
    private String password;
    private String realName;

    public Employee(Integer id, String username, String password, String realName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.realName = realName;
    }

    public Employee() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Override
    public String toString() {
        return "Manage{" +
                "id=" + id +
                ", userName='" + username + '\'' +
                ", passWord='" + password + '\'' +
                ", realName='" + realName + '\'' +
                '}';
    }
}
