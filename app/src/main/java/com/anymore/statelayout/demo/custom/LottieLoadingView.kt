package com.anymore.statelayout.demo.custom

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.anymore.statelayout.StateLayout
import com.anymore.statelayout.api.LoadingView
import com.anymore.statelayout.demo.R

/**
 * Created by anymore on 2020/4/3.
 */
class LottieLoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), LoadingView {

    companion object {
        const val TAG = "LottieLoadingView"
    }

    private var lavLoading: LottieAnimationView
    private var tvLoadingMsg: TextView
    private lateinit var mParentLayout: StateLayout

    init {
        View.inflate(context, R.layout.view_lottie_loading, this)
        orientation = VERTICAL
        gravity = Gravity.CENTER
        lavLoading = findViewById(R.id.lavLoading)
        tvLoadingMsg = findViewById(R.id.tvLoadingMsg)
    }

    override fun setLoadingMessage(message: String) {
        tvLoadingMsg.text = message
    }

    override fun setLoadingProgress(progress: Int) {

    }

    override fun attach(parent: StateLayout) {
        mParentLayout = parent
    }

    override fun view() = this
}