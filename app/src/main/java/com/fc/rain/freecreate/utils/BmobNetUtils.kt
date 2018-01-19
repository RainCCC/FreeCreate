package com.fc.rain.freecreate.utils

import android.content.Context
import cn.bmob.v3.BmobObject
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.*
import com.fc.rain.freecreate.moduel.contract.SuperListener
import com.fc.rain.freecreate.moduel.model.bean.MyUser
import com.fc.rain.freecreate.moduel.ui.activity.LoginActivity
import com.hyphenate.exceptions.HyphenateException
import com.qiongliao.qiongliaomerchant.hx.HxNetUtils


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

    //获取自己的用户id
    fun getSelfUserId(): String {
        return BmobUser.getObjectByKey("objectId").toString()
    }

    //获取自己的用户对象
    fun <T> getCurrentUser(clazz: Class<T>): T? {
        return BmobUser.getCurrentUser(clazz)
    }

    //保存好友信息到bmob
    fun saveFriend(callBack: SuperListener.RequestSaveBmobFriendListener?) {
        HxNetUtils.instance.getHxFriendList(object : SuperListener.RequestHxFriendListener {
            override fun requestSuccess(list: MutableList<String>) {
                var currentUser = getCurrentUser(MyUser::class.java)
                currentUser?.let {
                    currentUser.setValue("friendList", list)
                    currentUser.update(object : UpdateListener() {
                        override fun done(p0: BmobException?) {
                            if (p0 == null) {
                                callBack?.requestSuccess(list)
                            } else {
                                callBack?.requestFail(p0, null)
                            }
                            callBack?.requestDone()
                        }
                    })
                }
            }

            override fun requestFail(e: HyphenateException?) {
                callBack?.requestFail(null, e)
                callBack?.requestDone()
            }
        })

    }

    //获取服务器好友列表
    @Synchronized
    fun getFriendDetailMessage(callBack: SuperListener.RequestBmobFriendListListener?) {
        var query = BmobQuery<MyUser>()
        query.getObject(getSelfUserId(), object : QueryListener<MyUser>() {
            override fun done(p0: MyUser?, p1: BmobException?) {
                if (p0 != null) {
                    var friendList = p0.friendList
                    var queryList: MutableList<BmobQuery<MyUser>> = arrayListOf()
                    friendList.forEach {
                        var bmobQuery = BmobQuery<MyUser>()
                        bmobQuery.addWhereEqualTo("objectId", it.subSequence(2, it.length))
                        queryList.add(bmobQuery)
                    }
                    var mainQuery = BmobQuery<MyUser>()
                    mainQuery.or(queryList)
                    mainQuery.findObjects(object : FindListener<MyUser>() {
                        override fun done(p0: MutableList<MyUser>?, p1: BmobException?) {
                            if (p0 != null) {
                                callBack?.requestSuccess(p0)
                            } else {
                                callBack?.requestFail(p1)
                            }
                            callBack?.requestDone()
                        }
                    })
                } else {
                    callBack?.requestFail(p1)
                    callBack?.requestDone()
                }
            }
        })

    }

    //注册帐号到Bmob
    fun registerToBmob(mUser: MyUser, mListener: SuperListener.RegisterBmobListener) {
        mUser.signUp(object : SaveListener<MyUser>() {
            override fun done(p0: MyUser?, p1: BmobException?) {
                if (p1 == null) {
                    mListener.createSuccess(p0)
                } else {
                    LogUtil.e("bmob:" + p1.message)
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