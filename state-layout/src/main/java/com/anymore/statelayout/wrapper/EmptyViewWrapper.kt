package com.anymore.statelayout.wrapper

import android.util.Log
import android.view.View
import com.anymore.statelayout.StateLayout
import com.anymore.statelayout.api.EmptyView
import com.anymore.statelayout.api.OnIconClickListener
import java.util.*

/**
 * Created by anymore on 2020/3/28.
 */
class EmptyViewWrapper(mRealView: View) : ViewWrapper(mRealView), EmptyView {

    companion object {
        private const val TAG = "EmptyViewWrapper"
        private const val OP_MSG = "Invalid operation<%s>,because this is a EmptyViewWrapper!"
    }

    override fun setEmptyMessage(message: String) {
        Log.e(TAG, String.format(Locale.getDefault(), OP_MSG, "setEmptyMessage"))
    }

    override fun setEmptyIcon(emptyIconId: Int) {
        Log.e(TAG, String.format(Locale.getDefault(), OP_MSG, "setEmptyIcon"))
    }

    override fun setOnEmptyIconClickListener(listener: OnIconClickListener) {
        Log.e(TAG, String.format(Locale.getDefault(), OP_MSG, "setOnEmptyIconClickListener"))
    }

    override fun attach(parent: StateLayout) {
        Log.e(TAG, String.format(Locale.getDefault(), OP_MSG, "attach"))
    }

    override fun view(): View = mRealView
}