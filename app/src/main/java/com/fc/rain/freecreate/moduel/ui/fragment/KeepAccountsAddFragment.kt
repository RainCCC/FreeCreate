package com.fc.rain.freecreate.moduel.ui.fragment

import android.os.Bundle
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.base.BaseFragment

/**
 *
 * Describe # 记账下的添加页面
 *
 * Created by Rain on 2018/2/6.
 */
class KeepAccountsAddFragment private constructor(): BaseFragment() {
    companion object {
        fun newInstance(bundle: Bundle?): KeepAccountsAddFragment {
            var keepAccountsAddFragment = KeepAccountsAddFragment()
            keepAccountsAddFragment.arguments = bundle
            return keepAccountsAddFragment
        }
    }

    override fun initView() {
    }

    override fun initContract() {
    }

    override fun initData() {
    }

    override fun initListener() {
    }

    override fun lazyLoadData() {
    }

    override val layoutResID: Int
        get() = R.layout.fragment_account_add

}