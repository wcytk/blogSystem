package com.wcytk.service.impl;

import com.wcytk.entity.*;
import com.wcytk.dao.UserDao;
import com.wcytk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    //将用户输入的修改后的用户信息传进来，返回修改以后的用户的个人信息
    public boolean updateUserDetail(DetailUser detailUser) {
        return userDao.updateUserDetail(detailUser);
    }

    public List<User> ListAll() {
        List<User> list = userDao.ListAll();
        return list;
    }

    public User findByName(String username) {
        User user = userDao.findByName(username);
        return user;
    }

    public User findById(int id) {
        User user = userDao.findById(id);
        return user;
    }

    public UserAttention selectAttention(int userId1, int userId2) {
        return userDao.selectAttention(userId1, userId2);
    }

    public boolean find(int id) {
        boolean num = userDao.find(id);
        return num;
    }

    //根据用户的id 进行一对一的连表查询
    public DetailUser showDetailInfo(int detailId) {
        return userDao.showDetailInfo(detailId);
    }

    public void insertUser(User user) {
        userDao.insertUser(user);
    }

    public boolean insertUserDetail(int detailId) {
        return userDao.insertUserDetail(detailId);
    }

    public User login(User user) {
        User user1 = userDao.login(user);
        if (user1 != null) {
            user1.setIsLogin(1);
            updateUser(user1);
        }

        return user1;
    }

    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    public void logout(User user) {
        user.setIsLogin(0);
        userDao.logoutUser(user);
    }

    public List<User> admin_listAll() {
        return userDao.admin_listAll();
    }

    public void admin_updateUser(User user) {
        userDao.admin_updateUser(user);
    }

    public void admin_delete(int id) {
        userDao.admin_delete(id);
    }

    public void admin_addUser(User user) {
        userDao.admin_addUser(user);
    }

    public List<User> admin_search(String username) {
        return userDao.admin_search(username);
    }

    public void deleteById(int id) {
        userDao.deleteById(id);
    }

    public List<User> selectUserArticle(int id) {
        return userDao.selectUserArticle(id);
    }

    public List<User> selectUserThumbArticle(int id) {
        return userDao.selectUserThumbArticle(id);
    }

    public List<User> userAttention(int userId1) {
        List<User> list = userDao.userAttention(userId1);
        return list;
    }

    //这个service层中的方法是删除某一个关注的人，如果删除成功，就会返回true，
//    如果删除不成功就会返回false   便于向前端传值
    public boolean deleteAttention(String username) {
        boolean isDeleteAttention = userDao.deleteAttention(username);
        return isDeleteAttention;
    }

    public boolean insertCollection(UserCollectArticle userCollectArticle) {
        return userDao.insertCollection(userCollectArticle);
    }

    public boolean insertThumb(UserThumbArticle userThumbArticle) {
        return userDao.insertThumb(userThumbArticle);
    }

    public boolean deleteCollection(UserCollectArticle userCollectArticle) {
        return userDao.deleteCollection(userCollectArticle);
    }

    public boolean deleteThumb(UserThumbArticle userThumbArticle) {
        return userDao.deleteThumb(userThumbArticle);
    }

    public boolean setAttention(int userid1, int userid2) {
        return userDao.setAttention(userid1, userid2);
    }

    public UserCollectArticle selectCollection(int user_id, int article_id) {
        return userDao.selectCollection(user_id, article_id);
    }

    public UserThumbArticle selectThumb(int user_id, int article_id) {
        return userDao.selectThumb(user_id, article_id);
    }

    public boolean addCollectionNum(int id) {
        return userDao.addCollectionNum(id);
    }

    public boolean addThumbNum(int id) {
        return userDao.addThumbNum(id);
    }

    public boolean subCollectionNum(int id) {
        return userDao.subCollectionNum(id);
    }

    public boolean subThumbNum(int id) {
        return userDao.subThumbNum(id);
    }

    public boolean changeHeadImage(String headImage,int detailId){
        return userDao.changeHeadImage(headImage,detailId);
    }

    public List<User> getFans(int userId2){
        return userDao.userFans(userId2);
    }
}
