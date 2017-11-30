package com.fc.rain.freecreate.moduel.model.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Describe #
 * <p>
 * Created by Rain on 2017/11/29.
 */

public class MyUser extends BmobUser {
    String sex;
    Integer age;
    String address;
    BmobFile headurl;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BmobFile getHeadurl() {
        return headurl;
    }

    public void setHeadurl(BmobFile headurl) {
        this.headurl = headurl;
    }
}
