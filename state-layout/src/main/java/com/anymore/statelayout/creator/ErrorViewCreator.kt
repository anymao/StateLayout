package com.anymore.statelayout.creator

import android.content.Context
import com.anymore.statelayout.StateLayout
import com.anymore.statelayout.api.ErrorView

/**
 * Created by anymore on 2020/3/28.
 */
interface ErrorViewCreator {
    fun create(context: Context, layout: StateLayout): ErrorView
}