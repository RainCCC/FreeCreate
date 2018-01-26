package com.fc.rain.freecreate.base

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.fc.rain.freecreate.MyApplication
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.utils.AppManager
import com.fc.rain.freecreate.utils.LoadDialogUtils
import kotlinx.android.synthetic.main.base_layout.*
import kotlinx.android.synthetic.main.base_loading_view.*
import kotlinx.android.synthetic.main.title_bar.*
import org.jetbrains.anko.toast

/**
 *
 * Describe # activity的基类
 *
 * Created by Rain on 2017/11/22.
 */
abstract class BaseActivity : AppCompatActivity(), IBaseView {

    var mContext: Activity? = null
    var loadDialogUtils: LoadDialogUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        AppManager.addActivity(this)
        mContext = this
        setContentView(R.layout.base_layout)
        setBaseView(layoutResID())

        loadDialogUtils = LoadDialogUtils(this)
        initContract(savedInstanceState)
        initView()
        initData()
        initListener()
    }

    private fun setBaseView(layoutResID: Int) {
        root_view?.let {
            var inflate = LayoutInflater.from(this).inflate(layoutResID, it, false)
            it.addView(inflate, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // 设置标题栏颜色 根据状态栏颜色变换
                if (resources.getColor(R.color.app_color) == window.statusBarColor) {
                    rl_head_title.setBackgroundColor(resources.getColor(R.color.app_color))
                } else {
                    rl_head_title.setBackgroundColor(window.statusBarColor)
                }
            }
            tv_head_title.text = getLable()
        }
        rl_head_back.setOnClickListener { finish() }
//        Glide.with(this).load(R.drawable.cool_loading).into(iv_loading)
        hideBaseLoading()
    }

    //获取Lable名
    private fun getLable(): String {
        val com = ComponentName(mContext?.getString(R.string.package_name), mContext?.javaClass?.name)
        val pm = mContext?.packageManager
        val activityInfo = pm?.getActivityInfo(com, 0)
        return activityInfo?.loadLabel(pm).toString()
    }

    /**
     * 隐藏title_bar
     */
    fun hideTitle(boolean: Boolean) {
        if (boolean) {
            rl_head_title.visibility = View.GONE
        } else {
            rl_head_title.visibility = View.VISIBLE
        }
    }

    /**
     * 隐藏返回键
     */
    fun hideBack(boolean: Boolean) {
        if (boolean) {
            rl_head_back.visibility = View.GONE
        } else {
            rl_head_back.visibility = View.VISIBLE
        }
    }

    override fun hideBaseLoading() {
        rl_loading?.visibility = View.GONE
    }

    override fun showBaseLoading() {
        rl_loading?.visibility = View.VISIBLE
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        when (jumpAnimationMode()) {
            MyApplication.OVERRIDE_PENDING_TRANSITION_TRANSLATE_LEFTRIGHT -> {
                overridePendingTransition(R.anim.translate_lr_enter, R.anim.translate_lr_out)
            }
            MyApplication.OVERRIDE_PENDING_TRANSITION_TRANSLATE_TOPBOTTOM -> {
                overridePendingTransition(R.anim.translate_tb_enter, R.anim.translate_tb_out)
            }
            MyApplication.OVERRIDE_PENDING_TRANSITION_ALPAH -> {
                overridePendingTransition(R.anim.alpha_enter, R.anim.alpha_out)
            }
            MyApplication.OVERRIDE_PENDING_TRANSITION_SCALE -> {
                overridePendingTransition(R.anim.scale_enter, R.anim.scale_out)
            }
        }
    }

    override fun finish() {
        super.finish()
        when (jumpAnimationMode()) {
            MyApplication.OVERRIDE_PENDING_TRANSITION_TRANSLATE_LEFTRIGHT -> {
                overridePendingTransition(R.anim.translate_lr_enter_finish, R.anim.translate_lr_out_finish)
            }
            MyApplication.OVERRIDE_PENDING_TRANSITION_TRANSLATE_TOPBOTTOM -> {
                overridePendingTransition(R.anim.translate_tb_enter_finish, R.anim.translate_tb_out_finish)
            }
            MyApplication.OVERRIDE_PENDING_TRANSITION_ALPAH -> {
                overridePendingTransition(R.anim.alpha_enter, R.anim.alpha_out)
            }
            MyApplication.OVERRIDE_PENDING_TRANSITION_SCALE -> {
                overridePendingTransition(R.anim.scale_enter, R.anim.scale_out)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AppManager.finishActivity(this)
    }

    protected abstract fun initContract(savedInstanceState: Bundle?)

    protected abstract fun initView()

    protected abstract fun initData()

    protected abstract fun initListener()

    protected abstract fun layoutResID(): Int

    //转场动画
    open fun jumpAnimationMode(): Int = MyApplication.OVERRIDE_PENDING_TRANSITION_TRANSLATE_LEFTRIGHT

    override fun showLoading() {
        loadDialogUtils?.showLoadingDialog()
//        showBaseLoading()
    }

    override fun hideLoading() {
        loadDialogUtils?.disMissDialog()
//        hideBaseLoading()
    }

    override fun toastMessage(message: String) {
        toast(message)
    }
}
