package com.anymore.statelayout.demo.mvp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anymore.statelayout.StateLayout.Companion.CONTENT
import com.anymore.statelayout.StateLayout.Companion.EMPTY
import com.anymore.statelayout.StateLayout.Companion.LOADING
import com.anymore.statelayout.demo.R
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.layout_empty_page.*
import kotlinx.android.synthetic.main.layout_error_page.*

class SecondActivity : AppCompatActivity(), IView {

    private lateinit var mPresenter: IPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        mPresenter = MockPresenter(this)
        mPresenter.loadText()
        stateLayout.setState(LOADING)
        tvEmptyRetry.setOnClickListener {
            mPresenter.loadText()
            stateLayout.setState(LOADING)
        }
        tvErrorRetry.setOnClickListener {
            mPresenter.loadText()
            stateLayout.setState(LOADING)
        }
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
