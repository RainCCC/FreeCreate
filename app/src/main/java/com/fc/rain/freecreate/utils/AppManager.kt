package com.fc.rain.freecreate.utils

import android.app.Activity
import java.util.*
import android.app.ActivityManager
import android.content.Context


object AppManager {

    private var activityStack: Stack<Activity>? = null

    init {
        if (activityStack == null) {
            activityStack = Stack()
        }
    }

    /**
     * 获取栈顶Activity（堆栈中最后一个压入的）
     */
    val topActivity: Activity?
        get() = activityStack?.lastElement()

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {
        activityStack?.add(activity)
    }

    /**
     * 结束栈顶Activity（堆栈中最后一个压入的）
     */
    fun finishTopActivity() {
        val activity = activityStack?.lastElement()
        finishActivity(activity)
    }

    /**
     * 结束指定类名的Activity
     *
     * @param cls
     */
    fun finishActivity(cls: Class<*>) {
        activityStack?.let {
            for (i in it.indices) {
                if (it[i].javaClass == cls) {
                    it.remove(it[i])
                    it[i].finish()
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        activityStack?.let {
            for (i in it.indices) {
                if (null != it[i]) {
                    it.remove(it[i])
                    it[i].finish()
                }
            }
            it.clear()
        }
    }

    /**
     * 退出应用程序
     */
    fun appExit(context: Context) {
        try {
            finishAllActivity()
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.restartPackage(context.packageName)
            System.exit(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        activityStack?.let {
            if (null != activity) {
                it.remove(activity)
                activity.finish()
            }
        }
    }

    /**
     * 得到指定类名的Activity
     */
    fun getActivity(cls: Class<*>): Activity? {
        activityStack?.let {
            it.forEach {
                return it
            }
        }
        return null
    }
}