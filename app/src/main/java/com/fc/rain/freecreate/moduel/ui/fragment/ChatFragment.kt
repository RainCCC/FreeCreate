package com.fc.rain.freecreate.moduel.ui.fragment

import android.os.Bundle
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.base.BaseFragment

/**
 *
 * Describe # 聊天
 *
 * Created by Rain on 2017/11/29.
 */
class ChatFragment : BaseFragment() {

    companion object {
        fun newInstance(bundle: Bundle?): ChatFragment {
            var chatFragment = ChatFragment()
            bundle?.let { chatFragment.arguments = it }
            return chatFragment
        }
    }

    override fun initView() {
    }

    override fun initContract() {
    }

    override fun initData() {
    }

    override fun initListener() {
    }

    override fun lazyLoadData() {
    }

    override val layoutResID: Int
        get() = R.layout.fragment_chat

}