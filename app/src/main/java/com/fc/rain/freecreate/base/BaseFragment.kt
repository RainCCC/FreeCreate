package com.fc.rain.freecreate.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fc.rain.freecreate.MyApplication
import com.fc.rain.freecreate.R
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

    protected var isFirstVisible: Boolean = true

    var loadDialogUtils: LoadDialogUtils? = null

    protected var mContext: Context? = null
    var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = activity
        if (rootView == null) {
            rootView = LayoutInflater.from(mContext).inflate(layoutResID, null)
        }
        initView()
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadDialogUtils = mContext?.let { LoadDialogUtils(it) }
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

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        when (jumpAnimationMode()) {
            MyApplication.OVERRIDE_PENDING_TRANSITION_TRANSLATE_LEFTRIGHT -> {
                activity?.overridePendingTransition(R.anim.translate_lr_enter, R.anim.translate_lr_out)
            }
            MyApplication.OVERRIDE_PENDING_TRANSITION_TRANSLATE_TOPBOTTOM -> {
                activity?.overridePendingTransition(R.anim.translate_tb_enter, R.anim.translate_tb_out)
            }
            MyApplication.OVERRIDE_PENDING_TRANSITION_ALPAH -> {
                activity?.overridePendingTransition(R.anim.alpha_enter, R.anim.alpha_out)
            }
            MyApplication.OVERRIDE_PENDING_TRANSITION_SCALE -> {
                activity?.overridePendingTransition(R.anim.scale_enter, R.anim.scale_out)
            }
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
        if (isFirstVisible) {
            isFirstVisible = false
            //加载数据
            lazyLoadData()
        }
    }

    protected abstract fun initView()

    protected abstract fun initContract()

    protected abstract fun initData()

    protected abstract fun initListener()

    protected abstract fun lazyLoadData()

    protected abstract val layoutResID: Int
    //转场动画
    open fun jumpAnimationMode(): Int = MyApplication.OVERRIDE_PENDING_TRANSITION_TRANSLATE_LEFTRIGHT

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