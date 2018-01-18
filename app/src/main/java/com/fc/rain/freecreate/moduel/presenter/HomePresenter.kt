package com.fc.rain.freecreate.moduel.presenter

import android.content.Context
import com.fc.rain.freecreate.moduel.contract.HomeContract

/**
 *
 * Describe #
 *
 * Created by Rain on 2017/11/22.
 */
class HomePresenter(var mContext: Context, var mView: HomeContract.IHomeView?) : HomeContract.IHomePresenter {
    override fun addFriendListener() {
    }

    override fun getHxFriendList() {
        //保存当前用户friend数据
//        HxNetUtils.instance.saveFriend()
        //同步hx到bmob好友列表
//        HxNetUtils.instance.reFreshFriendDataToBmob(mContext)
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