package com.anymore.statelayout.demo.mvp

/**
 * Created by anymore on 2020/4/1.
 */
interface IView {
    fun showError(message: String)
    fun showData(list: List<String>)
    fun showText(text: String?) {}
}