package com.xybean.fragmentarouter

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter

/**
 * Author @xybean on 2018/8/27.
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // 打印日志
        ARouter.openLog()
        // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.openDebug()
        // 尽可能早，推荐在Application中初始化
        ARouter.init(this)
    }

}