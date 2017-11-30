package com.fc.rain.freecreate.moduel.contract

import com.fc.rain.freecreate.base.IBasePView
import com.fc.rain.freecreate.base.IBasePresenter
import com.fc.rain.freecreate.moduel.model.bean.MyUser

/**
 *
 * Describe # 好友presenter
 *
 * Created by Rain on 2017/11/29.
 */
class FriendContract {
    interface View : IBasePView<Presenter> {
        fun refreshFriendListSuccess(friendList: MutableList<MyUser>?)
    }

    interface Presenter : IBasePresenter {
        fun getFriendList()
    }
}