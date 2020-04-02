package com.anymore.statelayout

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.IntDef
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import com.anymore.statelayout.api.EmptyView
import com.anymore.statelayout.api.ErrorView
import com.anymore.statelayout.api.LoadingView
import com.anymore.statelayout.api.OnIconClickListener
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
        //状态枚举
        //空状态，无数据
        const val EMPTY = 1
        //加载中
        const val LOADING = 2
        //加载出错
        const val ERROR = 3
        //真正内容布局
        const val CONTENT = 4

        //资源未设置
        private const val NOT_SET = Integer.MIN_VALUE

        private const val TAG = "StateLayout"
        //全局的状态视图创建器
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

    /**
     * 当前状态
     */
    @State
    private var mState: Int = CONTENT

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.StateLayout, defStyleAttr, 0)
        val emptyLayoutId = ta.getResourceId(R.styleable.StateLayout_emptyLayout, NOT_SET)
        val loadingLayoutId = ta.getResourceId(R.styleable.StateLayout_loadingLayout, NOT_SET)
        val errorLayoutId = ta.getResourceId(R.styleable.StateLayout_errorLayout, NOT_SET)
        mState = ta.getInteger(R.styleable.StateLayout_state, CONTENT)
        ta.recycle()
        val emptyLayout = inflateStateView(emptyLayoutId)
        val loadingLayout = inflateStateView(loadingLayoutId)
        val errorLayout = inflateStateView(errorLayoutId)
        mEmptyView = when (emptyLayout) {
            null -> globalEmptyViewCreator.create(context, this)
            is EmptyView -> emptyLayout
            else -> EmptyViewWrapper(emptyLayout)
        }
        mEmptyView.attach(this)
        mLoadingView = when (loadingLayout) {
            null -> globalLoadingViewCreator.create(context, this)
            is LoadingView -> loadingLayout
            else -> LoadingViewWrapper(loadingLayout)
        }
        mLoadingView.attach(this)
        mErrorView = when (errorLayout) {
            null -> globalErrorViewCreator.create(context, this)
            is ErrorView -> errorLayout
            else -> ErrorViewWrapper(errorLayout)
        }
        mErrorView.attach(this)
    }

    /**
     * 在加载完成xml视图后调用，在这里可以读取其子View
     * [mContentView]就是在这个时候被持有的
     */
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
        if (mState != CONTENT) {
            showViewByState(mState)
        }
    }

    /**
     * 设置当前状态
     */
    fun setState(@State state: Int) {
        if (mState != state) {
            showViewByState(state)
            mState = state
        }
    }

    fun getState(): Int = mState

    /**
     * 设置空布局提示语和资源，并且更新当前布局状态为[EMPTY]
     */
    fun setEmptyState(message: String, emptyIconId: Int = NOT_SET) {
        if (emptyIconId != NOT_SET) {
            mEmptyView.setEmptyIconResource(emptyIconId)
        }
        mEmptyView.setEmptyMessage(message)
        setState(EMPTY)
    }

    /**
     * 设置错误布局提示语和资源，并且更新当前布局状态为[ERROR]
     */
    fun setErrorState(message: String, errorIconId: Int = NOT_SET) {
        if (errorIconId != NOT_SET) {
            mErrorView.setErrorIconResource(errorIconId)
        }
        mErrorView.setErrorMessage(message)
        setState(ERROR)
    }

    /**
     * 设置加载中布局提示语和资源，并且更新当前布局状态为[LOADING]
     */
    fun setLoadingState(message: String, @IntRange(from = 0, to = 100) progress: Int) {
        mLoadingView.setLoadingProgress(progress)
        mLoadingView.setLoadingMessage(message)
        setState(
            if (progress == 100) {
                CONTENT
            } else {
                LOADING
            }
        )
    }

    fun setOnEmptyIconClickListener(listener: OnIconClickListener) {
        mEmptyView.setOnEmptyIconClickListener(listener)
    }

    fun setOnErrorIconClickListener(listener: OnIconClickListener) {
        mErrorView.setOnErrorIconClickListener(listener)
    }

    fun getEmptyView(): View = mEmptyView.view()

    fun getErrorView(): View = mErrorView.view()

    fun getLoadingView(): View = mLoadingView.view()

    private fun inflateStateView(@LayoutRes layoutId: Int): View? {
        return if (layoutId == NOT_SET) {
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