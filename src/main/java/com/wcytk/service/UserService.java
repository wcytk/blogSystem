package com.wcytk.service;

import com.wcytk.entity.*;

import java.sql.Timestamp;
import java.util.List;

public interface UserService {
    public boolean updateUserDetail(DetailUser detailUser);

    public UserAttention selectAttention(int userId1, int userId2);

    public List<User> ListAll();

    public User findByName(String username);

    public User findById(int id);

    public boolean find(int id);

    //�����û���id ����һ��һ�������ѯ
    public DetailUser showDetailInfo(int detailId);

    public void insertUser(User user);

    public boolean insertUserDetail(int detailId);

    public User login(User user);

    public void updateUser(User user);

    public void logout(User user);

    public List<User> admin_listAll();

    public void admin_updateUser(User user);

    public void admin_delete(int id);

    public void admin_addUser(User user);

    public List<User> admin_search(String username);

    public void deleteById(int id);

    public List<User> selectUserArticle(int id);

    public List<User> selectUserThumbArticle(int id);

    public boolean insertCollection(UserCollectArticle userCollectArticle);

    public boolean insertThumb(UserThumbArticle userTHumbArticle);

    public boolean deleteCollection(UserCollectArticle userCollectArticle);

    public boolean deleteThumb(UserThumbArticle userThumbArticle);

    public List<User> userAttention(int userId1);

    //���service���еķ�����ɾ��ĳһ����ע���ˣ����ɾ���ɹ����ͻ᷵��true��
//    ���ɾ�����ɹ��ͻ᷵��false   ������ǰ�˴�ֵ
    public boolean deleteAttention(String username);

    public boolean setAttention(int userid1, int userid2);

    public UserCollectArticle selectCollection(int user_id, int article_id);

    public UserThumbArticle selectThumb(int user_id, int article_id);

    public boolean addCollectionNum(int id);

    public boolean addThumbNum(int id);

    public boolean subCollectionNum(int id);

    public boolean subThumbNum(int id);

    public boolean changeHeadImage(String headImage, int detailId);

    public List<User> getFans(int userId2);
}
