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
import com.anymore.statelayout.api.EmptyView
import com.anymore.statelayout.api.OnIconClickListener
import com.anymore.statelayout.creator.EmptyViewCreator
import com.anymore.statelayout.exts.getColorCompatibly
import com.anymore.statelayout.exts.getDimension
import com.anymore.statelayout.exts.getDimensionPixelSize
import com.anymore.statelayout.exts.getDrawableCompatibly

/**
 * 默认空白视图
 * Created by anymore on 2020/3/28.
 */
class DefaultEmptyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), EmptyView {

    companion object {
        const val TAG = "DefaultEmptyView"
    }

    private var ivEmptyIcon: ImageView
    private var tvEmptyMsg: TextView
    private var mOnIconClickListener: OnIconClickListener? = null
    private var mParentLayout: StateLayout? = null

    init {
        val ta =
            context.obtainStyledAttributes(attrs, R.styleable.DefaultEmptyView, defStyleAttr, 0)

        val emptyMessage = ta.getString(R.styleable.DefaultEmptyView_emptyMessage)
            ?: context.getString(R.string.sl_internal_no_data)
        val emptyIconId = ta.getResourceId(
            R.styleable.DefaultEmptyView_emptyIcon,
            R.drawable.sl_internal_ic_empty
        )
        ta.recycle()
        orientation = VERTICAL
        gravity = Gravity.CENTER
        ivEmptyIcon = ImageView(context).apply {
            ImageViewCompat.setImageTintList(
                this,
                ColorStateList.valueOf(context.getColorCompatibly(R.color.sl_internal_color))
            )
        }
        setEmptyIconResource(emptyIconId)
        ivEmptyIcon.setOnClickListener {
            mOnIconClickListener?.onClick(mParentLayout)
        }
        tvEmptyMsg = TextView(context)
        setEmptyMessage(emptyMessage)
        tvEmptyMsg.setTextColor(context.getColorCompatibly(R.color.sl_internal_color))
        tvEmptyMsg.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            context.getDimension(R.dimen.sl_internal_text_size)
        )
        val params1 = LayoutParams(
            context.getDimensionPixelSize(R.dimen.sl_internal_state_image_width),
            context.getDimensionPixelSize(R.dimen.sl_internal_state_image_height)
        )
        addView(ivEmptyIcon, params1)
        val params2 = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params2.topMargin = context.getDimensionPixelSize(R.dimen.sl_internal_margin)
        addView(tvEmptyMsg, params2)
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
        ivEmptyIcon.setImageDrawable(drawable)
    }

    override fun setOnEmptyIconClickListener(listener: OnIconClickListener) {
        mOnIconClickListener = listener
    }

    class Creator : EmptyViewCreator {
        override fun create(context: Context, layout: StateLayout): EmptyView =
            DefaultEmptyView(context)

    }

}