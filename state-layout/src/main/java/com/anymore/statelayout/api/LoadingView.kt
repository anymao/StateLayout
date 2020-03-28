package com.anymore.statelayout.api

import androidx.annotation.IntRange

/**
 * 加载中视图接口
 * Created by anymore on 2020/3/26.
 */
interface LoadingView : StateView {
    fun setLoadingMessage(message: String)
    fun setLoadingProgress(@IntRange(from = 0, to = 100) progress: Int)
}