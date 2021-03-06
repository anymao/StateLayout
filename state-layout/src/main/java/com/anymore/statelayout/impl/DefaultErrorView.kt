package com.anymore.statelayout.impl

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.ImageViewCompat
import com.anymore.statelayout.R
import com.anymore.statelayout.StateLayout
import com.anymore.statelayout.api.ErrorView
import com.anymore.statelayout.api.OnIconClickListener
import com.anymore.statelayout.creator.ErrorViewCreator
import com.anymore.statelayout.exts.getColorCompatibly
import com.anymore.statelayout.exts.getDimension
import com.anymore.statelayout.exts.getDimensionPixelSize
import com.anymore.statelayout.exts.getDrawableCompatibly

/**
 * 默认错误视图
 * Created by anymore on 2020/3/28.
 */
class DefaultErrorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), ErrorView {

    companion object {
        const val TAG = "DefaultErrorView"
    }

    private var ivErrorIcon: ImageView
    private var tvErrorMsg: TextView
    private var mOnIconClickListener: OnIconClickListener? = null
    private var mParentLayout: StateLayout? = null

    init {
        val ta =
            context.obtainStyledAttributes(attrs, R.styleable.DefaultErrorView, defStyleAttr, 0)
        val errorMessage = ta.getString(R.styleable.DefaultErrorView_errorMessage)
            ?: context.getString(R.string.sl_internal_load_error)
        val errorIconId = ta.getResourceId(
            R.styleable.DefaultErrorView_errorIcon,
            R.drawable.sl_internal_ic_error
        )
        ta.recycle()
        orientation = VERTICAL
        gravity = Gravity.CENTER
        ivErrorIcon = ImageView(context)
        ImageViewCompat.setImageTintList(
            ivErrorIcon,
            ColorStateList.valueOf(context.getColorCompatibly(R.color.sl_internal_color))
        )
        setErrorIconResource(errorIconId)
        ivErrorIcon.setOnClickListener {
            mOnIconClickListener?.onClick(mParentLayout)
        }
        tvErrorMsg = TextView(context)
        setErrorMessage(errorMessage)
        tvErrorMsg.setTextColor(context.getColorCompatibly(R.color.sl_internal_color))
        tvErrorMsg.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            context.getDimension(R.dimen.sl_internal_text_size)
        )
        val params1 = LayoutParams(
            context.getDimensionPixelSize(R.dimen.sl_internal_state_image_width),
            context.getDimensionPixelSize(R.dimen.sl_internal_state_image_height)
        )
        addView(ivErrorIcon, params1)
        val params2 = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params2.topMargin = context.getDimensionPixelSize(R.dimen.sl_internal_margin)
        addView(tvErrorMsg, params2)
    }

    override fun attach(parent: StateLayout) {
        mParentLayout = parent
    }

    override fun view(): View = this

    override fun setErrorMessage(message: String) {
        tvErrorMsg.text = message
    }

    override fun setErrorIconResource(errorIconResource: Int) {
        setErrorIconDrawable(context.getDrawableCompatibly(errorIconResource))
    }

    override fun setErrorIconDrawable(drawable: Drawable?) {
        ivErrorIcon.setImageDrawable(drawable)
    }

    override fun setOnErrorIconClickListener(listener: OnIconClickListener) {
        mOnIconClickListener = listener
    }

    class Creator : ErrorViewCreator {
        override fun create(context: Context, layout: StateLayout): ErrorView =
            DefaultErrorView(context)

    }
}