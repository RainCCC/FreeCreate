package com.fc.rain.freecreate.moduel.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.view.View
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.base.BaseActivity
import com.fc.rain.freecreate.moduel.contract.HomeContract
import com.fc.rain.freecreate.moduel.model.bean.MyUser
import com.fc.rain.freecreate.moduel.presenter.HomePresenter
import com.fc.rain.freecreate.moduel.ui.fragment.HomeFragment
import com.fc.rain.freecreate.moduel.ui.fragment.MessageFragment
import com.fc.rain.freecreate.moduel.ui.fragment.MyFragment
import com.fc.rain.freecreate.utils.AppActivityManager
import com.fc.rain.freecreate.utils.FragmentUtils
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.title_bar.*

/**
 *
 * Describe # 主页
 *
 * Created by Rain on 2017/11/21.
 */
class HomeActivity : BaseActivity(), HomeContract.IHomeView {
    override fun refreshFriendListSuccess(friendList: MutableList<MyUser>?) {

    }

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
        HomePresenter(this,this)
        if (savedInstanceState == null) {
            homeFragment = HomeFragment()
            messageFragment = MessageFragment()
            myFragment = MyFragment()
            FragmentUtils.add(supportFragmentManager, R.id.fl_fragment, homeFragment, "HomeFragment")
            FragmentUtils.add(supportFragmentManager, R.id.fl_fragment, messageFragment, "MessageFragment")
            FragmentUtils.add(supportFragmentManager, R.id.fl_fragment, myFragment, "MyFragment")
        } else {
            toastMessage("ssssfafd")
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
        mPresenter?.addFriendListener()
        rb_home.setOnClickListener { fragments?.get(0)?.let { FragmentUtils.showHideOher(supportFragmentManager, fragments, it) } }
        rb_message.setOnClickListener { fragments?.get(1)?.let { FragmentUtils.showHideOher(supportFragmentManager, fragments, it) } }
        rb_my.setOnClickListener { fragments?.get(2)?.let { FragmentUtils.showHideOher(supportFragmentManager, fragments, it) } }
    }


    var mPresenter: HomeContract.IHomePresenter? = null

    override fun setPresenter(presenter: HomeContract.IHomePresenter) {
        mPresenter = presenter
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.destroyView()
    }

    var exitTime: Long = 0

    /*
     * 重写onKeyDown方法
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //2s之内按返回键就会推出

            if (System.currentTimeMillis() - exitTime > 2000) {
                toastMessage(getString(R.string.exit_app_again))
                exitTime = System.currentTimeMillis()
            } else {
                AppActivityManager.getInstance()?.AppExit(this)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}