package com.anymore.statelayout.creator

import android.content.Context
import com.anymore.statelayout.StateLayout
import com.anymore.statelayout.api.EmptyView

/**
 * 全局空白视图创建器,优先级较低 低于xml中指定
 * Created by anymore on 2020/3/28.
 */
interface EmptyViewCreator {
    fun create(context: Context, layout: StateLayout): EmptyView
}