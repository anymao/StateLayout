package com.anymore.statelayout.api

/**
 * 空白视图接口
 * Created by anymore on 2020/3/26.
 */
interface EmptyView : StateView {
    fun setEmptyMessage(message: String)
    fun setEmptyIconResource(emptyIconResource: Int)
    fun setOnEmptyIconClickListener(listener: OnIconClickListener)
}