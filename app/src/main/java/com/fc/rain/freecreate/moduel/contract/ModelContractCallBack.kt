package com.fc.rain.freecreate.moduel.contract

/**
 *
 * Describe # model请求数据的回调，方便复用
 *
 * Created by Rain on 2017/11/28.
 */
class ModelContractCallBack {

    /**
     * 登录注册帐号回调
     */
    interface ILoginHxCallBack {
        fun loginHxSuccess(userName: String?, password: String?) {

        }

        fun loginHxFail(message: String) {

        }

        fun loginBmobSuccess(userName: String?, password: String?) {

        }

        fun loginBmobFail(message: String) {

        }

        fun registerHxSuccess(userName: String?, password: String?, emailAddress: String?) {

        }

        fun registerHxFail(message: String) {

        }

        fun registerBmobSuccess(userName: String?, password: String?, emailAddress: String?) {

        }

        fun registerBmobFail(message: String) {

        }
    }

    interface IFriendFragmentCB{

    }
}