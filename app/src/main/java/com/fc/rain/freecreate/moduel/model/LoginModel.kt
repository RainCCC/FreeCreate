package com.fc.rain.freecreate.moduel.model

import android.app.Activity
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.LogInListener
import cn.bmob.v3.listener.SaveListener
import com.fc.rain.freecreate.moduel.contract.ModelContractCallBack
import com.fc.rain.freecreate.moduel.model.bean.MyUser
import com.fc.rain.freecreate.utils.LogUtil
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import org.jetbrains.anko.custom.async

/**
 *
 * Describe # 登录model
 *
 * Created by Rain on 2017/11/27.
 */
class LoginModel(var mContext: Activity, var mPresenter: ModelContractCallBack.ILoginHxCallBack) {
    fun registerToBmob(userName: String?, password: String?, emailAddress: String?) {
        var myUser = MyUser("女", 18, "广州", "")
        myUser.username = userName
        myUser.setPassword(password)
        myUser.signUp(object : SaveListener<MyUser>() {
            override fun done(p0: MyUser?, p1: BmobException?) {
                if (p1 == null) {
                    mPresenter?.registerBmobSuccess(userName, password, emailAddress)
                } else {
                    p1.message?.let { mPresenter?.registerBmobFail(it) }
                }
            }

        })
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

    fun registerToHx(userName: String?, password: String?, emailAddress: String?) {
        mContext.async {
            try {
                //注册失败会抛出HyphenateException
                EMClient.getInstance().createAccount(userName, password)//同步方法
                mContext.runOnUiThread {
                    mPresenter?.registerHxSuccess(userName, password, emailAddress)
                }
            } catch (e: HyphenateException) {
                mContext.runOnUiThread {
                    e.description.let { mPresenter?.registerHxFail(it) }
                }
            }
        }
    }

    fun loginToHx(userName: String?, password: String?) {
        EMClient.getInstance().login(userName, password, object : EMCallBack {
            //回调
            override fun onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups()
                EMClient.getInstance().chatManager().loadAllConversations()
                mContext?.runOnUiThread {
                    mPresenter?.loginHxSuccess(userName, password)
                }
            }

            override fun onProgress(progress: Int, status: String) {

            }

            override fun onError(code: Int, message: String) {
                LogUtil.e("hx------", message)
                mContext.runOnUiThread {
                    mPresenter?.loginHxFail(message)
                }
            }
        })
    }
}