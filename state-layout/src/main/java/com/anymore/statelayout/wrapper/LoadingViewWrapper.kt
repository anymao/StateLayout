package com.anymore.statelayout.wrapper

import android.util.Log
import android.view.View
import com.anymore.statelayout.StateLayout
import com.anymore.statelayout.api.LoadingView
import java.util.*

/**
 * 加载界面Wrapper，当界面布局没有实现对于接口时候代替使用
 * Created by anymore on 2020/3/28.
 */
class LoadingViewWrapper(mRealView: View) : ViewWrapper(mRealView), LoadingView {

    companion object {
        private const val TAG = "LoadingViewWrapper"
        private const val OP_MSG = "Invalid operation<%s>,because this is a LoadingViewWrapper!"
    }

    override fun setLoadingMessage(message: String) {
        Log.e(TAG, String.format(Locale.getDefault(), OP_MSG, "setLoadingMessage"))
    }

    override fun setLoadingProgress(progress: Int) {
        Log.e(TAG, String.format(Locale.getDefault(), OP_MSG, "setLoadingProgress"))
    }

    override fun attach(parent: StateLayout) {
        Log.e(TAG, String.format(Locale.getDefault(), OP_MSG, "attach"))
    }

    override fun view(): View = mRealView
}