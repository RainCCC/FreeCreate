package com.fc.rain.freecreate.moudel.ui.fragment

import android.content.Intent
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.base.BaseFragment
import com.fc.rain.freecreate.base.Constant
import com.fc.rain.freecreate.moudel.ui.activity.MainActivity
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
    var etName: EditText? = null
    var etReason: EditText? = null
    override fun initView() {
        etName = findViewById<EditText>(R.id.et_name) as EditText?
        etReason = findViewById<EditText>(R.id.et_reason) as EditText?
        var tvUser = findViewById<TextView>(R.id.tv_name) as TextView
        tvUser?.text = SPUtils.get(mContext, Constant.USERNAME, "").toString()
    }

    override fun initContract() {
    }

    override fun initData() {
    }

    override fun initListener() {
        findViewById<Button>(R.id.btn_add)?.setOnClickListener {

            if (TextUtils.isEmpty(etName?.text.toString()) || TextUtils.isEmpty(etReason?.text.toString())) {
                toast("username or reason can not isEmpty")
            } else {
                try {
                    //参数为要添加的好友的username和添加理由
                    EMClient.getInstance().contactManager().addContact(etName?.text.toString(), etReason?.text.toString())
                    toast("发送好友请求成功！")
                    etName?.text = null
                    etReason?.text = null
                } catch (e: HyphenateException) {
                    toast(e.description)
                }
            }
        }
        findViewById<Button>(R.id.btn_exit)?.setOnClickListener {
            showLoading()
            EMClient.getInstance().logout(true, object : EMCallBack {
                override fun onSuccess() {
                    // TODO Auto-generated method stub
                    mContext.runOnUiThread {
                        hideLoading()
                        toast("退出登录成功")
                        SPUtils.clear(mContext)
                        startActivity(Intent(mContext, MainActivity::class.java))
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
                        toast("退出登录失败")
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
