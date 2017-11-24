package com.fc.rain.freecreate.moudel.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import com.hyphenate.EMCallBack
import android.util.Log
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.base.BaseActivity
import com.fc.rain.freecreate.base.Constant.PASSWORD
import com.fc.rain.freecreate.base.Constant.USERNAME
import com.fc.rain.freecreate.utils.SPUtils
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import com.yanzhenjie.permission.PermissionListener
import org.jetbrains.anko.custom.async


class MainActivity : BaseActivity() {
    override fun layoutResID(): Int {
        return R.layout.activity_main
    }

    override fun initContract(savedInstanceState: Bundle?) {

    }

    companion object {
        var REQUEST_CONTACTS = 100
        var REQUEST_CODE_SETTING = 200
    }

    private fun initPermission() {
        // 在Activity：
        AndPermission.with(this)
                .requestCode(REQUEST_CONTACTS)
                .permission(Permission.STORAGE)
                .callback(object : PermissionListener {
                    override fun onSucceed(requestCode: Int, grantPermissions: MutableList<String>) {
                        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                        } else {
                            login()
                        }
                    }

                    override fun onFailed(requestCode: Int, deniedPermissions: MutableList<String>) {
                        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
                        if (AndPermission.hasAlwaysDeniedPermission(this@MainActivity, deniedPermissions)) {
                            AndPermission.defaultSettingDialog(this@MainActivity, REQUEST_CODE_SETTING)
                                    .setTitle(R.string.permission_title_dialog)
                                    .setMessage(R.string.permission_message_failed)
                                    .setPositiveButton(R.string.permission_ok)
                                    .setNegativeButton(R.string.permission_no, null)
                                    .show()
                            // 更多自定dialog，请看上面。
                        }
                    }

                })
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale { _, rationale ->
                    // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                    AndPermission.rationaleDialog(this, rationale).show()
                }
                .start()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_SETTING -> {
                // 用户从设置回来
                initPermission()
            }
        }
    }

    override fun initView() {
        userName = SPUtils.get(this, USERNAME, "").toString()
        password = SPUtils.get(this, PASSWORD, "").toString()
        et_name.setText(userName)
        et_password.setText(password)

    }

    override fun initData() {
        initPermission()
    }

    override fun initListener() {
        btn_login.setOnClickListener { login() }
        btn_register.setOnClickListener { register() }
        btn_exit.setOnClickListener { exitLogin() }
    }

    override fun openDefaultHXListener(): Int {
        return CUSTOMHXLISTENER
    }

    var userName: String? = ""
    var password: String? = ""

    private fun login() {
        userName = et_name.text.toString()
        password = et_password.text.toString()
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            toast("username or password can't isEmpty")
            return
        }
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
                        userName?.let { SPUtils.put(this@MainActivity, USERNAME, it) }
                        password?.let { SPUtils.put(this@MainActivity, PASSWORD, it) }
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
        userName = et_name.text.toString()
        password = et_password.text.toString()
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            toast("username or password can't isEmpty")
            return
        }
        showLoading()
        async {
            try {
                //注册失败会抛出HyphenateException
                EMClient.getInstance().createAccount(userName, password)//同步方法
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
                    SPUtils.clear(this@MainActivity)
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
