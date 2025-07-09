package com.robot.util

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.robot.common.Global

object KeyboardUtils {

    fun showSoftInput() {
        val imm =
            Global.getContext()?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?
        imm?.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    fun showSoftInput(view: View, flags: Int = 0) {
        val imm =
            Global.getContext()?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        view.requestFocus()
        imm?.showSoftInput(view, flags, object : ResultReceiver(Handler()) {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
                if (resultCode == InputMethodManager.RESULT_UNCHANGED_HIDDEN
                    || resultCode == InputMethodManager.RESULT_HIDDEN
                ) {
                    toggleSoftInput()
                }
            }
        })
        imm?.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    fun hideSoftInput(activity: Activity) {
        hideSoftInput(activity.window)
    }

    fun hideSoftInput(window: Window) {
        var view = window.currentFocus
        if (view == null) {
            val decorView = window.decorView
            val focusView = decorView.findViewWithTag<View>("keyboardTagView")
            if (focusView == null) {
                view = EditText(window.context)
                view.setTag("keyboardTagView")
                (decorView as ViewGroup).addView(view, 0, 0)
            } else {
                view = focusView
            }
            view.requestFocus()
        }
        hideSoftInput(view)
    }

    fun hideSoftInput(view: View) {
        val imm =
            Global.getContext()?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun toggleSoftInput() {
        val imm =
            Global.getContext()?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?
        imm?.toggleSoftInput(0, 0)
    }
}