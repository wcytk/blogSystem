package com.wcytk.service;

import com.wcytk.entity.Article;
import com.wcytk.entity.UserCollectArticle;

import java.sql.Timestamp;
import java.util.List;

public interface ArticleService {
    public boolean AddArticle(int user_id, String title, String content, int thumbNum, int collectNum, String publishTime, String updateTime);

    public boolean DeleteArticleById(int id, int user_id);

    public boolean UpdateArticleById(int id, int user_id, String title, String content, int thumbNum, int collectNum, String updateTime);

    public boolean UpdateThumbNumById(int id, int thumbNum);

    public boolean UpdateCollectNumById(int id, int collectNum);

    public List<Article> SelectAllArticles(int user_id);

    public Article SelectArticlesById(int id, int user_id);

    public List<Article> selectAllUserArticles();

    public Article selectUserArticles(int id);

    public List<Article> searchArticles(String search);

    public List<Article> searchMyArticles(String search, int user_id);

    public List<Article> adminArticles();

    public boolean adminDeleteArticle(int id);

    public boolean adminAddArticle(int user_id, String title, String content, int thumbNum, int collectNum, Timestamp publishTime, Timestamp updateTime);

    public boolean adminUpdateArticleById(int id, int user_id, String title, String content, int thumbNum, int collectNum, Timestamp updateTime);
}
