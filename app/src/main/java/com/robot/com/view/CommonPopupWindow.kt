package com.robot.com.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import com.robot.util.DensityUtil

/**
 * CommonPopupWindow
 * 封装可以展示任意布局的自定义PopupWindow
 */
open class CommonPopupWindow(context: Context) :
    PopupWindow(context) {

    lateinit var mRootView: View
    var mPageContext: Context = context //只能是activity的context，不能是application的，不然会报错

    @StyleRes
    var mResStyle: Int = 0
    var mHeightScale: Double = 0.0
    var mWidthScale: Double = 0.0
    var mIsCanTouchOut: Boolean = false//触摸空白是否可以消失 默认不消失
//    var mIsCanBack: Boolean = false//点击返回键是否可以消失 默认消失

    var mOnDismissListener: CommonPopupWindowDismissListener? = null

    // 需要主动调用
    fun build() {
        setOnDismissListener { mOnDismissListener?.onDismiss() }
        contentView = mRootView
        isOutsideTouchable = mIsCanTouchOut
        isFocusable = mIsCanTouchOut
        //不设置size时，默认为布局中的size,即包裹
        height = when (mHeightScale) {
            0.0 -> WindowManager.LayoutParams.WRAP_CONTENT//此时设置外部布局的宽高是无用的，需要再包裹一层，内部显示宽高
            1.0 -> WindowManager.LayoutParams.MATCH_PARENT
            else -> (DensityUtil.getScreenHeight() * mHeightScale).toInt()
        }
        width = when (mWidthScale) {
            0.0 -> WindowManager.LayoutParams.WRAP_CONTENT//此时设置外部布局的宽高是无用的，需要再包裹一层，内部显示宽高
            1.0 -> WindowManager.LayoutParams.MATCH_PARENT
            else -> (DensityUtil.getScreenWidth() * mWidthScale).toInt()
        }
        if (mResStyle != 0) animationStyle = mResStyle
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun setStyle(@StyleRes resStyle: Int) {
        mResStyle = resStyle
    }

    fun layout(@LayoutRes resView: Int) {
        mRootView = LayoutInflater.from(mPageContext).inflate(
            resView,
            null,
            false
        )
    }

    fun layout(v: View) {
        mRootView = v
    }

    /**
     * 设置位置大小
     * 设置ppw的位置大小1.设置ppw高度的屏占比2.设置ppw宽度的屏占比
     * 不设置size时，默认为布局中的size
     * @return
     */
    fun setHeightScale(heightScale: Double) {
        this.mHeightScale = heightScale
    }

    fun setWidthScale(widthScale: Double) {
        this.mWidthScale = widthScale
    }

    /**
     * 触摸空白是否可以消失1.true 消失 2.false 不消失3.不传默认为false
     */
    fun isCanTouchout(isCanTouchout: Boolean) {
        this.mIsCanTouchOut = isCanTouchout
    }

    /**
     * 点击返回键ppw是否消失1.false 消失 2.true 不消失3.不传默认为false
     */
//    fun isCanBack(isCanBack: Boolean) {
//        this.mIsCanBack = isCanBack
//    }

    fun setOnDismissListener(onDismissListener: CommonPopupWindowDismissListener) {
        this.mOnDismissListener = onDismissListener
    }

    interface CommonPopupWindowDismissListener {
        fun onDismiss()
    }
}