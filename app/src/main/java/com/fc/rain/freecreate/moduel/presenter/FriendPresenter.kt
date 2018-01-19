package com.fc.rain.freecreate.moduel.presenter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import cn.bmob.v3.exception.BmobException
import com.fc.rain.freecreate.base.Constant
import com.fc.rain.freecreate.moduel.contract.FriendContract
import com.fc.rain.freecreate.moduel.contract.SuperListener
import com.fc.rain.freecreate.moduel.model.bean.MyUser
import com.fc.rain.freecreate.utils.BmobNetUtils
import com.hyphenate.exceptions.HyphenateException

/**
 *
 * Describe # 好友presenter
 *
 * Created by Rain on 2017/11/29.
 */
class FriendPresenter(var mContext: Context, var mView: FriendContract.View?) : FriendContract.Presenter {

    var receiver: FriendReceiver? = null

    override fun getFriendList() {
//        mView?.showLoading()
        BmobNetUtils.getFriendDetailMessage(object : SuperListener.RequestBmobFriendListListener {
            override fun requestSuccess(list: MutableList<MyUser>) {
                mView?.refreshFriendListSuccess(list)
            }

            override fun requestFail(bmobError: BmobException?) {
                bmobError?.message?.let { mView?.toastMessage(it) }
            }

            override fun requestDone() {
//                mView?.hideLoading()
            }

        })
    }

    init {
        mView?.setPresenter(this)
    }

    override fun destroyView() {
        receiver?.let {
            mContext.unregisterReceiver(it)
        }
        mView = null
    }

    inner class FriendReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Constant.BroadcastReceiverAction.REFRESHFRIENDLISTACTION) {
                getFriendList()
            }
        }

    }

    override fun start() {
        if (receiver == null) {
            receiver = FriendReceiver()
            mContext.registerReceiver(receiver, IntentFilter(Constant.BroadcastReceiverAction.REFRESHFRIENDLISTACTION))
        }
        BmobNetUtils.saveFriend(object : SuperListener.RequestSaveBmobFriendListener {
            override fun requestSuccess(list: MutableList<String>) {
            }

            override fun requestFail(bmobError: BmobException?, hxError: HyphenateException?) {
            }

            override fun requestDone() {
                getFriendList()
            }
        })
    }

}