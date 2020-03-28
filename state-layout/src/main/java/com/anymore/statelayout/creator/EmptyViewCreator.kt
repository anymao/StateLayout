package com.anymore.statelayout.creator

import android.content.Context
import com.anymore.statelayout.api.EmptyView

/**
 * Created by anymore on 2020/3/28.
 */
interface EmptyViewCreator {
    fun create(context: Context): EmptyView
}