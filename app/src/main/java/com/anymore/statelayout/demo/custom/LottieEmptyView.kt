package com.anymore.statelayout.demo.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.anymore.statelayout.StateLayout
import com.anymore.statelayout.api.EmptyView
import com.anymore.statelayout.api.OnIconClickListener
import com.anymore.statelayout.demo.R
import com.anymore.statelayout.demo.exts.getDrawableCompatibly


/**
 * Created by anymore on 2020/4/2.
 */
class LottieEmptyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), EmptyView {
    companion object {
        const val TAG = "CustomEmptyView"
    }

    private var lavEmptyIcon: LottieAnimationView
    private var tvEmptyMsg: TextView
    private var mOnIconClickListener: OnIconClickListener? = null
    private var mParentLayout: StateLayout? = null

    init {
        View.inflate(context, R.layout.view_lottie_empty, this)
        orientation = VERTICAL
        gravity = Gravity.CENTER
        lavEmptyIcon = findViewById(R.id.lavEmpty)
        tvEmptyMsg = findViewById(R.id.tvEmptyMsg)
        lavEmptyIcon.setOnClickListener {
            mOnIconClickListener?.onClick(mParentLayout)
        }
    }

    override fun attach(parent: StateLayout) {
        mParentLayout = parent
    }

    override fun view(): View = this

    override fun setEmptyMessage(message: String) {
        tvEmptyMsg.text = message
    }

    override fun setEmptyIconResource(emptyIconResource: Int) {
        setEmptyIconDrawable(context.getDrawableCompatibly(emptyIconResource))
    }

    override fun setEmptyIconDrawable(drawable: Drawable?) {
        lavEmptyIcon.setImageDrawable(drawable)
    }

    override fun setOnEmptyIconClickListener(listener: OnIconClickListener) {
        mOnIconClickListener = listener
    }
}