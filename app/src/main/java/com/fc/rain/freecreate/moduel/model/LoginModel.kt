package com.fc.rain.freecreate.moduel.model

import android.app.Activity
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.LogInListener
import cn.bmob.v3.listener.SaveListener
import com.fc.rain.freecreate.moduel.contract.ModelContractCallBack
import com.fc.rain.freecreate.moduel.contract.SuperListener
import com.fc.rain.freecreate.moduel.model.bean.MyUser
import com.fc.rain.freecreate.utils.BmobNetUtils
import com.fc.rain.freecreate.utils.LogUtil
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import com.qiongliao.qiongliaomerchant.hx.HxNetUtils

/**
 *
 * Describe # 登录model
 *
 * Created by Rain on 2017/11/27.
 */
class LoginModel(var mContext: Activity, var mPresenter: ModelContractCallBack.ILoginHxCallBack) {
    fun registerToBmob(mUser: MyUser, mListener: SuperListener.RegisterBmobListener) {
        BmobNetUtils.registerToBmob(mUser, mListener)
    }

    fun loginToBmob(userName: String?, password: String?) {
        BmobUser.loginByAccount(userName, password, object : LogInListener<MyUser>() {
            override fun done(p0: MyUser?, p1: BmobException?) {
                if (p0 != null) {
                    mPresenter?.loginBmobSuccess(userName, password)
                } else {
                    p1?.message?.let { mPresenter?.loginBmobFail(it) }
                }
            }

        })
    }

    fun registerToHx(userName: String, password: String) {
        HxNetUtils.instance.createAccountToHx(mContext, userName, password, object : SuperListener.RegisterHxListener {
            override fun createSuccess(username: String, password: String) {
            }

            override fun createFail(e: HyphenateException) {
            }
        })
    }

    fun loginToHx(userName: String?, password: String?) {
        HxNetUtils.instance.loginHx(userName, password, object : EMCallBack {
            //回调
            override fun onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups()
                EMClient.getInstance().chatManager().loadAllConversations()
                mContext.runOnUiThread {
                    mPresenter.loginHxSuccess(userName, password)
                }
            }

            override fun onProgress(progress: Int, status: String) {

            }

            override fun onError(code: Int, message: String) {
                LogUtil.e("hx------", message)
                mContext.runOnUiThread {
                    mPresenter.loginHxFail(message)
                }
            }
        })
    }
}