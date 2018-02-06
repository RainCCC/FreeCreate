package com.fc.rain.freecreate.moduel.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.base.BaseActivity
import com.fc.rain.freecreate.moduel.contract.KeepAccountsContract
import com.fc.rain.freecreate.moduel.presenter.KeepAccountsPresenter
import com.fc.rain.freecreate.moduel.ui.fragment.KeepAccountsAddFragment
import com.fc.rain.freecreate.moduel.ui.fragment.KeepAccountsPayFragment
import kotlinx.android.synthetic.main.activity_keep_accounts.*

/**
 *
 * Describe # 记账
 *
 * Created by Rain on 2018/2/6.
 */
class KeepAccountsActivity : BaseActivity(), KeepAccountsContract.View {

    var mPresenter: KeepAccountsContract.Presenter? = null
    var fragmentList: MutableList<Fragment> = arrayListOf()

    override fun setPresenter(presenter: KeepAccountsContract.Presenter) {
        mPresenter = presenter
    }

    override fun initContract(savedInstanceState: Bundle?) {
        KeepAccountsPresenter(this)
    }

    override fun initView() {
        fragmentList.add(KeepAccountsPayFragment.newInstance(intent.extras))
        fragmentList.add(KeepAccountsAddFragment.newInstance(intent.extras))
        vp.adapter = KeepAccountsVpAdapter(fragmentList, supportFragmentManager)
    }

    class KeepAccountsVpAdapter(var list: MutableList<Fragment>, fs: FragmentManager) : FragmentPagerAdapter(fs) {
        override fun getItem(position: Int): Fragment {
            return list[position]
        }

        override fun getCount(): Int {
            return list.size
        }
    }

    override fun initData() {
        mPresenter?.start()
    }

    override fun initListener() {
    }

    override fun layoutResID(): Int {
        return R.layout.activity_keep_accounts
    }

}