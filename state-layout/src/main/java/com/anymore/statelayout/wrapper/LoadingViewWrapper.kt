package com.anymore.statelayout.wrapper

import android.view.View
import com.anymore.statelayout.StateLayout
import com.anymore.statelayout.api.LoadingView

/**
 * Created by anymore on 2020/3/28.
 */
class LoadingViewWrapper(private val mRealView: View) : LoadingView {
    override fun setLoadingMessage(message: String) {

    }

    override fun setLoadingProgress(progress: Int) {

    }

    override fun attach(parent: StateLayout) {

    }

    override fun view(): View = mRealView
}