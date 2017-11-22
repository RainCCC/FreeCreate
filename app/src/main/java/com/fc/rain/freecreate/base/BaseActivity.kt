package com.fc.rain.freecreate.base

import android.os.Bundle
import com.fc.rain.freecreate.utils.AppActivityManager
import com.zhy.autolayout.AutoLayoutActivity

/**
 *
 * Describe # activity的基类
 *
 * Created by Rain on 2017/11/22.
 */
abstract class BaseActivity : AutoLayoutActivity(), IBaseView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppActivityManager.getInstance()?.addActivity(this)
        setContentView(layoutResID)

        initContract()
        initView()
        initData()
        initListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        AppActivityManager.getInstance()?.removeActivity(this)
    }

    protected abstract fun initContract()

    protected abstract fun initView()

    protected abstract fun initData()

    protected abstract fun initListener()

    protected abstract val layoutResID: Int

    override fun showLoading() {
    }

    override fun hideLoading() {
    }
}