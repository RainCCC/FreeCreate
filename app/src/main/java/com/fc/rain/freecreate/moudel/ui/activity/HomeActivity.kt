package com.fc.rain.freecreate.moudel.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.base.BaseActivity
import com.fc.rain.freecreate.moudel.contract.HomeContract
import com.fc.rain.freecreate.moudel.presenter.HomePresenter
import com.fc.rain.freecreate.moudel.ui.fragment.HomeFragment
import com.fc.rain.freecreate.moudel.ui.fragment.MessageFragment
import com.fc.rain.freecreate.moudel.ui.fragment.MyFragment
import com.fc.rain.freecreate.utils.FragmentUtils
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.title_bar.*
import org.jetbrains.anko.toast

/**
 *
 * Describe # 主页
 *
 * Created by Rain on 2017/11/21.
 */
class HomeActivity : BaseActivity(), HomeContract.IHomeView {

    var homeFragment: HomeFragment? = null
    var messageFragment: MessageFragment? = null
    var myFragment: MyFragment? = null
    override fun layoutResID(): Int {
        return R.layout.activity_home
    }

    override fun openDefaultHXListener(): Int {
        return DEFAULTHXLISTENER
    }

    var fragments: MutableList<Fragment>? = arrayListOf()

    override fun initContract(savedInstanceState: Bundle?) {
        HomePresenter(this)
        if (savedInstanceState == null) {
            homeFragment = HomeFragment()
            messageFragment = MessageFragment()
            myFragment = MyFragment()
            FragmentUtils.add(supportFragmentManager, R.id.fl_fragment, homeFragment, "HomeFragment")
            FragmentUtils.add(supportFragmentManager, R.id.fl_fragment, messageFragment, "MessageFragment")
            FragmentUtils.add(supportFragmentManager, R.id.fl_fragment, myFragment, "MyFragment")
        } else {
            homeFragment = FragmentUtils.findFragmentByTag(supportFragmentManager, "HomeFragment") as HomeFragment
            messageFragment = FragmentUtils.findFragmentByTag(supportFragmentManager, "MessageFragment") as MessageFragment
            myFragment = FragmentUtils.findFragmentByTag(supportFragmentManager, "MyFragment") as MyFragment
        }

        fragments?.clear()
        homeFragment?.let { fragments?.add(it) }
        messageFragment?.let { fragments?.add(it) }
        myFragment?.let { fragments?.add(it) }
        fragments?.let { FragmentUtils.hideAll(supportFragmentManager, it) }
        fragments?.get(0)?.let { FragmentUtils.show(supportFragmentManager, it) }
    }

    override fun initView() {
        supportFragmentManager
        iv_back.visibility = View.GONE

    }

    override fun initData() {
    }

    override fun initListener() {
        btn_home.setOnClickListener {
            fragments?.get(0)?.let { FragmentUtils.showHideOher(supportFragmentManager, fragments, it) }
        }
        btn_message.setOnClickListener {
            fragments?.get(1)?.let { FragmentUtils.showHideOher(supportFragmentManager, fragments, it) }
        }
        btn_my.setOnClickListener {
            fragments?.get(2)?.let { FragmentUtils.showHideOher(supportFragmentManager, fragments, it) }
        }
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