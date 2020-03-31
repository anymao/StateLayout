package com.anymore.statelayout.wrapper

import android.util.Log
import android.view.View
import com.anymore.statelayout.StateLayout
import com.anymore.statelayout.api.ErrorView
import com.anymore.statelayout.api.OnIconClickListener
import java.util.*

/**
 * Created by anymore on 2020/3/28.
 */
class ErrorViewWrapper(mRealView: View) : ViewWrapper(mRealView), ErrorView {

    companion object {
        private const val TAG = "ErrorViewWrapper"
        private const val OP_MSG = "Invalid operation<%s>,because this is a ErrorViewWrapper!"
    }

    override fun setErrorMessage(message: String) {
        Log.e(TAG, String.format(Locale.getDefault(), OP_MSG, "setErrorMessage"))
    }

    override fun setErrorIcon(errorIconId: Int) {
        Log.e(TAG, String.format(Locale.getDefault(), OP_MSG, "setErrorIcon"))
    }

    override fun setOnErrorIconClickListener(listener: OnIconClickListener) {
        Log.e(TAG, String.format(Locale.getDefault(), OP_MSG, "setOnErrorIconClickListener"))
    }

    override fun attach(parent: StateLayout) {
        Log.e(TAG, String.format(Locale.getDefault(), OP_MSG, "attach"))
    }

    override fun view(): View = mRealView
}