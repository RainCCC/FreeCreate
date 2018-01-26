package com.fc.rain.freecreate

import android.app.Application
import android.app.ActivityManager
import android.content.Context
import android.support.multidex.MultiDex
import cn.bmob.v3.Bmob
import com.fc.rain.freecreate.moduel.contract.SuperListener
import com.fc.rain.freecreate.utils.NotNullSingleValueVar
import com.hyphenate.hx.HxHelper
import com.qiongliao.qiongliaomerchant.hx.HxNetUtils
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.scwang.smartrefresh.layout.header.BezierRadarHeader
import com.taobao.sophix.SophixManager
import org.jetbrains.anko.toast

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
        var appVersion = "0.0.0"
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
        //初始化环信
        initHx()
        initHxListener()
        //初始化Bmob
        initBmob()
        initRefresh()
        // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
        SophixManager.getInstance().queryAndLoadNewPatch()

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
        initSophix()
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

    fun setHotFixStatusListener(listener: SuperListener.HotFixStatusListener) {
        hotFixListener = listener
    }

    var hotFixListener: SuperListener.HotFixStatusListener? = null

    /**
     * 初始化热更新
     */
    private fun initSophix() {
        try {
            appVersion = this.packageManager
                    .getPackageInfo(this.packageName, 0)
                    .versionName
        } catch (e: Exception) {
        }
        var instance = SophixManager.getInstance()
        instance.setContext(this)
                .setAppVersion(appVersion)
                .setSecretMetaData("24783976-1", "ad7d84fa0ad20fddfbf0bd734a2297cc", "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDLuJKG95b66KHQHpNkDOdx8trAwGu9SCMD2Z0Ttt/lpXWEHPtELfEatXQAJVdrtt6lgEOKBfQ0+BmfS5lmGLR4CaffvPwtkbaMkdRHosVCdEWrISV2WIwMS0s71iKyzU92OiDJYsOV22D+joLf489Bc3zlX+jmgx9+bxsURQ1Zrf0/B/VlIe/GAvbhA11VsQP96MyVsziAyOMs75r6T/X2OjMfQ+0b/ezEqE8Q+F/45ahdovbTCIbtCqgVdpSWWpWqpFN7i8pzFYPmsvAls+UskKQuqQD019o1I6ys+XCGLv+tOxQvl404ew7FXfLHUZiTum+omtFcVmQ2aexzM57HAgMBAAECggEAKvjD77RKJKLtfvj0Hiaaq26OthngoAyXVjiQ6ZeposdEApoaZkdKlFeyIFXAGvh3/nEPzg1zdbjfrrpDoUWUy709q/QxPWt/Fiesn2+2LE5aY76aKFyFLhKzy+tfZlfoawfX4qLynqmnzApDkPZ8wVGbBsc0WWtPOVw2WhA2wLOSMOJFbUIS2B1WCV35+GpgWH5kJU9sc1Ow2IOFoOJfIv4qBRVVKm18ma8REyIulKTpoK2TtMHs1bp95fcECNMW0qv1Z8Wp6l+o+ndRYY1vq24dgwt3wGf8KvGkcHNEreb+V1b+EpnmB4VUyOqWL3ya2Zf0gZzZeH3+bUbvl36gAQKBgQDy5sMv2WOMNcXSeuO2Vh21MLKvW2hWyDqzinhqLIW2JizV+n+Q+sqer/AX2Ec6ujrNFok5oXi8jf20sJmL/oLZfisyNjktlvag50Wq15q6Hy9sJ5z48aLwFS9/Fv+3vSKNiAQqJHS1YhOXBGB37eeGZBLu9LvmeiKsOIU1ybGXCQKBgQDWtO0/VRYXOgNmDXTsgnU4Ot1JmoXP5GeXHMFMkvmoAnj5hyY+gSa6/DUNswCFaiiEp9sjqzw0mZTiM0p6X7X2u9Q9WY/qMUbTG0XwskSzewzntbINt/PK9Z++u3ZeGNUbqF//yrEzqb/VHqGlCHlJepC5HjdOCz+9HctKPyurTwKBgH1WN9eD7AsYYCh4wvF+RDwPkdRMbW2CSlAEnpfiu2g/EJmldZfA4Ta7AD+9mRujpt3WyB5VxLA+8HABRGnpfWlu4ik5s41jvYVPcTJzqGJr3xOG6UfHn9UUiXqhynfr/11alxOL1jEWF5ewaXrY5sQA9YgZ/q8xzJxjWdFkbt/JAoGALDAwY8hvM3iNGoPKfKKc3WXABVX4FEE90KvqXIgjOaDIpRJbnKGYBwPBJ4CfqtWeYS7quvyS94BkU+maenUYTh96HuY/6EMcAHasfk0yNn4sucN7Ubd6RVsn9Wk/gz45HegZWdutfKSDBqm3Ou/zhlvjTTYay8Uk6Rcor68wnaECgYBN2cIpoe2zNxHsps+6ndQtYCPpwgvE88fQ21YI5HKlOn/pGxDPynJ7cNUIku/bqM2sLUP/uXmBv+RWSF9LL3pSI+UZU906bnHiNd1YhAGaitv74qmkmnSYcJaFxuI+yZwM7//MU0eKYPnnkB77pk5j2Wx9cIw0q2ybTS0wN+fS5w==")
                .setEnableDebug(MyApplication.isDebug)
                .setEnableFullLog()
                .setPatchLoadStatusStub { mode, code, info, handlePatchVersion ->
                    var msg = StringBuilder("").append("Mode:").append(mode)
                            .append(" Code:").append(code)
                            .append(" Info:").append(info)
                            .append(" HandlePatchVersion:").append(handlePatchVersion).toString()
//                    if (code == PatchStatus.CODE_LOAD_SUCCESS) {
//                        toast(getString(R.string.hot_update_success))
//                        // 表明补丁加载成功
//                        LogUtil.i("sophix load patch success!")
//                    } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
//                        toast(getString(R.string.hot_update_need_restart))
//                        // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
//                        // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
//                        // 如果需要在后台重启，建议此处用SharePreference保存状态。
//                        LogUtil.i("sophix preload patch success. restart app to make effect.")
//                    } else {
//                        // 其它错误信息, 查看PatchStatus类说明
                        toast(msg)
//                    }
//                    hotFixListener?.requestResult(mode, code, info, handlePatchVersion)
                }.initialize()
    }
}
