package com.fc.rain.freecreate.moudel.ui.activity

import android.support.v4.app.Fragment
import android.view.View
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.base.BaseActivity
import com.fc.rain.freecreate.moudel.contract.HomeContract
import com.fc.rain.freecreate.moudel.presenter.HomePresenter
import com.fc.rain.freecreate.moudel.ui.fragment.HomeFragment
import com.fc.rain.freecreate.moudel.ui.fragment.MessageFragment
import com.fc.rain.freecreate.moudel.ui.fragment.MyFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.title_bar.*

/**
 *
 * Describe # 主页
 *
 * Created by Rain on 2017/11/21.
 */
class HomeActivity : BaseActivity(), HomeContract.IHomeView {
    override fun layoutResID(): Int {
        return R.layout.activity_home
    }

    override fun openDefaultHXListener(): Int {
        return DEFAULTHXLISTENER
    }

    var fragments: MutableList<Fragment>? = arrayListOf()

    override fun initContract() {
        HomePresenter(this)
    }

    override fun initView() {
        iv_back.visibility = View.GONE
        fragments?.add(HomeFragment())
        fragments?.add(MessageFragment())
        fragments?.add(MyFragment())
//        fragments?.forEach { FragmentUtils.add(supportFragmentManager, it, R.id.fl_fragment, true) }
//        fragments?.get(0)?.let { FragmentUtils.show(it) }
    }

    override fun initData() {
    }

    override fun initListener() {
//        btn_home.setOnClickListener {
//            fragments?.forEach { FragmentUtils.hide(it) }
//            fragments?.get(0)?.let { FragmentUtils.show(it) }
//        }
//        btn_message.setOnClickListener {
//            fragments?.forEach { FragmentUtils.hide(it) }
//            fragments?.get(1)?.let { FragmentUtils.show(it) }
//        }
//        btn_my.setOnClickListener {
//            fragments?.forEach { FragmentUtils.hide(it) }
//            fragments?.get(2)?.let { FragmentUtils.show(it) }
//        }
    }


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