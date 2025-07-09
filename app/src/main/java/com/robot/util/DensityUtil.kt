package com.robot.util

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.WindowManager
import com.robot.common.Global

object DensityUtil {

    private var isPad = -1

    @JvmStatic
    fun dp2px(value: Float): Int {
        Global.getContext()?.resources?.displayMetrics?.density?.let {
            return (value * it + 0.5f).toInt()
        } ?: return 0
    }

    fun dp2px(value: Int): Int {
        Global.getContext()?.resources?.displayMetrics?.density?.let {
            return (value * it + 0.5f).toInt()
        } ?: return 0
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dp(value: Float): Int {
        Global.getContext()?.resources?.displayMetrics?.density?.let {
            return (value / it + 0.5f).toInt()
        } ?: return 0
    }

    /**
     * 获取状态栏高度
     */
    private var mStatusBarHeight = 0
    fun getStatusBarHeight(): Int {
        if (mStatusBarHeight > 0) return mStatusBarHeight
        Global.getContext()?.resources?.let {
            val resourceId: Int = it.getIdentifier("status_bar_height", "dimen", "android")
            mStatusBarHeight = it.getDimensionPixelSize(resourceId)
        }
        return mStatusBarHeight
    }

    /**
     * 获取手机宽度
     *
     * @param context
     * @return
     */
    private var mScreenWidth: Int = 0
    fun getScreenWidth(): Int {
        if (mScreenWidth > 0) return mScreenWidth
        (Global.getContext()?.getSystemService(Context.WINDOW_SERVICE) as? WindowManager?)?.let {
            val point = Point()
            it.defaultDisplay?.getRealSize(point)
            mScreenWidth = point.x
        }
        return mScreenWidth
    }

    /**
     * 获取手机高度
     *
     * @param context
     * @return
     */
    private var mScreenHeight = 0
    fun getScreenHeight(): Int {
        if (mScreenHeight > 0) return mScreenHeight
        (Global.getContext()?.getSystemService(Context.WINDOW_SERVICE) as? WindowManager?)?.let {
            val point = Point()
            it.defaultDisplay?.getRealSize(point)
            mScreenHeight = point.y
        }
        return mScreenHeight
    }

    /**
     * check whether the device is pad or not
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun isPad(context: Context): Boolean {
        if (isPad > -1) return isPad == 1
        isPad = if (DensityUtil.isGreatScreen(context) && DensityUtil.officialPadCheck(context)) 1 else 0
        return isPad == 1
    }

    private fun isGreatScreen(context: Context): Boolean {
        val screenInches: Double = getScreenInch(context)
        return screenInches >= 6.0
    }

    private fun officialPadCheck(context: Context): Boolean {
        return ((context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE)
    }

    /**
     * 屏幕尺寸 inch
     *
     * @param context
     * @return
     */
    fun getScreenInch(context: Context): Double {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            ?: return 0.0
        val display = wm.defaultDisplay
        val dm = DisplayMetrics()
        display.getMetrics(dm)
        val x = Math.pow((dm.widthPixels / dm.xdpi).toDouble(), 2.0)
        val y = Math.pow((dm.heightPixels / dm.ydpi).toDouble(), 2.0)
        // 屏幕尺寸
        return Math.sqrt(x + y)
    }

    fun forceLandscape(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    fun forcePortrait(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}