package com.fc.rain.freecreate.base

/**
 *
 * Describe # 带presenter连接的基类接口
 *
 * Created by Rain on 2017/11/22.
 */

interface IBasePView<P> : IBaseView {
    fun setPresenter(presenter: P)
}