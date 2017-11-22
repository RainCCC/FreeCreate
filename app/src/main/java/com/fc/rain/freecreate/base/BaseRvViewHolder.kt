package com.qiongliao.qiongliaomerchant.base

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.zhy.autolayout.utils.AutoUtils

import java.util.HashMap

/**
 * RecyclerView 的ViewHolder基类
 *
 *
 * Created by Rain on 2017/9/28.
 */

class BaseRvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mViewMap: MutableMap<Int, View>

    init {
        mViewMap = HashMap()
        AutoUtils.autoSize(itemView)
    }

    /**
     * 获取设置的view
     *
     * @param id
     * @return
     */
    fun <T : View> getView(id: Int): T? {
        var view: View? = mViewMap[id]
        if (view == null) {
            view = itemView.findViewById(id)
            mViewMap.put(id, view)
        }
        return view as T?
    }

    fun getTextView(id: Int): TextView? {
        return getView(id)
    }

    fun getImageView(id: Int): ImageView? {
        return getView(id)
    }

    fun getButton(id: Int): Button? {
        return getView(id)
    }

    fun getEditText(id: Int): EditText? {
        return getView(id)
    }

}
