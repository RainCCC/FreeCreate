package com.fc.rain.freecreate.moduel.model.bean;


import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Describe # 好友bean
 * <p>
 * Created by Rain on 2017/11/29.
 */

public class FriendBean extends BmobObject {

    String userId;
    String nickName;
    BmobFile headurl;
    Integer age;
    String sex;
    String address;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public BmobFile getHeadurl() {
        return headurl;
    }

    public void setHeadurl(BmobFile headurl) {
        this.headurl = headurl;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
