package com.anymore.statelayout.api

import android.graphics.drawable.Drawable

/**
 * 错误视图接口
 * Created by anymore on 2020/3/26.
 */
interface ErrorView : StateView {
    /**
     * 设置错误视图文案
     */
    fun setErrorMessage(message: String)

    /**
     * 设置错误视图图标资源，可以将DrawableId传进来
     */
    fun setErrorIconResource(errorIconResource: Int)

    /**
     * 设置错误视图图标资源
     */
    fun setErrorIconDrawable(drawable: Drawable?) {}

    /**
     * 设置图标点击事件，比如重试等操作
     */
    fun setOnErrorIconClickListener(listener: OnIconClickListener)
}