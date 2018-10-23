package com.xybean.fragmentarouter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.util.SparseArray
import com.alibaba.android.arouter.core.LogisticsCenter
import com.alibaba.android.arouter.exception.NoRouteFoundException
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.facade.service.DegradeService
import com.alibaba.android.arouter.facade.service.InterceptorService
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.arouter.utils.TextUtils
import java.io.Serializable


/**
 * Author @xybean on 2018/9/11.
 * 因为ARouter不支持fragment.startActivityForResult(),
 * 所以实现了这个类扩展了ARouter的功能，使用方式大致同ARouter
 * 只支持android.support.v4.app.Fragment，
 */
class FragmentResultRouter private constructor(fragment: Fragment) {

    companion object {
        private val router = ARouter.getInstance()
        private var mHandler: Handler = Handler(Looper.getMainLooper())

        fun build(fragment: Fragment, path: String): FragmentResultRouter {
            val fragmentRouter = FragmentResultRouter(fragment)
            fragmentRouter.postcard = router.build(path)
            return fragmentRouter
        }

        fun build(fragment: Fragment, uri: Uri): FragmentResultRouter {
            val fragmentRouter = FragmentResultRouter(fragment)
            fragmentRouter.postcard = router.build(uri)
            return fragmentRouter
        }
    }

    private var fragment: Fragment = fragment
    private lateinit var postcard: Postcard

    fun setTag(tag: Any): FragmentResultRouter {
        postcard.tag = tag
        return this
    }

    fun setTimeout(timeout: Int): FragmentResultRouter {
        postcard.timeout = timeout
        return this
    }

    fun setUri(uri: Uri): FragmentResultRouter {
        postcard.uri = uri
        return this
    }

    fun greenChannel(): FragmentResultRouter {
        postcard.greenChannel()
        return this
    }

    /**
     * 这里的逻辑基本等同于com.alibaba.android.arouter.launcher._Arouter#_navigation()
     */
    fun navigation(requestCode: Int) {
        navigation(requestCode, null)
    }

    fun navigation(requestCode: Int, callback: NavigationCallback?) {
        try {
            LogisticsCenter.completion(postcard)
        } catch (e: NoRouteFoundException) {
            if (null != callback) {
                callback.onLost(postcard)
            } else {
                val degradeService = ARouter.getInstance().navigation(DegradeService::class.java)
                degradeService?.onLost(fragment.context, postcard)
            }
            return
        }

        callback?.onFound(postcard)

        if (!postcard.isGreenChannel) {
            val interceptorService = ARouter.getInstance()
                    .build("/arouter/service/interceptor")
                    .navigation() as InterceptorService
            interceptorService.doInterceptions(postcard, object : InterceptorCallback {

                override fun onContinue(postcard: Postcard) {
                    internalNavigation(requestCode, callback)
                }

                override fun onInterrupt(exception: Throwable) {
                    callback?.onInterrupt(postcard)
                }
            })
        } else {
            internalNavigation(requestCode, callback)
        }
    }

    private fun internalNavigation(requestCode: Int, callback: NavigationCallback?) {
        val intent = Intent(fragment.activity, postcard.destination)
        intent.putExtras(postcard.extras)
        val flags = postcard.flags
        if (-1 != flags) {
            intent.flags = flags
        }
        // Set Actions
        val action = postcard.action
        if (!TextUtils.isEmpty(action)) {
            intent.action = action
        }
        // Navigation in main looper.
        if (Looper.getMainLooper().thread != Thread.currentThread()) {
            mHandler.post {
                fragment.startActivityForResult(intent, requestCode, postcard.optionsBundle)
                if (-1 != postcard.enterAnim && -1 != postcard.exitAnim) {    // Old version.
                    fragment.activity!!.overridePendingTransition(postcard.enterAnim, postcard.exitAnim)
                }
            }
        } else {
            fragment.startActivityForResult(intent, requestCode, postcard.optionsBundle)
            if (-1 != postcard.enterAnim && -1 != postcard.exitAnim) {    // Old version.
                fragment.activity!!.overridePendingTransition(postcard.enterAnim, postcard.exitAnim)
            }
        }

        callback?.onArrival(postcard)
    }

    fun with(bundle: Bundle?): FragmentResultRouter {
        postcard.with(bundle)
        return this
    }

    fun withFlags(@Postcard.FlagInt flag: Int): FragmentResultRouter {
        postcard.withFlags(flag)
        return this
    }

    fun withObject(key: String, value: Any): FragmentResultRouter {
        postcard.withObject(key, value)
        return this
    }

    fun withString(key: String, value: String): FragmentResultRouter {
        postcard.withString(key, value)
        return this
    }

    fun withBoolean(key: String, value: Boolean): FragmentResultRouter {
        postcard.withBoolean(key, value)
        return this
    }

    fun withShort(key: String, value: Short): FragmentResultRouter {
        postcard.withShort(key, value)
        return this
    }

    fun withInt(key: String, value: Int): FragmentResultRouter {
        postcard.withInt(key, value)
        return this
    }

    fun withLong(key: String, value: Long): FragmentResultRouter {
        postcard.withLong(key, value)
        return this
    }

    fun withDouble(key: String, value: Double): FragmentResultRouter {
        postcard.withDouble(key, value)
        return this
    }

    fun withByte(key: String, value: Byte): FragmentResultRouter {
        postcard.withByte(key, value)
        return this
    }

    fun withChar(key: String, value: Char): FragmentResultRouter {
        postcard.withChar(key, value)
        return this
    }

    fun withFloat(key: String, value: Float): FragmentResultRouter {
        postcard.withFloat(key, value)
        return this
    }

    fun withCharSequence(key: String, value: CharSequence?): FragmentResultRouter {
        postcard.withCharSequence(key, value)
        return this
    }

    fun withParcelable(key: String, value: Parcelable?): FragmentResultRouter {
        postcard.withParcelable(key, value)
        return this
    }

    fun withParcelableArray(key: String, value: Array<Parcelable>?): FragmentResultRouter {
        postcard.withParcelableArray(key, value)
        return this
    }

    fun withParcelableArrayList(key: String, value: ArrayList<out Parcelable>?): FragmentResultRouter {
        postcard.withParcelableArrayList(key, value)
        return this
    }

    fun withSparseParcelableArray(key: String, value: SparseArray<out Parcelable>?): FragmentResultRouter {
        postcard.withSparseParcelableArray(key, value)
        return this
    }

    fun withIntegerArrayList(key: String, value: ArrayList<Int>?): FragmentResultRouter {
        postcard.withIntegerArrayList(key, value)
        return this
    }

    fun withStringArrayList(key: String, value: ArrayList<String>?): FragmentResultRouter {
        postcard.withStringArrayList(key, value)
        return this
    }

    fun withCharSequenceArrayList(key: String, value: ArrayList<CharSequence>?): FragmentResultRouter {
        postcard.withCharSequenceArrayList(key, value)
        return this
    }

    fun withSerializable(key: String, value: Serializable?): FragmentResultRouter {
        postcard.withSerializable(key, value)
        return this
    }

    fun withByteArray(key: String, value: ByteArray?): FragmentResultRouter {
        postcard.withByteArray(key, value)
        return this
    }

    fun withShortArray(key: String, value: ShortArray?): FragmentResultRouter {
        postcard.withShortArray(key, value)
        return this
    }

    fun withCharArray(key: String, value: CharArray?): FragmentResultRouter {
        postcard.withCharArray(key, value)
        return this
    }

    fun withFloatArray(key: String, value: FloatArray?): FragmentResultRouter {
        postcard.withFloatArray(key, value)
        return this
    }

    fun withCharSequenceArray(key: String, value: Array<CharSequence>?): FragmentResultRouter {
        postcard.withCharSequenceArray(key, value)
        return this
    }

    fun withBundle(key: String, value: Bundle?): FragmentResultRouter {
        postcard.withBundle(key, value)
        return this
    }

    fun withTransition(enterAnim: Int, exitAnim: Int): FragmentResultRouter {
        postcard.withTransition(enterAnim, exitAnim)
        return this
    }

    @RequiresApi(16)
    fun withOptionsCompat(compat: ActivityOptionsCompat?): FragmentResultRouter {
        postcard.withOptionsCompat(compat)
        return this
    }

    fun withAction(action: String): FragmentResultRouter {
        postcard.withAction(action)
        return this
    }

}