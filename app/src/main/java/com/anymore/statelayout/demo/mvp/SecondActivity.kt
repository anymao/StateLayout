package com.anymore.statelayout.demo.mvp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anymore.statelayout.StateLayout
import com.anymore.statelayout.StateLayout.Companion.CONTENT
import com.anymore.statelayout.StateLayout.Companion.EMPTY
import com.anymore.statelayout.StateLayout.Companion.LOADING
import com.anymore.statelayout.api.OnIconClickListener
import com.anymore.statelayout.demo.R
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity(), IView {

    private lateinit var mPresenter: IPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        mPresenter = MockPresenter(this)
        mPresenter.loadText()
//        tvText.text = "lym"
//        stateLayout.setState(LOADING)
        stateLayout.setOnErrorIconClickListener(object : OnIconClickListener {
            override fun onClick(layout: StateLayout?) {
                mPresenter.loadText()
                stateLayout.setState(StateLayout.LOADING)
            }
        })
        stateLayout.setOnEmptyIconClickListener(object : OnIconClickListener {
            override fun onClick(layout: StateLayout?) {
                mPresenter.loadText()
                stateLayout.setState(StateLayout.LOADING)
            }

        })
    }

    override fun onDestroy() {
        mPresenter.destroy()
        super.onDestroy()
    }

    override fun showError(message: String) {
        stateLayout.setErrorState(message)
    }

    override fun showData(list: List<String>) {

    }

    override fun showText(text: String?) {
        tvText.text = text
        stateLayout.setState(
            if (text.isNullOrEmpty()) {
                EMPTY
            } else {
                CONTENT
            }
        )
    }

}
