package com.fc.rain.freecreate.moduel.presenter

import android.content.Context
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.moduel.contract.FriendContract
import com.fc.rain.freecreate.moduel.model.FriendModel
import com.fc.rain.freecreate.moduel.model.bean.MyUser

/**
 *
 * Describe # 好友presenter
 *
 * Created by Rain on 2017/11/29.
 */
class FriendPresenter(var mContext: Context, var mView: FriendContract.View?) : FriendContract.Presenter {

    override fun getFriendList() {
        mView?.showLoading()
        FriendModel.getFriendList(object : FindListener<MyUser>() {
            override fun done(p0: MutableList<MyUser>?, p1: BmobException?) {
                if (p1 == null) {
                    mView?.refreshFriendListSuccess(p0)
                } else {
                    mView?.toastMessage(mContext.getString(R.string.get_friend_list_fail) + p1.message)
                }
                mView?.hideLoading()
            }
        })
    }

    var mModel: FriendModel? = null

    init {
        mView?.setPresenter(this)
        mModel = FriendModel()
    }

    override fun destroyView() {
        mView = null
        mModel = null
    }

    override fun start() {
        getFriendList()
    }

}