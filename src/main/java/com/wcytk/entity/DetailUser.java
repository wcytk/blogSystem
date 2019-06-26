package com.wcytk.entity;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class DetailUser {
    private int detailId;
    private String neckname;
    private String sex;
    private String birthday;
    private String place;
    private String personal_sign;
    private String headImage;

    public DetailUser() {
    }

    public DetailUser(int detailId, String neckname, String sex,
                      String birthday, String place, String personal_sign, String headImage) {
        this.detailId = detailId;
        this.neckname = neckname;
        this.sex = sex;
        this.birthday = birthday;
        this.place = place;
        this.personal_sign = personal_sign;
        this.headImage = headImage;

    }

    public DetailUser(int detailId, String neckname, String sex, String birthday, String place, String personal_sign) {
        this.detailId = detailId;
        this.neckname = neckname;
        this.sex = sex;
        this.birthday = birthday;
        this.place = place;
        this.personal_sign = personal_sign;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public String getNeckname() {
        return neckname;
    }

    public void setNeckname(String neckname) {
        this.neckname = neckname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPersonal_sign() {
        return personal_sign;
    }

    public void setPersonal_sign(String personal_sign) {
        this.personal_sign = personal_sign;
    }

}
