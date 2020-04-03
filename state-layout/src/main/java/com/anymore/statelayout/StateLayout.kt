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


    /**
     * 三种状态视图和一个内容视图是层叠堆积的
     * 从下至上为[0]EmptyView、[1]LoadingView、[2]ErrorView、[3]ContentView
     */
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
        val emptyLayout = inflateView(emptyLayoutId)
        val loadingLayout = inflateView(loadingLayoutId)
        val errorLayout = inflateView(errorLayoutId)
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
        addView(mErrorView.view(), 0, params)
        addView(mLoadingView.view(), 0, params)
        addView(mEmptyView.view(), 0, params)

        mEmptyView.view().visibility = if (mState == EMPTY) {
            View.VISIBLE
        } else {
            View.GONE
        }
        mLoadingView.view().visibility = if (mState == LOADING) {
            View.VISIBLE
        } else {
            View.GONE
        }
        mErrorView.view().visibility = if (mState == ERROR) {
            View.VISIBLE
        } else {
            View.GONE
        }
        mContentView?.visibility = if (mState == CONTENT) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    /**
     * 修正状态，在这里设置在xml中对于[StateLayout]设置的state属性
     * 之前尝试将这一步骤放在[onFinishInflate]中，这样会导致，如果
     * xml中设置的state不为[CONTENT]，在Activity中获取不到内容布局.
     */
//    override fun onAttachedToWindow() {
//        super.onAttachedToWindow()
//        if (mState != CONTENT) {
////            showViewByState(mState)
//            switchViewState(CONTENT, mState)
//        }
//    }

    /**
     * 设置内容视图
     */
    fun setContentView(@LayoutRes id: Int) {
        val view = inflateView(id)
        view?.let { setContentView(it) }
    }

    /**
     * 设置内容视图，
     */
    fun setContentView(view: View) {
        mContentView?.let { removeView(it) }
        mContentView = view
        mContentView?.visibility = if (mState == CONTENT) {
            View.VISIBLE
        } else {
            View.GONE
        }
        addView(view)
    }

    /**
     * 设置当前状态
     */
    fun setState(@State state: Int) {
        if (mState != state) {
//            showViewByState(state)
            switchViewState(mState, state)
            mState = state
        }
    }

    /**
     * 获取当前状态
     */
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

    private fun inflateView(@LayoutRes layoutId: Int): View? {
        return if (layoutId == NOT_SET) {
            null
        } else {
            LayoutInflater.from(context).inflate(layoutId, this, false)
        }
    }

    /**
     * 切换状态页面的展示与否:
     * [preState]切换前的状态，
     * [nextState]将要切换的下一个状态
     */
    private fun switchViewState(@State preState: Int, @State nextState: Int) {
        //隐藏上一个状态
        when (preState) {
            EMPTY -> mEmptyView.view().visibility = View.GONE
            LOADING -> mLoadingView.view().visibility = View.GONE
            ERROR -> mErrorView.view().visibility = View.GONE
            CONTENT -> mContentView?.visibility = View.GONE
        }
        //显示下一个状态
        when (nextState) {
            EMPTY -> mEmptyView.view().visibility = View.VISIBLE
            LOADING -> mLoadingView.view().visibility = View.VISIBLE
            ERROR -> mErrorView.view().visibility = View.VISIBLE
            CONTENT -> mContentView?.visibility = View.VISIBLE
        }
    }


    /**
     * 这个是之前用于切换视图的方法，但是这个切换方法是存在问题的，之前我想的是[StateLayout]
     * 是一个单Child的ViewGroup，在切换的时候将非选择状态对应视图进行移除，但是这样存在一个严重问题是
     * 在[StateLayout.findViewById]的时候，非显示状态的视图，获取不到视图中的view控件，所以来说，
     * 所有的状态视图和内容视图必须都是[StateLayout]的子View，而通过[View.setVisibility]来控制状态页面
     * 和内容页面的显示与否
     */
    @Deprecated(message = "切换视图的方法，但是有问题，不能使用")
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