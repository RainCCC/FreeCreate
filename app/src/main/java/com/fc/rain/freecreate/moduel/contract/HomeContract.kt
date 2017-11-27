package com.fc.rain.freecreate.moduel.contract

import com.fc.rain.freecreate.base.IBasePView
import com.fc.rain.freecreate.base.IBasePresenter

/**
 *
 * Describe #
 *
 * Created by Rain on 2017/11/22.
 */
class HomeContract {
    interface IHomeView : IBasePView<IHomePresenter> {
        fun receiveData()
    }

    interface IHomePresenter : IBasePresenter {
        fun refreshData()
    }
}