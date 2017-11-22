package com.fc.rain.freecreate.moudel.presenter

import com.fc.rain.freecreate.moudel.contract.HomeContract

/**
 *
 * Describe #
 *
 * Created by Rain on 2017/11/22.
 */
class HomePresenter(var mView: HomeContract.IHomeView?) : HomeContract.IHomePresenter {
    init {
        mView?.setPresenter(this)
    }

    override fun destroyView() {
        mView = null
    }

    override fun start() {
    }

}