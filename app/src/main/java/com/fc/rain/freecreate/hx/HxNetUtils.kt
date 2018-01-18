package com.qiongliao.qiongliaomerchant.hx

import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.DialogInterface
import android.os.Build
import android.support.annotation.RequiresApi
import cn.bmob.v3.BmobUser
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.moduel.contract.SuperListener
import com.fc.rain.freecreate.moduel.ui.activity.LoginActivity
import com.fc.rain.freecreate.utils.AppManager
import com.fc.rain.freecreate.utils.BmobNetUtils
import com.fc.rain.freecreate.utils.LogUtil
import com.fc.rain.freecreate.utils.SPUtils
import com.hyphenate.*
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.hyphenate.easeui.EaseUI
import com.hyphenate.exceptions.HyphenateException
import com.hyphenate.hx.HxHelper
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast


/**
 *
 * Describe # 环信bmob操作
 *
 * Created by Rain on 2017/12/19.
 */
class HxNetUtils private constructor() {

    companion object {
        var instance = HxNetUtils()
    }

    //好友状态监听
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
//                    refreshFriendList(mContext)
                }
            }


            override fun onContactAdded(username: String) {
                //增加了联系人时回调此方法
                mContext.runOnUiThread {
                    toast(username + mContext.getString(R.string.already_add_friend))
//                    refreshFriendList(mContext)
                }
            }
        })
    }

    //同意添加好友
    fun acceptInvitation(username: String) {
        EMClient.getInstance().contactManager().acceptInvitation(username)
    }

    //拒绝添加好友
    fun declineInvitation(username: String) {
        EMClient.getInstance().contactManager().declineInvitation(username)
    }

    //获取HX所有好友
    fun getHxFriendList(): MutableList<String> {
        var allContactsFromServer = EMClient.getInstance().contactManager().allContactsFromServer
        return allContactsFromServer
    }


    //注册环信
    fun createAccountToHx(mContext: Context, username: String, password: String, mListener: SuperListener.RegisterHxListener) {
        mContext.async {
            try {
                //注册失败会抛出HyphenateException
                EMClient.getInstance().createAccount(username, password)//同步方法
                mContext.runOnUiThread {
                    mListener.createSuccess(username, password)
                }
            } catch (e: HyphenateException) {
                mContext.runOnUiThread {
                    e.description.let {
                        LogUtil.e("hx:" + e.message)
                        mListener.createFail(e)
                    }
                }
            }
            mContext.runOnUiThread {
                mListener.createDone()
            }
        }
    }


    //环信连接监听
    fun addConnectionListener(mContext: Context) {
        mContext.doAsync {
            EMClient.getInstance().addConnectionListener(object : EMConnectionListener {
                override fun onConnected() {
                    LogUtil.i("loginHxSuccess")
                }

                override fun onDisconnected(errorCode: Int) {
                    mContext.runOnUiThread {
                        when (errorCode) {
                        // 显示帐号已经被移除
                            EMError.USER_REMOVED -> {
//                                toast(getString(R.string.hx_account_removed))
                                offLineOperation(mContext)
                            }
                        // 显示帐号在其他设备登录
                            EMError.USER_LOGIN_ANOTHER_DEVICE -> {
//                                toast(getString(R.string.hx_account_login))
                                offLineOperation(mContext)
                            }
                            else -> {
//                                if (NetUtils.hasNetwork(mContext))
//                                //连接不到聊天服务器
//                                else
//                                //当前网络不可用，请检查网络设置
////                                    toast(getString(R.string.hx_network_error))
                            }
                        }

                    }
                }

            })
        }
    }

    /**
     * 被挤下线的操作
     */
    private fun offLineOperation(mContext: Context) {
        //退出登录
        logoutHx(object : EMCallBack {
            override fun onSuccess() {
                mContext.runOnUiThread {
                    LogUtil.i("logoutHxSuccess:")
                    BmobNetUtils.logout(mContext)
                }
            }

            override fun onProgress(progress: Int, status: String?) {
            }

            override fun onError(code: Int, error: String?) {
                mContext.runOnUiThread {
                    LogUtil.i("logoutHxFail:" + error + ":" + code)
                    BmobNetUtils.logout(mContext)
                }
            }
        })
    }

    //登录环信
    fun loginHx(userName: String?, password: String?, emCallBack: EMCallBack) {
        //回调成功的方法运行在子线程中
        EMClient.getInstance().login(userName, password, emCallBack)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
            /**
             * 收到消息通知栏
             */
    fun notification(context: Context) {
        var mNotifyMgr = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        var contentIntent = PendingIntent.getActivity(
//                context, 0, Intent(context, ConversationActivity::class.java), 0)

        var notification = Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Free")
                .setContentText("收到了一条消息")
                .setAutoCancel(true)
//                .setContentIntent(contentIntent)
                .build()// getNotification()
        mNotifyMgr.notify(100, notification)
    }

    //退出登录
    fun logoutHx(emCallBack: EMCallBack) {
        HxHelper.getInstance().logout(true, emCallBack)
    }

    //环信消息监听
    fun addEMMessageListener(mContext: Context) {
        if (messageListener == null) {
            messageListener = MyEMMessageListener(mContext)
        }
        messageListener?.let { mContext.doAsync { EMClient.getInstance().chatManager().addMessageListener(it) } }
    }

    var messageListener: MyEMMessageListener? = null

    inner class MyEMMessageListener(var mContext: Context) : EMMessageListener {

        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
        override fun onMessageReceived(messages: List<EMMessage>) {
            if (!EaseUI.getInstance().hasForegroundActivies()) {
                notification(mContext)
            }
            //收到消息
//            messages.forEach {
//                val intent = Intent(Constant.ReceiverAction.MESSAGEACTION)
//                intent.putExtra("EMMessageList", it)
//                mContext.sendBroadcast(intent)
//                //新的对话需要保存会话并且设置头像及其名字
//                if (!HxUserStrs.contains(it.from)) {
//                    getConversationListAndSaveService(object : JsonCallback<BaseBean>(BaseBean::class.java) {
//                        override fun onSuccess(response: Response<BaseBean>?) {
//                            UpdateConversationData(object : IUpdateIconAndName {
//                                override fun updateIconAndNameSuccess() {
//
//                                }
//                            })
//                        }
//                    })
//                }
//            }
        }


        override fun onCmdMessageReceived(messages: List<EMMessage>) {
            //收到透传消息
        }

        override fun onMessageRead(messages: List<EMMessage>) {
            //收到已读回执
        }

        override fun onMessageDelivered(message: List<EMMessage>) {
            //收到已送达回执
        }

        override fun onMessageRecalled(messages: List<EMMessage>) {
            //消息被撤回
        }

        override fun onMessageChanged(message: EMMessage, change: Any) {
            //消息状态变动
        }
    }

    //移除环信消息监听
    fun removeEMMessageListener(mContext: Context) {
        messageListener?.let { mContext.doAsync { EMClient.getInstance().chatManager().removeMessageListener(it) } }
    }
}