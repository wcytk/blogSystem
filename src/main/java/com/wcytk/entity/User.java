package com.wcytk.entity;

import java.util.List;

public class User {
    private int id;

    private String username;

    private String password;

    private String email;

    private int isLogin;//ÊÇµÇÂ¼×´Ì¬ÊÇ1   ²»ÊÇµÇÂ¼×´Ì¬ÊÇ0

    private List<Article> articles;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(int isLogin) {
        this.isLogin = isLogin;
    }

    public User(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(int id, String username, String password, String email, int isLogin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.isLogin = isLogin;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGroup(List<Article> articles) {
        this.articles = articles;
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

    public List<Article> getGroup() {
        return articles;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
