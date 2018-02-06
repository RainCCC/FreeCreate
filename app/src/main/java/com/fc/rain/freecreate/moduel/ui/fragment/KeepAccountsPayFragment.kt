package com.fc.rain.freecreate.moduel.ui.fragment

import android.content.Context
import android.os.Bundle
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.base.BaseFragment

/**
 *
 * Describe # 记账下的pay页面
 *
 * Created by Rain on 2018/2/6.
 */
class KeepAccountsPayFragment private constructor() : BaseFragment() {

    companion object {
        fun newInstance(bundle: Bundle?): KeepAccountsPayFragment {
            var keepAccountsPayFragment = KeepAccountsPayFragment()
            keepAccountsPayFragment.arguments = bundle
            return keepAccountsPayFragment
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
        get() = R.layout.fragment_account_pay

}