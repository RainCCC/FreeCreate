package com.fc.rain.freecreate.utils

import android.content.Context
import cn.bmob.v3.BmobObject
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.fc.rain.freecreate.moduel.contract.SuperListener
import com.fc.rain.freecreate.moduel.model.bean.FriendBean
import com.fc.rain.freecreate.moduel.model.bean.MyUser
import com.fc.rain.freecreate.moduel.ui.activity.LoginActivity
import cn.bmob.v3.listener.LogInListener


/**
 *
 * Describe #
 *
 * Created by Rain on 2018/1/18.
 */
object BmobNetUtils {

    //更新好友列表将hx的好友列表更新到Bmob中
    fun updateBmobFriendList(list: MutableList<String>?) {
//        var bmobQuery = BmobQuery<MyUser>()
//        bmobQuery.addWhereContainsAll("username", list)
//        bmobQuery.findObjects(object : FindListener<MyUser>() {
//            override fun done(p0: MutableList<MyUser>?, p1: BmobException?) {
//                if (p1 == null) {
//                    //先查询当前用户的好友数据
//                    findObjectsFriend(object : FindListener<FriendBean>() {
//                        override fun done(friendList: MutableList<FriendBean>?, e: BmobException?) {
//                            if (e == null) {
//                                //如果存在
//                                var friendBean = friendList?.get(0)
//                                //添加关联更新好友列表
//                                var bmobRelation = BmobRelation()
//                                p0?.forEach { bmobRelation.add(it) }
//                                friendBean?.friendUser = bmobRelation
//                                updateFriendList(friendBean, object : UpdateListener() {
//                                    override fun done(e1: BmobException?) {
//                                        if (e1 == null) {
//                                        } else {
//                                            LogUtil.i("bmob", "失败：" + e1.message + "," + e1.errorCode)
//                                        }
//                                    }
//                                })
//                            } else {
//                                LogUtil.i("bmob", "失败：" + e.message + "," + e.errorCode)
//                            }
//                        }
//                    })
//                } else {
//                    LogUtil.i("bmob", "失败：" + p1.message + "," + p1.errorCode)
//                }
//            }
//        })

    }

    //获取自己的用户名
    fun getSelfUserName(): String {
        return BmobUser.getObjectByKey("username").toString()
    }

    //查询自己的好友bean
    fun findFriendList(mUserName: String, listener: FindListener<FriendBean>) {
        var bmobQuery = BmobQuery<FriendBean>()
        bmobQuery.addWhereEqualTo("mUserName", mUserName)
        bmobQuery.findObjects(listener)
    }

    //添加用户信息到好友bean
    fun saveFriend() {
//        var friendBean = FriendBean()
//        friendBean.setmUserName(BmobUser.getObjectByKey("username").toString())
//        friendBean.save(object : SaveListener<String>() {
//            override fun done(objectId: String?, e: BmobException?) {
//                if (e == null) {
//                } else {
//                    LogUtil.i("bmob", "失败：" + e.message + "," + e.errorCode)
//                }
//            }
//        })
    }

    //注册帐号到Bmob
    fun registerToBmob(mUser: MyUser, mListener: SuperListener.RegisterBmobListener) {
        mUser.signUp(object : SaveListener<MyUser>() {
            override fun done(p0: MyUser?, p1: BmobException?) {
                if (p1 == null) {
                    mListener.createSuccess(p0)
                } else {
                    LogUtil.e("bmob:"+p1.message)
                    mListener.createFail(p1)
                }
                mListener.createDone()
            }
        })
    }

    //更新数据
    fun update(any: BmobObject, mCallBack: UpdateListener) {
        any.update(any.objectId, mCallBack)
    }

    //添加数据
    fun save(any: BmobObject, mCallBack: SaveListener<String>) {
        any.save(mCallBack)
    }

    //bmob退出登录
    fun logout(mContext: Context) {
        SPUtils.clear(mContext)
        //bmob退出登录
        //BmobUser.getCurrentUser().delete()
        BmobUser.logOut()
        LoginActivity.startActivity(mContext)
        AppManager.finishAllExceptActivity(LoginActivity::class.java)
    }

    fun login(username: String, password: String, mCallBack: LogInListener<MyUser>) {
        BmobUser.loginByAccount(username, password, mCallBack)

    }
}