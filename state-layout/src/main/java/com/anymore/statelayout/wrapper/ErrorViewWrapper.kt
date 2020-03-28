package com.anymore.statelayout.wrapper

import android.view.View
import com.anymore.statelayout.StateLayout
import com.anymore.statelayout.api.ErrorView
import com.anymore.statelayout.api.OnIconClickListener

/**
 * Created by anymore on 2020/3/28.
 */
class ErrorViewWrapper(private val mRealView: View) : ErrorView {
    override fun setErrorMessage(message: String) {

    }

    override fun setErrorIcon(errorIconId: Int) {

    }

    override fun setOnErrorIconClickListener(listener: OnIconClickListener) {

    }

    override fun attach(parent: StateLayout) {

    }

    override fun view(): View = mRealView
}