package com.anymore.statelayout.exts

import android.content.Context
import androidx.annotation.DimenRes

/**
 * Created by anymore on 2020/3/28.
 */
internal fun Context.dp2px(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

internal fun Context.px2dp(px: Float): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

internal fun Context.getDimension(@DimenRes id: Int): Float {
    return resources.getDimension(id)
}

internal fun Context.getDimensionPixelSize(@DimenRes id: Int): Int {
    return resources.getDimensionPixelSize(id)
}