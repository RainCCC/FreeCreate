package com.fc.rain.freecreate.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import java.util.*

/**
 * activity管理类
 */
class AppActivityManager private constructor() {

    companion object {
        private var mActivityStack: Stack<Activity>? = null
        private var mAppManager: AppActivityManager? = null
        /**
         * 单一实例
         */

        fun getInstance(): AppActivityManager? {
            if (mAppManager == null) {
                synchronized(AppActivityManager::class) {
                    if (mAppManager == null) {
                        mAppManager = AppActivityManager()
                    }
                }
            }
            return mAppManager
        }
    }

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {
        if (mActivityStack == null) {
            mActivityStack = Stack<Activity>()
        }
        mActivityStack!!.add(activity)
    }

    /**
     * 移除Activity到堆外
     */
    fun removeActivity(activity: Activity) {
        mActivityStack!!.remove(activity)
    }

    /**
     * 获取栈顶Activity
     */
    fun getTopActivity(): Activity {
        return mActivityStack!!.lastElement()
    }

    /**
     * 结束栈顶Activity
     */
    fun killTopActivity() {
        val activity = mActivityStack!!.lastElement()
        killActivity(activity)
    }

    /**
     * 结束指定的Activity
     */
    fun killActivity(activity: Activity?) {
        if (activity != null) {
            mActivityStack!!.remove(activity)
            activity.finish()
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun killActivity(cls: Class<*>) {
        for (activity in mActivityStack!!) {
            if (activity.javaClass == cls) {
                killActivity(activity)
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun killAllActivity() {
        var i = 0
        val size = mActivityStack!!.size
        while (i < size) {
            if (null != mActivityStack!![i]) {
                mActivityStack!![i].finish()
            }
            i++
        }
        mActivityStack!!.clear()
    }

    /**
     * 退出应用程序
     */
    fun AppExit(context: Context) {
        try {
            killAllActivity()
            val activityMgr = context
                    .getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
            activityMgr.restartPackage(context.packageName)
            System.exit(0)
        } catch (e: Exception) {
            Log.e("AppActivityManager", "" + e)
        }

    }
}