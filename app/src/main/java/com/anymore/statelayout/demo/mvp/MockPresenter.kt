package com.anymore.statelayout.demo.mvp

import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

/**
 * Created by anymore on 2020/4/1.
 */
class MockPresenter(private val mView: IView) : IPresenter {

    private val mDisposable: CompositeDisposable = CompositeDisposable()
    private val mockData = mutableListOf(
        "苹果", "沙果", "海棠", "野樱莓", "枇杷",
        "欧楂", "山楂", "橘子", "砂糖桔", "橙子",
        "柠檬", "青柠", "柚子", "金桔", "葡萄柚",
        "香橼", "佛手", "指橙", "哈密瓜", "香瓜",
        "白兰瓜", "刺角瓜"
    )

    override fun loadData() {
        val disposable = Maybe.create<List<String>> {
            val random = IntRange(0, 10).random()
            Thread.sleep(5000)
            when {
                random % 3 == 0 -> {
                    it.onSuccess(mockData)
                }
                random % 2 == 0 -> {
                    it.onError(Exception("加载数据失败了"))
                }
                else -> {
                    it.onComplete()
                }
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = {
                mView.showData(it)
            }, onComplete = {
                mView.showData(emptyList())
            }, onError = {
                mView.showError(it.message ?: "加载失败")
            })
        mDisposable.add(disposable)
    }

    override fun destroy() {
        mDisposable.dispose()
    }
}