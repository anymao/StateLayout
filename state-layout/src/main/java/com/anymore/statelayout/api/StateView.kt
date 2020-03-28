package com.anymore.statelayout.api

import android.view.View
import com.anymore.statelayout.StateLayout

/**
 * 状态页面共用接口
 * Created by anymore on 2020/3/28.
 */
interface StateView {
    /**
     * 当状态页面初始化完成后调用
     */
    fun attach(parent: StateLayout)

    /**
     * 返回状态页面真正可以加载到布局中的真实布局
     */
    fun view(): View
}