package com.fc.rain.freecreate.base

/**
 *
 * Describe # view 的基类接口
 *
 * Created by Rain on 2017/11/22.
 */
interface IBaseView {
    fun showLoading()
    fun showBaseLoading(){}
    fun hideBaseLoading(){}
    fun hideLoading()
    fun toastMessage(message: String)
}