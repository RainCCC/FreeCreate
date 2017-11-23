package com.fc.rain.freecreate.moudel.ui.activity

import android.content.Intent
import android.text.TextUtils
import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import com.hyphenate.EMCallBack
import android.util.Log
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.base.BaseActivity
import org.jetbrains.anko.custom.async


class MainActivity : BaseActivity() {
    override fun layoutResID(): Int {
        return R.layout.activity_main
    }

    override fun initContract() {
//        userName = SPUtils.getInstance().getString("UserName")
//        password = SPUtils.getInstance().getString("Password")
    }

    override fun initView() {
        et_name.setText(userName)
        et_password.setText(password)

    }

    override fun initData() {
        if (!TextUtils.isEmpty(userName)) {
            login()
        }
    }

    override fun initListener() {
        btn_login.setOnClickListener { login() }
        btn_register.setOnClickListener { register() }
        btn_exit.setOnClickListener { exitLogin() }
    }

    override fun openDefaultHXListener(): Int {
        return CUSTOMHXLISTENER
    }

    var userName: String = ""
    var password: String = ""

    private fun login() {
        userName = et_name.text.toString()
        password = et_password.text.toString()
        try {
            showLoading()
            EMClient.getInstance().login(userName, password, object : EMCallBack {
                //回调
                override fun onSuccess() {
                    EMClient.getInstance().groupManager().loadAllGroups()
                    EMClient.getInstance().chatManager().loadAllConversations()
                    Log.d("hx----------", "登录聊天服务器成功！")
                    runOnUiThread {
                        hideLoading()
//                        SPUtils.getInstance().put("UserName", userName)
//                        SPUtils.getInstance().put("Password", password)
                        toast("登录聊天服务器成功！")
                        var intent1 = Intent(this@MainActivity, HomeActivity::class.java)
                        startActivity(intent1)
                        finish()
                    }
                }

                override fun onProgress(progress: Int, status: String) {

                }

                override fun onError(code: Int, message: String) {
                    Log.e("hx------", message)
                    runOnUiThread {
                        hideLoading()
                        toast("登录聊天服务器失败：" + message)
                    }
                }
            })
        } catch (e: Exception) {
            runOnUiThread {
                hideLoading()
                toast(e.message.toString())
            }
        }
    }

    private fun register() {
        showLoading()
        async {
            try {
                //注册失败会抛出HyphenateException
                EMClient.getInstance().createAccount(et_name.text.toString(), et_password.text.toString())//同步方法
                runOnUiThread {
                    hideLoading()
                    toast("注册成功")
                }
            } catch (e: HyphenateException) {
                runOnUiThread {
                    hideLoading()
                    toast(e.description)
                }
            }
        }
    }

    private fun exitLogin() {
        showLoading()
        EMClient.getInstance().logout(true, object : EMCallBack {
            override fun onSuccess() {
                // TODO Auto-generated method stub
                runOnUiThread {
                    hideLoading()
                    toast("退出登录成功")
                    finish()
                }
            }

            override fun onProgress(progress: Int, status: String) {
                // TODO Auto-generated method stub

            }

            override fun onError(code: Int, message: String) {
                // TODO Auto-generated method stub
                runOnUiThread {
                    hideLoading()
                    toast("退出登录失败")
                }
            }
        })
    }
}
