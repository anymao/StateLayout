package com.anymore.statelayout.wrapper

import android.view.View

/**
 * 对于状态页面没有实现StateView的子接口的时候，将页面View进行一层wrap
 * 保证操作时候的统一性
 * Created by anymore on 2020/3/30.
 */
abstract class ViewWrapper(protected val mRealView: View)