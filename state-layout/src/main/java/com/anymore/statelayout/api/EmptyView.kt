package com.anymore.statelayout.api

import android.graphics.drawable.Drawable

/**
 * 空白视图接口
 * Created by anymore on 2020/3/26.
 */
interface EmptyView : StateView {
    /**
     * 设置空白视图提示文案
     */
    fun setEmptyMessage(message: String)

    /**
     * 设置空白视图图标资源
     */
    fun setEmptyIconResource(emptyIconResource: Int)

    /**
     * 设置空白视图图标资源
     */
    fun setEmptyIconDrawable(drawable: Drawable?) {}

    /**
     * 设置空白视图图标点击事件
     */
    fun setOnEmptyIconClickListener(listener: OnIconClickListener)
}