package com.anymore.statelayout.creator

import android.content.Context
import com.anymore.statelayout.StateLayout
import com.anymore.statelayout.api.LoadingView

/**
 * 全局加载视图创建器
 * Created by anymore on 2020/3/28.
 */
interface LoadingViewCreator {
    fun create(context: Context, layout: StateLayout): LoadingView
}