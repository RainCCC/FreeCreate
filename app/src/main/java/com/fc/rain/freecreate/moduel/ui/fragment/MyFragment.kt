package com.fc.rain.freecreate.moduel.ui.fragment

import android.content.Intent
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import cn.bmob.v3.BmobUser
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.base.BaseFragment
import com.fc.rain.freecreate.moduel.ui.activity.KeepAccountsActivity
import com.fc.rain.freecreate.moduel.ui.activity.MyActivity
import com.fc.rain.freecreate.utils.UiUtils
import com.qiongliao.qiongliaomerchant.hx.HxNetUtils
import com.taobao.sophix.SophixManager

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
        findViewById<LinearLayout>(R.id.ll_version)?.setOnClickListener {

            mContext?.let {
                try {
                    toastMessage("Version:" + it.packageManager.getPackageInfo(it.packageName, 0).versionName)
                } catch (e: Exception) {
                    toastMessage(UiUtils.getString(R.string.request_version_fail))
                }
            }

        }
        findViewById<LinearLayout>(R.id.ll_update)?.setOnClickListener {
            SophixManager.getInstance().queryAndLoadNewPatch()
        }
        findViewById<LinearLayout>(R.id.ll_keep_accounts)?.setOnClickListener {
            startActivity(Intent(mContext, KeepAccountsActivity::class.java))
        }
    }

    override fun lazyLoadData() {
    }

    override val layoutResID: Int
        get() = R.layout.fragment_my
}
