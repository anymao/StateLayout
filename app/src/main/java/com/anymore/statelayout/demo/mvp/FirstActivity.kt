package com.anymore.statelayout.demo.mvp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anymore.statelayout.StateLayout
import com.anymore.statelayout.StateLayout.Companion.CONTENT
import com.anymore.statelayout.StateLayout.Companion.EMPTY
import com.anymore.statelayout.StateLayout.Companion.LOADING
import com.anymore.statelayout.api.OnIconClickListener
import com.anymore.statelayout.demo.R
import com.anymore.statelayout.demo.adapter.DataAdapter
import com.anymore.statelayout.demo.adapter.OnItemClickListener
import kotlinx.android.synthetic.main.activity_first.*

/**
 * Created by anymore on 2020/4/3.
 */
class FirstActivity : AppCompatActivity(), IView {

    private lateinit var mAdapter: DataAdapter
    private lateinit var mPresenter: IPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        mAdapter = DataAdapter()
        mAdapter.mOnItemClickListener = object : OnItemClickListener {
            override fun invoke(p1: Int, p2: String) {
                startActivity(Intent(this@FirstActivity, SecondActivity::class.java))
            }

        }
        rvList.adapter = mAdapter
        mPresenter = MockPresenter(this)
        mPresenter.loadData()
        stateLayout.setOnErrorIconClickListener(object : OnIconClickListener {
            override fun onClick(layout: StateLayout?) {
                mPresenter.loadData()
                stateLayout.setState(LOADING)
            }
        })
        stateLayout.setOnEmptyIconClickListener(object : OnIconClickListener {
            override fun onClick(layout: StateLayout?) {
                mPresenter.loadData()
                stateLayout.setState(LOADING)
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