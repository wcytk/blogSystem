package com.wcytk.dao;

import com.wcytk.entity.Article;
import com.wcytk.entity.UserCollectArticle;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface ArticleDao {
    boolean AddArticle(@Param("user_id") int user_id, @Param("title") String title, @Param("content") String content, @Param("thumbNum") int thumbNum, @Param("collectNum") int collectNum, @Param("publishTime") String publishTime, @Param("updateTime") String updateTime);

    boolean DeleteArticleById(@Param("id") int id, @Param("user_id") int user_id);

    boolean UpdateArticleById(@Param("id") int id, @Param("user_id") int user_id, @Param("title") String title, @Param("content") String content, @Param("thumbNum") int thumbNum, @Param("collectNum") int collectNum, @Param("updateTime") String updateTime);

    boolean UpdateThumbNumById(@Param("id") int id, @Param("thumbNum") int thumbNum);

    boolean UpdateCollectNumById(@Param("id") int id, @Param("collectNum") int collectNum);

    List<Article> SelectAllArticles(@Param("user_id") int user_id);

    Article SelectArticlesById(@Param("id") int id, @Param("user_id") int user_id);

    List<Article> selectAllUserArticles();

    Article selectUserArticles(@Param("id") int id);

    List<Article> searchArticles(@Param("search") String search);

    List<Article> searchMyArticles(@Param("search") String search, @Param("user_id") int user_id);

    List<Article> adminArticles();

    boolean adminDeleteArticle(@Param("id") int id);

    boolean adminAddArticle(@Param("user_id") int user_id, @Param("title") String title, @Param("content") String content, @Param("thumbNum") int thumbNum, @Param("collectNum") int collectNum, @Param("publishTime") Timestamp publishTime, @Param("updateTime") Timestamp updateTime);

    boolean adminUpdateArticleById(@Param("id") int id, @Param("user_id") int user_id, @Param("title") String title, @Param("content") String content, @Param("thumbNum") int thumbNum, @Param("collectNum") int collectNum, @Param("updateTime") Timestamp updateTime);
}
