package com.wcytk.entity;

import java.sql.Timestamp;
import java.util.List;

public class Article {
    private int id;

    private int user_id;

    private String title;

    private String content;

    private int thumbNum;

    private int collectNum;

    private String publishTime;

    private String updateTime;

    private List<User> users;

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setThumbNum(int thumbNum) {
        this.thumbNum = thumbNum;
    }

    public void setCollectNum(int collectNum) {
        this.collectNum = collectNum;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setGroup(List<User> users) {
        this.users = users;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getThumbNum() {
        return thumbNum;
    }

    public int getCollectNum() {
        return collectNum;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public List<User> getGroup() {
        return users;
    }
}
