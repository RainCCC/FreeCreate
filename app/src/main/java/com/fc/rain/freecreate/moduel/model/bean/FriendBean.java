package com.fc.rain.freecreate.moduel.model.bean;


import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Describe # 好友bean
 * <p>
 * Created by Rain on 2017/11/29.
 */

public class FriendBean extends BmobObject {

    String mUserName;

    BmobRelation friendUser;

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public BmobRelation getFriendUser() {
        return friendUser;
    }

    public void setFriendUser(BmobRelation friendUser) {
        this.friendUser = friendUser;
    }
}
