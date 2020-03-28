package com.anymore.statelayout.api

import androidx.annotation.DrawableRes

/**
 * 错误视图接口
 * Created by anymore on 2020/3/26.
 */
interface ErrorView : StateView {
    fun setErrorMessage(message: String)
    fun setErrorIcon(@DrawableRes errorIconId: Int)
    fun setOnErrorIconClickListener(listener: OnIconClickListener)
}