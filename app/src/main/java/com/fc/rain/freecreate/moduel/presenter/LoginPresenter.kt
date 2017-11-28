package com.fc.rain.freecreate.moduel.presenter

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.moduel.contract.LoginContract
import com.fc.rain.freecreate.moduel.model.LoginModel
import com.fc.rain.freecreate.moduel.ui.activity.HomeActivity
import com.fc.rain.freecreate.utils.RegularUtils

/**
 *
 * Describe # 登录presenter
 *
 * Created by Rain on 2017/11/27.
 */
class LoginPresenter(var mContext: Activity, var mView: LoginContract.ILoginView?) : LoginContract.ILoginPresenter {

    var mModel: LoginModel? = null

    init {
        mView?.setPresenter(this)
        mModel = LoginModel(mContext, this)
    }

    override fun register(userName: String?, password: String?, againPassword: String?, emailAddress: String?) {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password) || TextUtils.isEmpty(againPassword)) {
            mView?.toastMessage(mContext.getString(R.string.username_or_password_no_empty))
            return
        }
        if (!password.equals(againPassword)) {
            mView?.toastMessage(mContext.getString(R.string.check_password))
            return
        }
        if (!RegularUtils.isEmail(emailAddress)) {
            mView?.toastMessage(mContext.getString(R.string.check_email_success))
            return
        }
        mView?.showLoading()
        mModel?.registerToHx(userName, againPassword, emailAddress)
    }

    override fun registerBmobSuccess(userName: String?, password: String?, emailAddress: String?) {
        mView?.hideLoading()
        mView?.toastMessage(mContext.getString(R.string.register_success))
        mView?.registerSuccess()
    }

    override fun registerBmobFail(message: String) {
        mView?.hideLoading()
        mView?.toastMessage(mContext.getString(R.string.register_fail) + message)
    }

    override fun registerHxSuccess(userName: String?, password: String?, emailAddress: String?) {
        mModel?.registerToBmob(userName, password, emailAddress)
    }

    override fun registerHxFail(message: String) {
        mView?.hideLoading()
        mView?.toastMessage(mContext.getString(R.string.register_fail) + message)
    }

    override fun login(userName: String?, password: String?) {

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            mView?.toastMessage(mContext.getString(R.string.username_or_password_no_empty))
            return
        }
        mView?.showLoading()
        mModel?.loginToHx(userName, password)
    }

    override fun loginBmobSuccess(userName: String?, password: String?) {
        mView?.hideLoading()
        mView?.toastMessage(mContext.getString(R.string.login_success))
        var intent1 = Intent(mContext, HomeActivity::class.java)
        mContext.startActivity(intent1)
        mContext.finish()
    }

    override fun loginBmobFail(message: String) {
        mView?.hideLoading()
        mView?.toastMessage(mContext.getString(R.string.login_fail) + message)
    }

    override fun loginHxSuccess(userName: String?, password: String?) {
        mModel?.loginToBmob(userName, password)
    }

    override fun loginHxFail(message: String) {
        mView?.hideLoading()
        mView?.toastMessage(mContext.getString(R.string.login_fail) + message)
    }

    override fun start() {

    }

    override fun destroyView() {
        mView = null
        mModel = null
    }
}