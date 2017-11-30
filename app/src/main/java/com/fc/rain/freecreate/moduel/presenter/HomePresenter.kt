package com.fc.rain.freecreate.moduel.presenter

import android.content.Context
import com.fc.rain.freecreate.moduel.contract.HomeContract
import com.fc.rain.freecreate.moduel.model.FriendModel

/**
 *
 * Describe #
 *
 * Created by Rain on 2017/11/22.
 */
class HomePresenter(var mContext: Context, var mView: HomeContract.IHomeView?) : HomeContract.IHomePresenter {
    override fun addFriendListener() {
        FriendModel.addHxFriendListener(mContext)
    }

    override fun getHxFriendList() {
        //保存当前用户friend数据
        FriendModel.saveFriend()
        //同步hx到bmob好友列表
        FriendModel.reFreshFriendDataToBmob(mContext)
    }


    init {
        mView?.setPresenter(this)
    }

    override fun destroyView() {
        mView = null
    }

    override fun start() {


    }

}