package com.robot.com.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import com.robot.com.R
import com.robot.util.DensityUtil
import com.robot.util.KeyboardUtils

/**
 * CommonDialog
 * 封装可以展示任意布局的自定义dialog
 */
open class CommonDialog : Dialog {
    lateinit var mRootView: View
    var mPageContext: Context //只能是activity的context，不能是application的，不然会报错

    @StyleRes
    var mResStyle: Int = R.style.DialogCenterStyle
    var mHeightScale: Double = 0.0
    var mWidthScale: Double = 0.0
    var mIsCanTouchOut: Boolean = false//触摸空白是否可以消失 默认不消失
    var mIsCanBack: Boolean = false//点击返回键是否可以消失 默认消失
    var mGravity: Int = Gravity.CENTER
//    var gravityMarginY = 0

    //返回键是否可以消失
    private var mOnKeyListener = DialogInterface.OnKeyListener { _, _, _ -> !mIsCanBack }

    constructor(context: Context) : super(context) {
        mPageContext = context
    }

    constructor(context: Context, resStyle: Int) : super(
        context,
        resStyle
    ) {
        mPageContext = context
        mResStyle = resStyle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mRootView)
        setCanceledOnTouchOutside(mIsCanTouchOut)
        setOnKeyListener(mOnKeyListener)
        window?.apply {
            attributes = attributes.apply {
                gravity = mGravity
//                y = gravityMarginY //用法见WindowManager注释

                //不设置size时，默认为布局中的size,即包裹
                height = when (mHeightScale) {
                    0.0 -> WindowManager.LayoutParams.WRAP_CONTENT
                    1.0 -> WindowManager.LayoutParams.MATCH_PARENT
                    else -> (DensityUtil.getScreenHeight() * mHeightScale).toInt()
                }
                width = when (mWidthScale) {
                    0.0 -> WindowManager.LayoutParams.WRAP_CONTENT
                    1.0 -> WindowManager.LayoutParams.MATCH_PARENT
                    else -> (DensityUtil.getScreenWidth() * mWidthScale).toInt()
                }
            }
            setDimAmount(0f)
            setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    /**
     * 点击空白区域dialog消失但是软键盘不消失bug处理
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && isOutOfBounds(event) && shouldCloseOnTouch()) {
            currentFocus?.let { KeyboardUtils.hideSoftInput(it) }
        }
        return super.onTouchEvent(event)
    }

    private fun shouldCloseOnTouch(): Boolean {
        return (isShowing && window?.peekDecorView() != null
                && currentFocus != null
                && currentFocus?.windowToken != null)
    }

    private fun isOutOfBounds(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        val slop = ViewConfiguration.get(mPageContext)?.scaledWindowTouchSlop
        val decorView = window?.decorView
        if (slop == null || decorView == null) {
            return false
        }
        return (x < -slop || y < -slop || x > decorView.width + slop || y > decorView.height + slop)
    }

    fun setStyle(@StyleRes resStyle: Int) {
        this.mResStyle = resStyle
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
     * 设置dialog弹出的位置
     * @param gravity BOOTOM(下) CENTER(中) TOP(上) LEFT(左) RIGHT(右)
     * @return
     */
    fun setGravity(gravity: Int) {
        this.mGravity = gravity
    }

    /**
     * 设置dialog的位置大小
     * 不设置size时，默认为布局中的size
     *
     * @param heightScale 设置dialog高度的屏占比
     * @param widthScale 设置dialog宽度的屏占比
     * @return
     */
    fun setHeightScale(heightScale: Double) {
        this.mHeightScale = heightScale
    }

    fun setWidthScale(widthScale: Double) {
        this.mWidthScale = widthScale
    }

    /**
     * 触摸空白是否可以消失
     */
    fun isCanTouchout(isCanTouchout: Boolean) {
        this.mIsCanTouchOut = isCanTouchout
    }

    /**
     * 点击返回键dialog是否消失
     */
    fun isCanBack(isCanBack: Boolean) {
        this.mIsCanBack = isCanBack
    }

    override fun show() {
        kotlin.runCatching {
            (mPageContext as? Activity?)?.let { activity ->
                if (!activity.isFinishing && !activity.isDestroyed && !isShowing) {
                    super.show()
                }
            }
        }.onFailure {
            it.printStackTrace()
        }
    }

    override fun dismiss() {
        kotlin.runCatching {
            (mPageContext as? Activity?)?.let { activity ->
                if (!activity.isFinishing && !activity.isDestroyed) {
                    if (shouldCloseOnTouch()) currentFocus?.let { KeyboardUtils.hideSoftInput(it) }
                    super.dismiss()
                }
            }
        }.onFailure {
            it.printStackTrace()
        }
    }

    fun dismissImmediately() {
        kotlin.runCatching {
            super.dismiss()
        }.onFailure {
            it.printStackTrace()
        }
    }

    interface CommonDialogDismissListener {
        fun onDismiss()
    }
}