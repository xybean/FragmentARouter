package com.xybean.fragmentarouter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = "/com/activity/ResultActivity")
class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
    }

    override fun finish() {

        val intent = Intent()
        intent.putExtra("hello", "world")
        setResult(Activity.RESULT_OK, intent)

        super.finish()
    }
}
