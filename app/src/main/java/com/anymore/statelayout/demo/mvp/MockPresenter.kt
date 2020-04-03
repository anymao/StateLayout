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
    private val mockText = "臼井仪人先生说之所以会创造出小新这个形象，是因为他在观察自己的孩子的时候，发现小孩子的想法往往非常独特，以至于作者被小孩的世界所吸引。所有的小孩都有乖巧和调皮的两面性。这种两面性对作者来讲是十分有趣的。反过来作者正是在自己的作品中反映了这一两面性。他同时承认“小新”有一部分是他自己的翻版。据他透露，蜡笔小新里有许多内容是他现实生活的写照，例如：小新爸爸造型与他本人有些相似；小新和他爸爸两道浓眉毛乃是因作者自己嫌弃自己的眉毛太稀疏"

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

    override fun loadText() {
        val disposable = Maybe.create<String> {
            val random = IntRange(0, 10).random()
            Thread.sleep(5000)
            when {
                random % 3 == 0 -> {
                    it.onSuccess(mockText)
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
                mView.showText(it)
            }, onComplete = {
                mView.showText("")
            }, onError = {
                mView.showError(it.message ?: "加载失败")
            })
        mDisposable.add(disposable)
    }



    override fun destroy() {
        mDisposable.dispose()
    }
}