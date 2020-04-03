package com.anymore.statelayout.demo.custom

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.anymore.statelayout.StateLayout
import com.anymore.statelayout.api.ErrorView
import com.anymore.statelayout.api.OnIconClickListener
import com.anymore.statelayout.demo.R

/**
 * Created by anymore on 2020/4/3.
 */
class LottieErrorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), ErrorView {

    companion object {
        const val TAG = "LottieErrorView"
    }

    private var lavError: LottieAnimationView
    private var tvErrorMsg: TextView
    private var mOnIconClickListener: OnIconClickListener? = null
    private var mParentLayout: StateLayout? = null

    init {
        View.inflate(context, R.layout.view_lottie_error, this)
        orientation = VERTICAL
        gravity = Gravity.CENTER
        lavError = findViewById(R.id.lavError)
        tvErrorMsg = findViewById(R.id.tvErrorMsg)
        lavError.setOnClickListener {
            mOnIconClickListener?.onClick(mParentLayout)
        }

    }

    override fun setErrorMessage(message: String) {
        tvErrorMsg.text = message
    }

    override fun setErrorIconResource(errorIconResource: Int) {

    }

    override fun setOnErrorIconClickListener(listener: OnIconClickListener) {
        mOnIconClickListener = listener
    }

    override fun attach(parent: StateLayout) {
        mParentLayout = parent
    }

    override fun view() = this

}