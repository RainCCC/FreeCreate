package com.fc.rain.freecreate.moduel.presenter

import android.content.Context
import com.fc.rain.freecreate.moduel.contract.FriendContract

/**
 *
 * Describe # 好友presenter
 *
 * Created by Rain on 2017/11/29.
 */
class FriendPresenter(var mContext: Context, var mView: FriendContract.View?) : FriendContract.Presenter {

    override fun getFriendList() {
//        mView?.showLoading()
//        HxNetUtils.instance.getFriendList(object : FindListener<MyUser>() {
//            override fun done(p0: MutableList<MyUser>?, p1: BmobException?) {
//                if (p1 == null) {
//                    mView?.refreshFriendListSuccess(p0)
//                } else {
//                    mView?.toastMessage(mContext.getString(R.string.get_friend_list_fail) + p1.message)
//                }
//                mView?.hideLoading()
//            }
//        })
    }


    init {
        mView?.setPresenter(this)
    }

    override fun destroyView() {
        mView = null
    }

    override fun start() {
        getFriendList()
    }

}