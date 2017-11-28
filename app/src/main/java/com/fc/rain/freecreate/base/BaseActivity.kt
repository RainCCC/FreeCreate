package com.fc.rain.freecreate.base

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import cn.bmob.v3.BmobUser
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.moduel.ui.activity.LoginActivity
import com.fc.rain.freecreate.utils.AppActivityManager
import com.fc.rain.freecreate.utils.LoadDialogUtils
import com.fc.rain.freecreate.utils.SPUtils
import com.hyphenate.EMConnectionListener
import com.hyphenate.EMError
import com.hyphenate.chat.EMClient
import com.hyphenate.util.NetUtils
import com.zhy.autolayout.AutoLayoutActivity
import org.jetbrains.anko.toast

/**
 *
 * Describe # activity的基类
 *
 * Created by Rain on 2017/11/22.
 */
abstract class BaseActivity : AutoLayoutActivity(), IBaseView {

    val DEFAULTHXLISTENER = 100
    val CUSTOMHXLISTENER = 200
    var mCurEMConnectionListener: EMConnectionListener? = null

    var mContext: Context? = null
    var loadDialogUtils: LoadDialogUtils? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        AppActivityManager.getInstance()?.addActivity(this)
        mContext = this
        setContentView(layoutResID())

        loadDialogUtils = LoadDialogUtils(this)
        initContract(savedInstanceState)
        initView()
        initData()
        initListener()
        //注册一个监听连接状态的listener
        when (openDefaultHXListener()) {
        //默认的环信监听
            DEFAULTHXLISTENER -> {
                mCurEMConnectionListener = DefaultConnectionListener()
                EMClient.getInstance().addConnectionListener(mCurEMConnectionListener)
            }
        //自定义环信监听
            CUSTOMHXLISTENER -> {
                mCurEMConnectionListener = mEMConnectionListener()
                EMClient.getInstance().addConnectionListener(mCurEMConnectionListener)
            }
        }

    }

    protected inner class DefaultConnectionListener : EMConnectionListener {
        override fun onConnected() {
        }

        override fun onDisconnected(errorCode: Int) {
            runOnUiThread {
                when (errorCode) {
                // 显示帐号已经被移除
                    EMError.USER_REMOVED -> {
                        toast(getString(R.string.already_user_deleted))
                        SPUtils.clear(this@BaseActivity)
                        //bmob退出登录
//                        BmobUser.getCurrentUser().delete()
                        BmobUser.logOut()
                        startActivity(Intent(this@BaseActivity, LoginActivity::class.java))
                        AppActivityManager.getInstance()?.killAllActivity()
                    }
                // 显示帐号在其他设备登录
                    EMError.USER_LOGIN_ANOTHER_DEVICE -> {
                        toast(getString(R.string.already_user_login_in_other))
                        SPUtils.clear(this@BaseActivity)
                        //bmob退出登录
                        BmobUser.logOut()
                        startActivity(Intent(this@BaseActivity, LoginActivity::class.java))
                        AppActivityManager.getInstance()?.killAllActivity()
                    }
                }
                if (NetUtils.hasNetwork(mContext)) {
                    //连接不到聊天服务器
//                    runOnUiThread {
//                        toast(getString(R.string.connection_chat_fail))
//                    }
                } else {
                    //当前网络不可用，请检查网络设置
//                    runOnUiThread {
//                        toast(getString(R.string.connection_no_network))
//                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mCurEMConnectionListener?.let { EMClient.getInstance().removeConnectionListener(it) }
        AppActivityManager.getInstance()?.removeActivity(this)
    }

    protected abstract fun initContract(savedInstanceState: Bundle?)

    protected abstract fun initView()

    protected abstract fun initData()

    protected abstract fun initListener()

    protected abstract fun layoutResID(): Int

    protected abstract fun openDefaultHXListener(): Int

    /**
     * 重写自定义环信连接状态监听
     */
    open fun mEMConnectionListener(): EMConnectionListener? {
        return null
    }

    override fun showLoading() {
        loadDialogUtils?.showLoadingDialog()
    }

    override fun hideLoading() {
        loadDialogUtils?.disMissDialog()
    }

    override fun toastMessage(message: String) {
        toast(message)
    }
}
