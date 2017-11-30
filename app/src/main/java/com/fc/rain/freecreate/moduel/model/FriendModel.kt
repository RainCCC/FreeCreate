package com.fc.rain.freecreate.moduel.model

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.datatype.BmobRelation
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.moduel.model.bean.FriendBean
import com.fc.rain.freecreate.moduel.model.bean.MyUser
import com.fc.rain.freecreate.utils.LogUtil
import com.hyphenate.EMContactListener
import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast


/**
 *
 * Describe # 请求封装类
 *
 * Created by Rain on 2017/11/29.
 */
class FriendModel {

    companion object {
        fun addHxFriendListener(mContext: Context) {
            //好友状态监听
            EMClient.getInstance().contactManager().setContactListener(object : EMContactListener {
                override fun onFriendRequestAccepted(username: String?) {
                    //好友请求被同意
                    mContext.runOnUiThread {
                        toast(username + mContext.getString(R.string.already_agree_your_request))
                    }
                }

                override fun onFriendRequestDeclined(username: String?) {
                    //好友请求被拒绝
                    mContext.runOnUiThread { toast(username + mContext.getString(R.string.already_disagree_your_request)) }
                }

                override fun onContactInvited(username: String, reason: String) {
                    mContext.runOnUiThread {
                        //收到好友邀请
                        var builder = AlertDialog.Builder(mContext)
                        builder.setTitle(mContext.getString(R.string.request_friend))
                        builder.setMessage(username + mContext.getString(R.string.request_add_friend))
                        builder.setCancelable(false)
                        builder.setPositiveButton(mContext.getString(R.string.agree), object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                try {
                                    acceptInvitation(username)
                                    toast(mContext.getString(R.string.agree_request_friend))
                                } catch (e: HyphenateException) {
                                    toast(e.description)
                                }
                                dialog?.dismiss()
                            }

                        })
                        builder.setNegativeButton(mContext.getString(R.string.disagree), object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                try {
                                    declineInvitation(username)
                                    toast(mContext.getString(R.string.disagree_request_friend))
                                } catch (e: HyphenateException) {
                                    toast(e.description)
                                }

                                dialog?.dismiss()
                            }

                        })
                        builder.show()
                    }

                }

                override fun onContactDeleted(username: String) {
                    //被删除时回调此方法
                    mContext.runOnUiThread {
                        toast(username + mContext.getString(R.string.already_delete_friend))
                        reFreshFriendDataToBmob(mContext)
                    }
                }


                override fun onContactAdded(username: String) {
                    //增加了联系人时回调此方法
                    mContext.runOnUiThread {
                        toast(username + mContext.getString(R.string.already_add_friend))
                        reFreshFriendDataToBmob(mContext)
                    }
                }
            })
        }

        fun acceptInvitation(username: String) {
            //同意添加好友
            EMClient.getInstance().contactManager().acceptInvitation(username)
        }

        fun declineInvitation(username: String) {
            //拒绝添加好友
            EMClient.getInstance().contactManager().declineInvitation(username)
        }

        /**
         * 同步HX好友列表到bmob
         */
        fun reFreshFriendDataToBmob(mContext: Context) {
            mContext.doAsync {
                try {
                    var hxFriendList = getHxFriendList()
                    mContext.runOnUiThread { updateBmobFriendList(hxFriendList) }
                } catch (e: HyphenateException) {
                    mContext.runOnUiThread { toast(e.description) }
                }

            }
        }

        fun getHxFriendList(): MutableList<String> {
            //获取HX所有好友
            var allContactsFromServer = EMClient.getInstance().contactManager().allContactsFromServer
            return allContactsFromServer
        }

        //更新好友列表将hx的好友列表更新到Bmob中
        fun updateBmobFriendList(list: MutableList<String>?) {
            var bmobQuery = BmobQuery<MyUser>()
            bmobQuery.addWhereContainsAll("username", list)
            bmobQuery.findObjects(object : FindListener<MyUser>() {
                override fun done(p0: MutableList<MyUser>?, p1: BmobException?) {
                    if (p1 == null) {
                        //先查询当前用户的好友数据
                        findObjectsFriend(object : FindListener<FriendBean>() {
                            override fun done(friendList: MutableList<FriendBean>?, e: BmobException?) {
                                if (e == null) {
                                    //如果存在
                                    var friendBean = friendList?.get(0)
                                    //添加关联更新好友列表
                                    var bmobRelation = BmobRelation()
                                    p0?.forEach { bmobRelation.add(it) }
                                    friendBean?.friendUser = bmobRelation
                                    updateFriendList(friendBean, object : UpdateListener() {
                                        override fun done(e1: BmobException?) {
                                            if (e1 == null) {
                                            } else {
                                                LogUtil.i("bmob", "失败：" + e1.message + "," + e1.errorCode)
                                            }
                                        }
                                    })
                                } else {
                                    LogUtil.i("bmob", "失败：" + e.message + "," + e.errorCode)
                                }
                            }
                        })
                    } else {
                        LogUtil.i("bmob", "失败：" + p1.message + "," + p1.errorCode)
                    }
                }
            })

        }

        //存好友到bmob
        fun updateFriendList(friendList: FriendBean?, listener: UpdateListener) {
            friendList?.update(listener)
        }

        //查询自己的好友bean
        fun findObjectsFriend(listener: FindListener<FriendBean>) {
            var bmobQuery = BmobQuery<FriendBean>()
            bmobQuery.addWhereEqualTo("mUserName", BmobUser.getObjectByKey("username").toString())
            bmobQuery.findObjects(listener)
        }

        //添加用户信息到好友bean
        fun saveFriend() {
            var friendBean = FriendBean()
            friendBean.setmUserName(BmobUser.getObjectByKey("username").toString())
            friendBean.save(object : SaveListener<String>() {
                override fun done(objectId: String?, e: BmobException?) {
                    if (e == null) {
                    } else {
                        LogUtil.i("bmob", "失败：" + e.message + "," + e.errorCode)
                    }
                }
            })
        }

        /**
         * 获取好友列表
         */
        fun getFriendList(listener: FindListener<MyUser>) {
            findObjectsFriend(object : FindListener<FriendBean>() {
                override fun done(friendList: MutableList<FriendBean>?, e: BmobException?) {
                    if (e == null) {
                        var bean = friendList?.get(0)
                        var query = BmobQuery<MyUser>()
                        query.addWhereRelatedTo("friendUser", BmobPointer(bean))
                        query.findObjects(listener)
                    }
                }
            })
        }
    }
}