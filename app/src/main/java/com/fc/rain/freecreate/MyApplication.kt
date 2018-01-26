package com.fc.rain.freecreate

import android.app.Application
import android.app.ActivityManager
import android.content.Context
import cn.bmob.v3.Bmob
import com.fc.rain.freecreate.utils.NotNullSingleValueVar
import com.hyphenate.hx.HxHelper
import com.qiongliao.qiongliaomerchant.hx.HxNetUtils
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.scwang.smartrefresh.layout.header.BezierRadarHeader
import com.taobao.sophix.SophixManager
import com.taobao.sophix.PatchStatus


/**
 * Describe # 全局Application
 *
 *
 * Created by Rain on 2017/11/21.
 */

class MyApplication : Application() {

    companion object {

        /**将Application 单例化，可供全局调用 Context */
        var instance: MyApplication by NotNullSingleValueVar.DelegatesExt.notNullSingleValue()

        //是否开启debug模式
        val isDebug = false
        //转场动画的模式
        val OVERRIDE_PENDING_TRANSITION_TRANSLATE_TOPBOTTOM = 1
        val OVERRIDE_PENDING_TRANSITION_TRANSLATE_LEFTRIGHT = 2
        val OVERRIDE_PENDING_TRANSITION_ALPAH = 3
        val OVERRIDE_PENDING_TRANSITION_SCALE = 4

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
        SophixManager.getInstance().queryAndLoadNewPatch()
        //初始化环信
        initHx()
        initHxListener()
        //初始化Bmob
        initBmob()
        initRefresh()
    }

    private fun initHxListener() {
        HxNetUtils.instance.addConnectionListener(this)
        HxNetUtils.instance.addEMMessageListener(this)
        HxNetUtils.instance.addHxFriendListener(this)

    }

    private fun initRefresh() {
//设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater { context, _ ->
            //            val header = ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate)
//            header.setPrimaryColorId(R.color.colorPrimary)
//            header.setAccentColorId(android.R.color.white)
//            header.setEnableLastTime(false)
            val header = BezierRadarHeader(context)
            header
            //指定为经典Header，默认是贝塞尔雷达Header
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater { context, layout ->
            layout.setEnableLoadmoreWhenContentNotFull(true)//内容不满一页时候启用加载更多
            val footer = BallPulseFooter(context)
            footer.setBackgroundResource(android.R.color.white)
            footer.spinnerStyle = SpinnerStyle.Scale//设置为拉伸模式
            footer//指定为经典Footer，默认是 BallPulseFooter
        }
    }

    private fun initBmob() {
        //第一：默认初始化
        Bmob.initialize(this, "287e64e1a48b3703c2fd4774cfb8e485");
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);
    }

    /**
     * 初始化环信
     */
    private fun initHx() {
        HxHelper.getInstance().init(this, isDebug)
    }

    private fun getAppName(pid: Int): String? {
        var processName: String? = null
        val am = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val l = am.runningAppProcesses
        val i = l.iterator()
        val pm = this.packageManager
        while (i.hasNext()) {
            val info = i.next() as ActivityManager.RunningAppProcessInfo
            try {
                if (info.pid == pid) {
                    processName = info.processName
                    return processName
                }
            } catch (e: Exception) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }

        }
        return processName
    }

    /**
     * 初始化热更新
     */
    private fun initSophix() {
        SophixManager.getInstance().setContext(this)
                .setAppVersion("")
                .setAesKey(null)
                .setEnableDebug(true)
                .setPatchLoadStatusStub { mode, code, info, handlePatchVersion ->
                    // 补丁加载回调通知
                    if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                        // 表明补丁加载成功
                    } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                        // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                        // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
                    } else {
                        // 其它错误信息, 查看PatchStatus类说明
                    }
                }.initialize()
    }
}
