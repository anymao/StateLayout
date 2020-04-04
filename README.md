# StateLayout

## 简介

StateLayout是一个用于状态切换和管理的布局，在实际需求中，我们在进入某个页面后需要加载很多数据，需要使用加载占位页面来友好地提示用户；当页面加载失败或者页面数据为空的时候，我们还需要提示用户并且引导用户进行重试等操作。简单来说就是管理当前页面的空数据empty布局、加载中loading布局、加载出错error布局和加载成功content布局。

<img src="http://cdn.1or1.icu/image/device-2020-04-04-144151.gif" alt="使用效果" style="zoom:80%;" />

##  简单使用

1. 在Activity的根布局引入StateLayout，再将真正的内容布局作为其子View布局在StateLayout中：

   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <com.anymore.statelayout.StateLayout xmlns:android="http://schemas.android.com/apk/res/android"
       xmlns:app="http://schemas.android.com/apk/res-auto"
       xmlns:tools="http://schemas.android.com/tools"
       android:id="@+id/stateLayout"
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       app:state="loading">
   
       <androidx.recyclerview.widget.RecyclerView
           android:id="@+id/rvList"
           android:layout_width="match_parent"
           android:layout_height="match_parent"
           app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
           tools:listitem="@android:layout/simple_expandable_list_item_1" />
   </com.anymore.statelayout.StateLayout>
   ```

2. 通过StateLayout.setState()方法控制要显示的状态页面即可：

   ```kotlin
   stateLayout.setState(EMPTY)//显示空数据布局
   stateLayout.setState(LOADING)//显示loading布局
   stateLayout.setState(ERROR)//显示加载失败布局
   stateLayout.setState(CONTENT)//显示内容布局
   ```

## 自定义状态布局

如果不进行其他配置，直接使用StateLayout，其内部会使用一套默认的状态布局的实现：DefaultEmptyView、DefaultLoadingView、DefaultErrorView，如果想要替换这个内置布局，则有两种方式：

### 全局替换

示例demo中，以lottie动画实现了三种状态布局的动画展示，实现这个效果只需要两步(以实现空白布局为例，其他两种状态布局同理)：

1. 自定义LottieEmptyView实现EmptyView接口：

   ```kotlin
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
   ```

   

2. 在Application的静态代码块部分将将生成LottieEmptyView的Creator设置进去：

   ```kotlin
   class App : Application() {
       companion object {
           init {
               StateLayout.globalEmptyViewCreator = object : EmptyViewCreator {
                   override fun create(context: Context, layout: StateLayout) =
                       LottieEmptyView(context)
               }
               StateLayout.globalErrorViewCreator = object : ErrorViewCreator {
                   override fun create(context: Context, layout: StateLayout) =
                       LottieErrorView(context)
               }
               StateLayout.globalLoadingViewCreator = object : LoadingViewCreator {
                   override fun create(context: Context, layout: StateLayout) =
                       LottieLoadingView(context)
   
               }
           }
       }
   }
   ```

这样即完成了全局配置状态页：

![自定义状态页面](http://cdn.1or1.icu/image/device-2020-04-04-144550.gif)

## 局部替换

局部替换需要先编写xml布局，然后通过在StateLayout的属性中添加进去即可，需要特别注意的是，如果设置的xml布局其根节点并非是EmptyView、LoadingView、ErrorView的子类的话，只能调用StateLayout的getState、setState方法，其他方法无效。具体步骤如下(以替换EmptyView为例)：

1. 自定义布局 layout_empty_page.xml：

   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       android:gravity="center"
       android:orientation="vertical">
   
       <TextView
           android:id="@+id/tvErrorRetry"
           android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:layout_marginBottom="24dp"
           android:text="点这里重试" />
   
       <TextView
           android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:layout_gravity="center"
           android:text="这是自定义的空界面，代表没有数据" />
   </LinearLayout>
   ```

2. 在要替换的StateLayout使用属性 emptyLayout指向该布局:

   ```kotlin
   <?xml version="1.0" encoding="utf-8"?>
   <com.anymore.statelayout.StateLayout xmlns:android="http://schemas.android.com/apk/res/android"
       xmlns:app="http://schemas.android.com/apk/res-auto"
       android:id="@+id/stateLayout"
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       app:emptyLayout="@layout/layout_empty_page"
       app:errorLayout="@layout/layout_error_page"
       app:loadingLayout="@layout/layout_loading_page"
       app:state="loading">
   
       <TextView
           android:id="@+id/tvText"
           android:layout_width="match_parent"
           android:layout_height="match_parent"
           android:gravity="center"
           android:text="Hello" />
   
   </com.anymore.statelayout.StateLayout>
   ```

即可完成替换。xml中通过属性设置状态布局的优先级高于全局设置Creator。