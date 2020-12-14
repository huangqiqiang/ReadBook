package com.qq.readbook.ui

import android.content.Intent
import android.view.View
import com.hqq.core.toolbar.DefToolBar
import com.hqq.core.ui.list.BaseVmListActivity
import com.qq.readbook.BR
import com.qq.readbook.R
import com.qq.readbook.adapter.BookAdapter
import com.qq.readbook.databinding.ActivityMainBinding
import com.qq.readbook.ui.book.ReadBookActivity

class MainActivity : BaseVmListActivity<MainViewModel, ActivityMainBinding>() {

    override val layoutId: Int = R.layout.activity_main

    override val bindingViewModelId: Int = BR.vm

    override val adapter: BookAdapter = BookAdapter().apply {
        setOnItemClickListener { _, _, position ->
            ReadBookActivity.open(activity, getItem(position))
        }
    }

    override fun initData() {
        (iToolBar as DefToolBar).leftView.visibility = View.GONE
        (iToolBar as DefToolBar).addRightImageView(R.mipmap.ic_search) {
            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
        }
    }


}