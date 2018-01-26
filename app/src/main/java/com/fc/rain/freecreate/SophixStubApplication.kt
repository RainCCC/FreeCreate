package com.fc.rain.freecreate

import android.content.Context
import com.taobao.sophix.SophixApplication
import com.taobao.sophix.SophixEntry
import android.support.annotation.Keep
import android.support.multidex.MultiDex
import android.util.Log
import com.taobao.sophix.PatchStatus
import com.taobao.sophix.SophixManager

/**
 *
 * Describe #Sophix热修复
 *
 * Created by Rain on 2018/1/26.
 */
class SophixStubApplication : SophixApplication() {

    private val TAG = "SophixStubApplication"

    @Keep
    @SophixEntry(MyApplication::class)
    internal class RealApplicationStub

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
        initSophix()
    }

    private fun initSophix() {
        var appVersion = "0.0.0"
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
                    if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                        // 表明补丁加载成功
                        Log.i(TAG, "sophix load patch success!")
                    } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                        // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                        // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
                        // 如果需要在后台重启，建议此处用SharePreference保存状态。
                        Log.i(TAG, "sophix preload patch success. restart app to make effect.")
                    } else {
                        // 其它错误信息, 查看PatchStatus类说明
                    }
                }.initialize()
    }
}