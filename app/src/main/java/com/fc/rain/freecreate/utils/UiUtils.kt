package com.fc.rain.freecreate.utils

import android.graphics.drawable.Drawable
import android.os.Build
import com.fc.rain.freecreate.MyApplication

/**
 * author: Alan on 2017/11/13 18:06
 * address: xk6321@gmail.com
 * description: #
 */
object UiUtils {
    fun getString(resId: Int): String {
        return MyApplication.instance.resources.getString(resId)
    }

    fun getStringArray(resId: Int): Array<out String> {
        return MyApplication.instance.resources.getStringArray(resId)
    }

    fun getIntArray(resId: Int): IntArray {
        return MyApplication.instance.resources.getIntArray(resId)
    }

    fun getColor(colorId: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MyApplication.instance.let {
                it.resources.getColor(colorId, it.theme)
            }
        } else {
            MyApplication.instance.let {
                it.resources.getColor(colorId)
            }
        }
    }

    fun getDrawable(drawableId: Int): Drawable? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MyApplication.instance.let {
                it.resources.getDrawable(drawableId, it.theme)
            }
        } else {
            MyApplication.instance.let {
                it.resources.getDrawable(drawableId)
            }
        }
    }

    fun formatString(stringID: Int, args: Any): String? {
        return getString(stringID).let { String.format(it, args) }
    }
}