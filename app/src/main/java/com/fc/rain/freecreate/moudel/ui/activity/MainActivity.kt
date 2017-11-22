package com.fc.rain.freecreate.moudel.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import com.hyphenate.EMCallBack
import android.util.Log
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.utils.LoadDialogUtils
import org.jetbrains.anko.custom.async


class MainActivity : AppCompatActivity() {
    var userName: String = ""
    var password: String = ""
    var loadDialogUtils: LoadDialogUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadDialogUtils = LoadDialogUtils(this)
        btn_login.setOnClickListener { login() }
        btn_register.setOnClickListener { register() }
        btn_exit.setOnClickListener { exitLogin() }
    }

    private fun login() {
        userName = et_name.text.toString()
        password = et_password.text.toString()
        try {
            loadDialogUtils?.showLoadingDialog()
            EMClient.getInstance().login(userName, password, object : EMCallBack {
                //回调
                override fun onSuccess() {
                    EMClient.getInstance().groupManager().loadAllGroups()
                    EMClient.getInstance().chatManager().loadAllConversations()
                    Log.d("hx----------", "登录聊天服务器成功！")
                    runOnUiThread {
                        loadDialogUtils?.disMissDialog()
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
                        loadDialogUtils?.disMissDialog()
                        toast("登录聊天服务器失败：" + message)
                    }
                }
            })
        } catch (e: Exception) {
            runOnUiThread {
                loadDialogUtils?.disMissDialog()
                toast(e.message.toString())
            }
        }
    }

    private fun register() {
        loadDialogUtils?.showLoadingDialog()
        async {
            try {
                //注册失败会抛出HyphenateException
                EMClient.getInstance().createAccount(et_name.text.toString(), et_password.text.toString())//同步方法
                runOnUiThread {
                    loadDialogUtils?.disMissDialog()
                    toast("注册成功")
                }
            } catch (e: HyphenateException) {
                runOnUiThread {
                    loadDialogUtils?.disMissDialog()
                    toast(e.description)
                }
            }
        }
    }

    private fun exitLogin() {
        loadDialogUtils?.showLoadingDialog()
        EMClient.getInstance().logout(true, object : EMCallBack {
            override fun onSuccess() {
                // TODO Auto-generated method stub
                runOnUiThread {
                    loadDialogUtils?.disMissDialog()
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
                    loadDialogUtils?.disMissDialog()
                    toast("退出登录失败")
                }
            }
        })
    }
}
