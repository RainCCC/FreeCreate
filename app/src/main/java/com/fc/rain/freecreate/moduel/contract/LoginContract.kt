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

    interface ILoginPresenter : IBasePresenter, ModelContractCallBack.ILoginHxCallBack {
        fun login(userName: String?, password: String?)
        fun register(userName: String?, password: String?, againPassword: String?, emailAddress: String?)
    }
}