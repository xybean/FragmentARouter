package com.xybean.fragmentarouter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = ARouter.getInstance().build("/com/fragment/kotlin").navigation() as Fragment

        supportFragmentManager.beginTransaction().replace(R.id.root, fragment).commit()

    }
}
