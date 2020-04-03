package com.anymore.statelayout.api

import androidx.annotation.IntRange

/**
 * 加载中视图接口
 * Created by anymore on 2020/3/26.
 */
interface LoadingView : StateView {
    /**
     * 设置加载中文案提醒
     */
    fun setLoadingMessage(message: String)

    /**
     * 设置加载进度
     */
    fun setLoadingProgress(@IntRange(from = 0, to = 100) progress: Int)
}