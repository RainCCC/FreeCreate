package com.fc.rain.freecreate.moduel.ui.fragment

import android.content.Intent
import android.widget.Button
import android.widget.TextView
import cn.bmob.v3.BmobUser
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.base.BaseFragment
import com.fc.rain.freecreate.moduel.ui.activity.MyActivity
import com.qiongliao.qiongliaomerchant.hx.HxNetUtils

/**
 *
 * Describe # 我的
 *
 * Created by Rain on 2017/11/23.
 */
class MyFragment : BaseFragment() {
    override fun initView() {
        var tvUser = findViewById<TextView>(R.id.tv_name) as TextView
        tvUser?.text = BmobUser.getObjectByKey("username").toString()
    }

    override fun initContract() {
    }

    override fun initData() {
    }

    override fun initListener() {
        findViewById<Button>(R.id.ll_set)?.setOnClickListener {
            startActivity(Intent(context, MyActivity::class.java))
        }
        findViewById<Button>(R.id.ll_exit)?.setOnClickListener {
            showLoading()
            mContext?.let { it1 -> HxNetUtils.instance.offLineOperation(it1) }
        }
    }

    override fun lazyLoadData() {
    }

    override val layoutResID: Int
        get() = R.layout.fragment_my
}
