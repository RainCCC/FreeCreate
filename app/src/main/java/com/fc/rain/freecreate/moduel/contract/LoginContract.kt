package com.fc.rain.freecreate.moduel.contract

import com.fc.rain.freecreate.base.IBasePView
import com.fc.rain.freecreate.base.IBasePresenter

/**
 *
 * Describe # login契约类
 *
 * Created by Rain on 2017/11/27.
 */
class LoginContract {
    interface ILoginView : IBasePView<ILoginPresenter> {
        fun registerSuccess()
    }

    interface ILoginPresenter : IBasePresenter {
        fun login(userName: String?, password: String?)
        fun register(userName: String?, password: String?, againPassword: String?, emailAddress: String?)
        fun registerToBmobSuccess(userName: String?, password: String?)
        fun registerToHxSuccess(userName: String?, password: String?, emailAddress: String?)
        fun registerFail(string: String)
        fun loginToBmobSuccess(userName: String?, password: String?)
        fun loginFail(string: String)
        fun loginToHxSuccess(userName: String?, password: String?)
    }

    interface ILoginModel {
        fun registerToBmob(userName: String?, password: String?, emailAddress: String?)
        fun loginToBmob(userName: String?, password: String?)
        fun registerToHx(userName: String?, password: String?, emailAddress: String?)
        fun loginToHx(userName: String?, password: String?)
    }
}