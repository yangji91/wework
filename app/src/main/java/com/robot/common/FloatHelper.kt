package com.robot.common

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.hjq.window.EasyWindow
import com.hjq.window.draggable.SpringDraggable
import com.robot.com.BuildConfig
import com.robot.com.R
import com.robot.com.view.CommonDialog
import com.robot.util.DensityUtil
import com.robot.util.MyLog

@SuppressLint("StaticFieldLeak")
object FloatHelper {

    @JvmField
    var mFloatView: TextView? = null
    var mTipView: TextView? = null

    @JvmStatic
    fun init(context: Activity?) {
        runCatching {
            context?.let {
                if (mTipView == null) {
                    mTipView = TextView(context).apply {
                        setTextColor(Color.WHITE)
                        text =
                            MConfiger.mRobotTips + "\r\n" + "版本号:" + BuildConfig.VERSION_NAME + " " + BuildConfig.VERSION_CODE + "\r\n" + "企微版本号:" + Global.getPackageWecomVersion() + "\r\n"
                    }
                }
                (mFloatView?.parent as ViewGroup?)?.removeView(mFloatView)
                mFloatView = null
                mFloatView = TextView(context).apply {
                    layoutParams =
                        ViewGroup.LayoutParams(DensityUtil.dp2px(50), DensityUtil.dp2px(50))
                    var floatGradientDrawable: GradientDrawable = Global.getShapColor(
                        Color.parseColor("#cc00c29a"), DensityUtil.dp2px(25).toFloat()
                    )
                    setTextColor(Color.WHITE)
                    gravity = Gravity.CENTER
                    when (MConfiger.mRobotStatus) {
                        0 -> {
                            floatGradientDrawable = Global.getShapColor(
                                Color.parseColor("#cc00c29a"), DensityUtil.dp2px(25).toFloat()
                            )
                            text = ""
                        }

                        -1 -> {
                            floatGradientDrawable = Global.getShapColor(
                                Color.parseColor("#cc00c29a"), DensityUtil.dp2px(25).toFloat()
                            )
                            text = ""
                        }

                        1 -> {
                            floatGradientDrawable = Global.getShapColor(
                                Color.parseColor("#cc00c29a"), DensityUtil.dp2px(25).toFloat()
                            )
                            text = ""
                        }

                        2 -> {
                            floatGradientDrawable = Global.getShapColor(
                                Color.parseColor("#cc00c29a"), DensityUtil.dp2px(25).toFloat()
                            )
                            text = ""
                        }
                    }
                    background = floatGradientDrawable
                    setOnClickListener {
                        notifyData()
                        CommonDialog(context).apply {
                            setWidthScale(0.0)
                            setHeightScale(0.0)
                            isCanTouchout(true)
                            mIsCanBack = true
                            setGravity(Gravity.BOTTOM)
                            setStyle(R.style.DialogBottomStyle)
                            layout(LinearLayout(context).apply {
                                (mTipView?.parent as ViewGroup?)?.removeAllViews()
                                setPadding(20, 25, 20, 200)
                                addView(LinearLayout(context).apply {
                                    background =
                                        Global.getShapColor(Color.parseColor("#cc00c29a"), 25f)
                                    setPadding(50, 20, 25, 20)
                                    addView(mTipView)
                                    addView(ImageView(context).apply {
                                        layoutParams = LinearLayout.LayoutParams(
                                            DensityUtil.dp2px(35), DensityUtil.dp2px(35)
                                        )
                                        gravity = Gravity.CENTER_VERTICAL
                                        setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
                                        setOnClickListener {
                                            dismiss()
                                        }
                                    })
                                })
                            })
                        }.show()
                    }
                }
                EasyWindow<EasyWindow<*>>(context).setContentView(mFloatView)
                    // 设置成可拖拽的
                    .setDraggable(SpringDraggable(SpringDraggable.ORIENTATION_HORIZONTAL))
                    .setGravity(Gravity.START or Gravity.BOTTOM).setYOffset(200)
                    // 设置动画样式
                    .setAnimStyle(android.R.style.Animation_Translucent)
                    // 设置外层是否能被触摸
                    //.setOutsideTouchable(false)
                    // 设置窗口背景阴影强度
                    .show()
            }
        }.onFailure {
            it.printStackTrace()
        }
    }

    /**
     * 刷新悬浮窗与提示
     */
    @JvmStatic
    fun notifyData() {
        MyLog.debug(
            "notifyData", "[notifyData] " + MConfiger.mRobotStatus
        )
        runCatching {
            mFloatView?.post {
                Global.wakeUpToast()
                mFloatView?.apply {
                    var floatGradientDrawable: GradientDrawable = Global.getShapColor(
                        Color.parseColor("#cc00c29a"), DensityUtil.dp2px(25).toFloat()
                    )
                    when (MConfiger.mRobotStatus) {
                        0 -> {
                            floatGradientDrawable = Global.getShapColor(
                                Color.parseColor("#cc00c29a"), DensityUtil.dp2px(25).toFloat()
                            )
                            text = ""
                        }

                        -1 -> {
                            floatGradientDrawable = Global.getShapColor(
                                Color.parseColor("#cc00c29a"), DensityUtil.dp2px(25).toFloat()
                            )
                            text = ""
                        }

                        1 -> {
                            floatGradientDrawable = Global.getShapColor(
                                Color.parseColor("#cc00c29a"), DensityUtil.dp2px(25).toFloat()
                            )
                            text = ""
                        }

                        2 -> {
                            floatGradientDrawable = Global.getShapColor(
                                Color.parseColor("#cc00c29a"), DensityUtil.dp2px(25).toFloat()
                            )
                            text = ""
                        }
                    }
                    background = floatGradientDrawable
                }
                mTipView?.apply {
                    text = "租户 ID: " + "${Global.getTenantId()}" + "\r\n" + MConfiger.mRobotTips + "\r\n" +
                                "版本号:" + BuildConfig.VERSION_NAME + " " + BuildConfig.VERSION_CODE + "\r\n" + "企微版本号:" + Global.getPackageWecomVersion() + "\r\n"
                }
            }
        }.onFailure {
            it.printStackTrace()
        }
    }
}