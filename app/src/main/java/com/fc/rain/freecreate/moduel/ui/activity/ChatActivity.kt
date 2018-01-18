package com.fc.rain.freecreate.moduel.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.base.BaseActivity
import com.fc.rain.freecreate.moduel.ui.fragment.ChatFragment
import com.hyphenate.easeui.EaseConstant
import com.hyphenate.hx.HxHelper
import com.hyphenate.hx.runtimepermissions.PermissionsManager

/**
 *
 * Describe # 聊天页面
 *
 * Created by Rain on 2018/1/18.
 */
class ChatActivity : BaseActivity() {

    companion object {
        fun startActivity(context: Context, userId: String) {
            var intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(EaseConstant.EXTRA_USER_ID, userId)
            context.startActivity(intent)
        }
    }

    var hxUser = ""

    override fun initContract(savedInstanceState: Bundle?) {

        HxHelper.getInstance().pushActivity(this)
        hxUser = intent.getStringExtra(EaseConstant.EXTRA_USER_ID)
        //new出EaseChatFragment或其子类的实例
        val chatFragment = ChatFragment()
        //传入参数
        intent.extras.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE)
        chatFragment.arguments = intent.extras
        supportFragmentManager.beginTransaction().add(R.id.fl, chatFragment).commit()
    }

    override fun onNewIntent(intent: Intent) {
        // make sure only one chat activity is opened
        val username = intent.getStringExtra("userId")
        if (hxUser == username)
            super.onNewIntent(intent)
        else {
            finish()
            startActivity(intent)
        }
    }

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() {
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults)
    }

    override fun layoutResID(): Int {
        return R.layout.activity_chat
    }

    override fun onDestroy() {
        super.onDestroy()
        HxHelper.getInstance().popActivity(this)
    }
}