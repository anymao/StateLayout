package com.anymore.statelayout.api

import android.graphics.drawable.Drawable

/**
 * 错误视图接口
 * Created by anymore on 2020/3/26.
 */
interface ErrorView : StateView {
    fun setErrorMessage(message: String)
    fun setErrorIconResource(errorIconResource: Int)
    fun setErrorIconDrawable(drawable: Drawable?) {}
    fun setOnErrorIconClickListener(listener: OnIconClickListener)
}