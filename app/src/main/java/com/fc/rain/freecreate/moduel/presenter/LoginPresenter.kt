package com.fc.rain.freecreate.moduel.presenter

import android.app.Activity
import android.text.TextUtils
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.LogInListener
import cn.bmob.v3.listener.UpdateListener
import com.fc.rain.freecreate.MyApplication
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.moduel.contract.LoginContract
import com.fc.rain.freecreate.moduel.contract.SuperListener
import com.fc.rain.freecreate.moduel.model.bean.MyUser
import com.fc.rain.freecreate.moduel.ui.activity.HomeActivity
import com.fc.rain.freecreate.utils.BmobNetUtils
import com.fc.rain.freecreate.utils.LogUtil
import com.fc.rain.freecreate.utils.UiUtils
import com.hyphenate.EMCallBack
import com.hyphenate.exceptions.HyphenateException
import com.qiongliao.qiongliaomerchant.hx.HxNetUtils

/**
 *
 * Describe # 登录presenter
 *
 * Created by Rain on 2017/11/27.
 */
class LoginPresenter(var mContext: Activity, var mView: LoginContract.ILoginView?) : LoginContract.ILoginPresenter {

    init {
        mView?.setPresenter(this)
    }

    override fun register(userName: String, password: String, againPassword: String) {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password) || TextUtils.isEmpty(againPassword)) {
            mView?.toastMessage(mContext.getString(R.string.username_or_password_no_empty))
            return
        }
        if (!password.equals(againPassword)) {
            mView?.toastMessage(mContext.getString(R.string.check_password))
            return
        }
        mView?.showLoading()
        var myUser = MyUser()
        myUser.username = userName
        myUser.setPassword(password)
        BmobNetUtils.registerToBmob(myUser, object : SuperListener.RegisterBmobListener {
            override fun createSuccess(mUser: MyUser?) {
                mView?.registerSuccess()
            }

            override fun createFail(e: BmobException?) {
                mView?.toastMessage(UiUtils.getString(R.string.register_fail) + e?.message)
            }

            override fun createDone() {
                mView?.hideLoading()
            }
        })

    }

    fun registerHx(mUser: MyUser) {
        //设置环信的帐号和密码
        mUser.hxUid = "hx" + mUser.objectId
        mUser.hxPassword = "password" + mUser.objectId
        BmobNetUtils.update(mUser, object : UpdateListener() {
            override fun done(p1: BmobException?) {
                if (p1 == null) {
                    //注册环信帐号
                    HxNetUtils.instance.createAccountToHx(MyApplication.instance, mUser.hxUid, mUser.hxPassword, object : SuperListener.RegisterHxListener {
                        override fun createSuccess(username: String, password: String) {
//                            mView?.toastMessage(UiUtils.getString(R.string.register_success))
                        }

                        override fun createFail(e: HyphenateException) {
//                            mView?.toastMessage(UiUtils.getString(R.string.register_fail) + "hx:" + e.message)
                        }

                        override fun createDone() {
                            loginHx(mUser)
                        }

                    })
                } else {
                    mView?.toastMessage(UiUtils.getString(R.string.update_fail) + p1?.message)
                    loginHx(mUser)
                }
            }
        })
    }

    override fun login(userName: String?, password: String?) {
        if (userName.isNullOrBlank() || password.isNullOrBlank()) {
            mView?.toastMessage(mContext.getString(R.string.username_or_password_no_empty))
        } else {
            mView?.showLoading()
            userName?.let {
                password?.let { it1 ->
                    //登录Bmob
                    BmobNetUtils.login(it, it1, object : LogInListener<MyUser>() {
                        override fun done(user: MyUser?, e: BmobException) {
                            if (user != null) {
                                loginAndRegister(user)
                            } else {
                                BmobUser.logOut()
                                mView?.hideLoading()
                                mView?.toastMessage(UiUtils.getString(R.string.login_fail) + e.message)
                            }
                        }
                    })
                }
            }
        }
    }

    //登录环信，如果没有注册环信去注册再登录
    private fun loginAndRegister(user: MyUser) {
        if (user.hxUid.isNullOrBlank() || user.hxPassword.isNullOrBlank()) {
            registerHx(user)
        } else {
            loginHx(user)
        }
    }

    //登录环信
    private fun loginHx(user: MyUser) {
        if (user.hxUid.isNullOrBlank() || user.hxPassword.isNullOrBlank()) {
            gotoHome()
            return
        } else {
            HxNetUtils.instance.loginHx(user.hxUid, user.hxPassword, object : EMCallBack {
                override fun onSuccess() {
                    gotoHome()
                }

                override fun onProgress(p0: Int, p1: String?) {
                }

                override fun onError(p0: Int, p1: String?) {
                    p1?.let { LogUtil.e(it) }
                    gotoHome()
                }
            })
        }
    }

    //跳转到首页
    private fun gotoHome() {
        mContext.runOnUiThread {
            HomeActivity.startActivity(mContext)
            mView?.hideLoading()
            mView?.hideBaseLoading()
            mContext.finish()
        }
    }

    override fun start() {
        mView?.showBaseLoading()
        //判断是否登录过了
        var currentUser = BmobUser.getCurrentUser(MyUser::class.java)
        if (currentUser != null) {
            loginAndRegister(currentUser)
        } else {
            mView?.hideBaseLoading()
        }
    }

    override fun destroyView() {
        mView = null
//        mModel = null
    }
}