package com.fc.rain.freecreate.moudel.ui.activity

import android.os.Bundle
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.base.BaseActivity
import com.fc.rain.freecreate.moudel.contract.HomeContract
import com.fc.rain.freecreate.moudel.presenter.HomePresenter

/**
 *
 * Describe # 主页
 *
 * Created by Rain on 2017/11/21.
 */
class HomeActivity : BaseActivity(), HomeContract.IHomeView {
    override fun initContract() {
        HomePresenter(this)
    }

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() {
    }

    override val layoutResID: Int
        get() = R.layout.activity_home

    var mPresenter: HomeContract.IHomePresenter? = null

    override fun setPresenter(presenter: HomeContract.IHomePresenter) {
        mPresenter = presenter
    }

    override fun receiveData() {
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.destroyView()
    }
}