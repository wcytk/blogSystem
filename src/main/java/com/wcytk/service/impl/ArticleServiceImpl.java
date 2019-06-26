package com.wcytk.service.impl;

import com.wcytk.dao.ArticleDao;
import com.wcytk.entity.Article;
import com.wcytk.entity.UserCollectArticle;
import com.wcytk.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Override
    public boolean AddArticle(int user_id, String title, String content, int thumbNum, int collectNum, String publishTime, String updateTime) {
        return articleDao.AddArticle(user_id, title, content, thumbNum, collectNum, publishTime, updateTime);
    }

    @Override
    public boolean DeleteArticleById(int id, int user_id) {
        return articleDao.DeleteArticleById(id, user_id);
    }

    @Override
    public boolean UpdateArticleById(int id, int user_id, String title, String content, int thumbNum, int collectNum, String updateTime) {
        return articleDao.UpdateArticleById(id, user_id, title, content, thumbNum, collectNum, updateTime);
    }

    @Override
    public boolean UpdateThumbNumById(int id, int thumbNum) {
        return articleDao.UpdateThumbNumById(id, thumbNum);
    }

    @Override
    public boolean UpdateCollectNumById(int id, int collectNum) {
        return articleDao.UpdateCollectNumById(id, collectNum);
    }

    @Override
    public List<Article> SelectAllArticles(int user_id) {
        return articleDao.SelectAllArticles(user_id);
    }

    @Override
    public Article SelectArticlesById(int id, int user_id) {
        return articleDao.SelectArticlesById(id, user_id);
    }

    @Override
    public List<Article> selectAllUserArticles(){
        return articleDao.selectAllUserArticles();
    }

    @Override
    public Article selectUserArticles(int id) {
        return articleDao.selectUserArticles(id);
    }

    @Override
    public List<Article> searchArticles(String search) {
        return articleDao.searchArticles(search);
    }

    @Override
    public List<Article> searchMyArticles(String search, int user_id) {
        return articleDao.searchMyArticles(search, user_id);
    }

    @Override
    public List<Article> adminArticles() {
        return articleDao.adminArticles();
    }

    @Override
    public boolean adminDeleteArticle(int id) {
        return articleDao.adminDeleteArticle(id);
    }

    @Override
    public boolean adminAddArticle(int user_id, String title, String content, int thumbNum, int collectNum, Timestamp publishTime, Timestamp updateTime) {
        return articleDao.adminAddArticle(user_id, title, content, thumbNum, collectNum, publishTime, updateTime);
    }

    @Override
    public boolean adminUpdateArticleById(int id, int user_id, String title, String content, int thumbNum, int collectNum, Timestamp updateTime){
        return articleDao.adminUpdateArticleById(id, user_id, title, content, thumbNum, collectNum, updateTime);
    }
}
