package com.anymore.statelayout.wrapper

import android.view.View
import com.anymore.statelayout.StateLayout
import com.anymore.statelayout.api.EmptyView
import com.anymore.statelayout.api.OnIconClickListener

/**
 * Created by anymore on 2020/3/28.
 */
class EmptyViewWrapper(private val mRealView: View) : EmptyView {
    override fun setEmptyMessage(message: String) {

    }

    override fun setEmptyIcon(emptyIconId: Int) {

    }

    override fun setOnEmptyIconClickListener(listener: OnIconClickListener) {

    }

    override fun attach(parent: StateLayout) {

    }

    override fun view(): View = mRealView
}