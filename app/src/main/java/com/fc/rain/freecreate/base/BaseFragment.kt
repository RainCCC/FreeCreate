package com.fc.rain.freecreate.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fc.rain.freecreate.utils.LoadDialogUtils
import org.jetbrains.anko.support.v4.toast

/**
 *
 * Describe # fragment基类
 *
 * Created by Rain on 2017/11/23.
 */
abstract class BaseFragment : Fragment(), IBaseView {

    var readyVisible: Boolean = false

    var loadDialogUtils: LoadDialogUtils? = null

    protected lateinit var mContext: Context
    var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = context
        rootView = LayoutInflater.from(mContext).inflate(layoutResID, null)
        initView()
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadDialogUtils = LoadDialogUtils(mContext)
        initContract()
        initData()
        initListener()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            readyVisible = true
            onVisible()
        } else {
            readyVisible = false
            onInVisible()
        }
    }

    /**
     * 未显示的方法
     */
    protected fun onInVisible() {
    }

    /**
     * 显示出页面的方法
     */
    protected fun onVisible() {
        //加载数据
        lazyLoadData()
    }

    protected abstract fun initView()

    protected abstract fun initContract()

    protected abstract fun initData()

    protected abstract fun initListener()

    protected abstract fun lazyLoadData()

    protected abstract val layoutResID: Int

    override fun showLoading() {
        loadDialogUtils?.showLoadingDialog()
    }

    override fun hideLoading() {
        loadDialogUtils?.disMissDialog()
    }

    protected fun <T : View> findViewById(resId: Int): View? {
        return rootView?.findViewById(resId)
    }

    override fun toastMessage(message: String) {
        toast(message)
    }
}