package com.fc.rain.freecreate.moduel.contract

import cn.bmob.v3.exception.BmobException
import com.fc.rain.freecreate.moduel.model.bean.MyUser
import com.hyphenate.exceptions.HyphenateException

/**
 *
 * Describe # 监听
 *
 * Created by Rain on 2018/1/18.
 */
class SuperListener {
    interface RegisterHxListener {
        fun createSuccess(username: String, password: String)
        fun createFail(e: HyphenateException)
        fun createDone(){}
    }

    interface RegisterBmobListener {
        fun createSuccess(mUser: MyUser?)
        fun createFail(e: BmobException?)
        fun createDone(){}
    }
}