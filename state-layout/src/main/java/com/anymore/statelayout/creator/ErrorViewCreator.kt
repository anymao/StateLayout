package com.anymore.statelayout.creator

import android.content.Context
import com.anymore.statelayout.StateLayout
import com.anymore.statelayout.api.ErrorView

/**
 * 全局错误视图创建器
 * Created by anymore on 2020/3/28.
 */
interface ErrorViewCreator {
    fun create(context: Context, layout: StateLayout): ErrorView
}