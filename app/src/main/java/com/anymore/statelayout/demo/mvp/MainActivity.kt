package com.anymore.statelayout.demo.mvp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anymore.statelayout.StateLayout
import com.anymore.statelayout.StateLayout.Companion.CONTENT
import com.anymore.statelayout.StateLayout.Companion.EMPTY
import com.anymore.statelayout.StateLayout.Companion.LOADING
import com.anymore.statelayout.api.OnIconClickListener
import com.anymore.statelayout.demo.R
import com.anymore.statelayout.demo.adapter.DataAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IView {

    private lateinit var mPresenter: IPresenter
    private lateinit var mAdapter: DataAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPresenter = MockPresenter(this)
        mAdapter = DataAdapter()
        rvList.adapter = mAdapter

        stateLayout.setOnEmptyIconClickListener(object : OnIconClickListener {
            override fun onClick(layout: StateLayout?) {
                stateLayout.setState(LOADING)
                mPresenter.loadData()
            }

        })
        stateLayout.setOnErrorIconClickListener(object : OnIconClickListener {
            override fun onClick(layout: StateLayout?) {
                stateLayout.setState(LOADING)
                mPresenter.loadData()
            }

        })
        stateLayout.setState(LOADING)
        mPresenter.loadData()
    }

    override fun showError(message: String) {
        stateLayout.setErrorState(message)
    }

    override fun showData(list: List<String>) {
        mAdapter.setData(list)
        stateLayout.setState(
            if (list.isNullOrEmpty()) {
                EMPTY
            } else {
                CONTENT
            }
        )
    }
}
