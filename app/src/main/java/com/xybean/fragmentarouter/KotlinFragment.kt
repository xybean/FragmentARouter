package com.xybean.fragmentarouter

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.alibaba.android.arouter.facade.annotation.Route

/**
 * Author @xybean on 2018/8/27.
 */
@Route(path = "/com/fragment/kotlin")
class KotlinFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_kotlin, container, false)

        root.findViewById<Button>(R.id.btn).setOnClickListener {
            FragmentResultRouter
                .build(this@KotlinFragment, "/com/activity/ResultActivity")
                .navigation(Constants.REQUEST_CODE)
        }

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE) {
            val result = data!!.getStringExtra("hello")
            println("xyb=================>>>>>>KotlinFragment 收到结果 ： $result")
        }
    }

}