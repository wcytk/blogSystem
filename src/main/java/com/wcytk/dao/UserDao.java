package com.wcytk.dao;

import com.wcytk.entity.*;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface UserDao {

    List<User> ListAll();

    //注册  添加用户
    void insertUser(User user);

    boolean insertUserDetail(@Param("detailId") int detailId);

    UserAttention selectAttention(@Param("userId1") int userId1, @Param("userId2") int userId2);

    //按id查询数据库
    User findById(int id);

    //按name查询数据库
    User findByName(String username);

    boolean find(int id);

    //按id删除用户
    void deleteById(int id);

    //登录
    User login(User user);

    //更新用户登录状态信息
    void updateUser(User user);

    //修改用户的个人详细信息
    boolean updateUserDetail(DetailUser detailUser);

    User logoutUser(User user);

    List<User> admin_listAll();

    void admin_updateUser(User user);

    void admin_delete(int id);

    void admin_addUser(User user);

    List<User> admin_search(@Param("username") String username);

    DetailUser showDetailInfo(int detailId);

    List<User> selectUserArticle(int id);

    List<User> selectUserThumbArticle(int id);

    //下面是获得正在登陆的用户的关注的用户的信息
    //根据输入用户的id 得到一个关注的用户的列表
    List<User> userAttention(int id);

    //根据传入的用户名删除关注列表中的这个用户
    boolean deleteAttention(String username);

    boolean insertCollection(UserCollectArticle userCollectArticle);

    boolean insertThumb(UserThumbArticle userThumbArticle);

    boolean deleteCollection(UserCollectArticle userCollectArticle);

    boolean deleteThumb(UserThumbArticle userTHumbArticle);

    boolean setAttention(@Param("userid1") int userid1, @Param("userid2") int userid2);

    UserCollectArticle selectCollection(@Param("user_id") int user_id, @Param("article_id") int article_id);

    UserThumbArticle selectThumb(@Param("user_id") int user_id, @Param("article_id") int article_id);

    boolean addCollectionNum(@Param("id") int id);

    boolean addThumbNum(@Param("id") int id);

    boolean subCollectionNum(@Param("id") int id);

    boolean subThumbNum(@Param("id") int id);

    boolean changeHeadImage(@Param("headImage") String headImage, @Param("detailId")int detailId);

    //获取用户粉丝列表
    List<User> userFans(@Param("user_id1") int user_id1);
}
