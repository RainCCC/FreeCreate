package com.fc.rain.freecreate.moduel.ui.fragment

import android.content.Intent
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import cn.bmob.v3.BmobUser
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.base.BaseFragment
import com.fc.rain.freecreate.base.Constant
import com.fc.rain.freecreate.moduel.ui.activity.LoginActivity
import com.fc.rain.freecreate.moduel.ui.activity.MyActivity
import com.fc.rain.freecreate.utils.SPUtils
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast

/**
 *
 * Describe # 我的
 *
 * Created by Rain on 2017/11/23.
 */
class MyFragment : BaseFragment() {
//    var etName: EditText? = null
//    var etReason: EditText? = null
    override fun initView() {
//        etName = findViewById<EditText>(R.id.et_name) as EditText?
//        etReason = findViewById<EditText>(R.id.et_reason) as EditText?
        var tvUser = findViewById<TextView>(R.id.tv_name) as TextView
        tvUser?.text = BmobUser.getObjectByKey("username").toString()
    }

    override fun initContract() {
    }

    override fun initData() {
    }

    override fun initListener() {
        findViewById<Button>(R.id.ll_set)?.setOnClickListener {
            startActivity(Intent(context,MyActivity::class.java))
        }
        findViewById<Button>(R.id.ll_exit)?.setOnClickListener {
            showLoading()
            EMClient.getInstance().logout(true, object : EMCallBack {
                override fun onSuccess() {
                    // TODO Auto-generated method stub
                    mContext.runOnUiThread {
                        hideLoading()
                        toast("exit_success")
                        //bmob退出登录
                        BmobUser.logOut()
                        SPUtils.clear(mContext)
                        startActivityForResult(Intent(mContext, LoginActivity::class.java),1)
                        activity.finish()
                    }
                }

                override fun onProgress(progress: Int, status: String) {
                    // TODO Auto-generated method stub

                }

                override fun onError(code: Int, message: String) {
                    // TODO Auto-generated method stub
                    mContext.runOnUiThread {
                        hideLoading()
                        toast("exit_fail")
                    }
                }
            })

        }
    }

    override fun lazyLoadData() {
    }

    override val layoutResID: Int
        get() = R.layout.fragment_my
}
