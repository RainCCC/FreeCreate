package com.fc.rain.freecreate.moduel.ui.fragment

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_message.*

/**
 *
 * Describe # 消息
 *
 * Created by Rain on 2017/11/23.
 */
class MessageFragment : BaseFragment() {

    var tabList: MutableList<String> = arrayListOf()
    var fragmentList: MutableList<Fragment> = arrayListOf()
    override fun initContract() {
    }

    override fun initView() {
    }

    override fun initData() {
        tabList?.add(getString(R.string.message))
        tabList?.add(getString(R.string.friend))
        tabList?.forEach { tab_layout.addTab(tab_layout.newTab().setText(it)) }
        fragmentList?.add(ChatFragment.newInstance(null))
        fragmentList?.add(FriendFragment.newInstance(null))
        vp.adapter = MessageVpAdapter(activity.supportFragmentManager, tabList, fragmentList)
        tab_layout.setupWithViewPager(vp)

    }

    override fun initListener() {

    }

    override fun lazyLoadData() {
    }

    override val layoutResID: Int
        get() = R.layout.fragment_message

    class MessageVpAdapter(fragmentManager: FragmentManager, var tabList: MutableList<String>, var fragmentList: MutableList<Fragment>) : FragmentPagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment {
            return fragmentList.get(position)
        }

        override fun getCount(): Int {
            return tabList.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return tabList[position]
        }

    }
}
