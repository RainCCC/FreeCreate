package com.fc.rain.freecreate.moduel.contract

import com.fc.rain.freecreate.base.IBasePView
import com.fc.rain.freecreate.base.IBasePresenter
import com.fc.rain.freecreate.moduel.model.bean.MyUser

/**
 *
 * Describe #
 *
 * Created by Rain on 2017/11/22.
 */
class HomeContract {
    interface IHomeView : IBasePView<IHomePresenter> {
        fun refreshFriendListSuccess(friendList: MutableList<MyUser>?)
    }

    interface IHomePresenter : IBasePresenter {
        fun addFriendListener()
        fun getHxFriendList()
    }
}