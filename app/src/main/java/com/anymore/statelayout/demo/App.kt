package com.anymore.statelayout.demo

import android.app.Application
import android.content.Context
import com.anymore.statelayout.StateLayout
import com.anymore.statelayout.creator.EmptyViewCreator
import com.anymore.statelayout.creator.ErrorViewCreator
import com.anymore.statelayout.creator.LoadingViewCreator
import com.anymore.statelayout.demo.custom.LottieEmptyView
import com.anymore.statelayout.demo.custom.LottieErrorView
import com.anymore.statelayout.demo.custom.LottieLoadingView

/**
 * Created by anymore on 2020/4/3.
 */
class App : Application() {
    companion object {
        init {
            StateLayout.globalEmptyViewCreator = object : EmptyViewCreator {
                override fun create(context: Context, layout: StateLayout) =
                    LottieEmptyView(context)
            }
            StateLayout.globalErrorViewCreator = object : ErrorViewCreator {
                override fun create(context: Context, layout: StateLayout) =
                    LottieErrorView(context)
            }
            StateLayout.globalLoadingViewCreator = object : LoadingViewCreator {
                override fun create(context: Context, layout: StateLayout) =
                    LottieLoadingView(context)

            }
        }
    }
}