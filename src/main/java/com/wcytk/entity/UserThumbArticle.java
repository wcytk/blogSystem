package com.wcytk.entity;

import java.sql.Timestamp;

public class UserThumbArticle {
    private int user_id;

    private int article_id;

    private int id;

    private Timestamp create_time;

    public void setArticle(int article_id) {
        this.article_id = article_id;
    }

    public void setUser(int user_id) {
        this.user_id = user_id;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public int getId() {
        return id;
    }

    public int getArticle() {
        return article_id;
    }

    public int getUser() {
        return user_id;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }
}
