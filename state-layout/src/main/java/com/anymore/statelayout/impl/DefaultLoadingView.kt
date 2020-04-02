package com.anymore.statelayout.impl

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import com.anymore.statelayout.R
import com.anymore.statelayout.StateLayout
import com.anymore.statelayout.api.LoadingView
import com.anymore.statelayout.creator.LoadingViewCreator
import com.anymore.statelayout.exts.getColorCompatibly
import com.anymore.statelayout.exts.getDimension
import com.anymore.statelayout.exts.getDimensionPixelSize
import com.anymore.statelayout.exts.getDrawableCompatibly

/**
 * Created by anymore on 2020/3/28.
 */
class DefaultLoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), LoadingView {

    companion object {
        const val TAG = "DefaultLoadingView"
    }

    private var pbLoading: ProgressBar
    private var tvLoadingMsg: TextView
    private lateinit var mParentLayout: StateLayout

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        pbLoading = ProgressBar(context)
        pbLoading.indeterminateDrawable =
            context.getDrawableCompatibly(R.drawable.sl_internal_loading_anim)?.apply {
                DrawableCompat.setTint(this, context.getColorCompatibly(R.color.sl_internal_color))
            }
        tvLoadingMsg = TextView(context)
        setLoadingMessage(context.getString(R.string.sl_internal_loading))
        tvLoadingMsg.setTextColor(context.getColorCompatibly(R.color.sl_internal_color))
        tvLoadingMsg.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            context.getDimension(R.dimen.sl_internal_text_size)
        )
        val params1 = LayoutParams(
            context.getDimensionPixelSize(R.dimen.sl_internal_progressbar_width),
            context.getDimensionPixelSize(R.dimen.sl_internal_progressbar_height)
        )
        addView(pbLoading, params1)
        val params2 = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params2.topMargin = context.getDimensionPixelSize(R.dimen.sl_internal_margin)
        addView(tvLoadingMsg, params2)
    }


    override fun attach(parent: StateLayout) {
        mParentLayout = parent
    }

    override fun view(): View = this

    override fun setLoadingMessage(message: String) {
        tvLoadingMsg.text = message
    }

    override fun setLoadingProgress(progress: Int) {
        //ignored
    }

    class Creator : LoadingViewCreator {
        override fun create(context: Context, layout: StateLayout): LoadingView =
            DefaultLoadingView(context)

    }
}