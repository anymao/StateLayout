package com.anymore.statelayout

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.IntDef
import androidx.annotation.LayoutRes
import com.anymore.statelayout.api.EmptyView
import com.anymore.statelayout.api.ErrorView
import com.anymore.statelayout.api.LoadingView
import com.anymore.statelayout.creator.EmptyViewCreator
import com.anymore.statelayout.creator.ErrorViewCreator
import com.anymore.statelayout.creator.LoadingViewCreator
import com.anymore.statelayout.impl.DefaultEmptyView
import com.anymore.statelayout.impl.DefaultErrorView
import com.anymore.statelayout.impl.DefaultLoadingView
import com.anymore.statelayout.wrapper.EmptyViewWrapper
import com.anymore.statelayout.wrapper.ErrorViewWrapper
import com.anymore.statelayout.wrapper.LoadingViewWrapper

/**
 * Created by anymore on 2020/3/25.
 */
class StateLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        const val EMPTY = 1
        const val LOADING = 2
        const val ERROR = 3
        const val CONTENT = 4

        private const val NO_LAYOUT = -1

        private const val TAG = "StateLayout"
        var globalEmptyViewCreator: EmptyViewCreator = DefaultEmptyView.Creator()
        var globalLoadingViewCreator: LoadingViewCreator = DefaultLoadingView.Creator()
        var globalErrorViewCreator: ErrorViewCreator = DefaultErrorView.Creator()
    }

    @IntDef(value = [EMPTY, LOADING, ERROR, CONTENT])
    @Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.SOURCE)
    annotation class State


    private var mEmptyView: EmptyView
    private var mLoadingView: LoadingView
    private var mErrorView: ErrorView
    private var mContentView: View? = null
    private val params: LayoutParams =
        LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT).apply {
            gravity = Gravity.CENTER
        }
    @State
    private var state: Int = CONTENT

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.StateLayout, defStyleAttr, 0)
        val emptyLayoutId = ta.getResourceId(R.styleable.StateLayout_emptyLayout, NO_LAYOUT)
        val loadingLayoutId = ta.getResourceId(R.styleable.StateLayout_loadingLayout, NO_LAYOUT)
        val errorLayoutId = ta.getResourceId(R.styleable.StateLayout_errorLayout, NO_LAYOUT)
        state = ta.getInteger(R.styleable.StateLayout_state, CONTENT)
        ta.recycle()
        val emptyLayout = inflateStateView(emptyLayoutId)
        val loadingLayout = inflateStateView(loadingLayoutId)
        val errorLayout = inflateStateView(errorLayoutId)
        mEmptyView = when (emptyLayout) {
            null -> {
                globalEmptyViewCreator.create(context)
            }
            is EmptyView -> {
                emptyLayout
            }
            else -> {
                EmptyViewWrapper(emptyLayout)
            }
        }
        mLoadingView = when (loadingLayout) {
            null -> {
                globalLoadingViewCreator.create(context)
            }
            is LoadingView -> {
                loadingLayout
            }
            else -> {
                LoadingViewWrapper(loadingLayout)
            }
        }
        mErrorView = when (errorLayout) {
            null -> {
                globalErrorViewCreator.create(context)
            }
            is ErrorView -> {
                errorLayout
            }
            else -> {
                ErrorViewWrapper(errorLayout)
            }
        }
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        when {
            childCount < 1 -> {
                Log.w(TAG, "StateLayout 没有Content布局")
            }
            childCount > 1 -> {
                throw IllegalStateException("StateLayout 最多只能有一个子View")
            }
            else -> {
                mContentView = getChildAt(0)
            }
        }
        if (state != CONTENT) {
            showViewByState(state)
        }
    }

    fun setState(@State target: Int) {
        if (state != target) {
            showViewByState(target)
            state = target
        }
    }

    private fun inflateStateView(@LayoutRes layoutId: Int): View? {
        return if (layoutId == NO_LAYOUT) {
            null
        } else {
            LayoutInflater.from(context).inflate(layoutId, this, false)
        }
    }


    private fun showViewByState(@State target: Int) {
        if (childCount > 0) {
            removeAllViews()
        }
        when (target) {
            EMPTY -> addView(mEmptyView.view(), params)
            LOADING -> addView(mLoadingView.view(), params)
            ERROR -> addView(mErrorView.view(), params)
            CONTENT -> mContentView?.let {
                addView(it, params)
            }
        }

    }

}