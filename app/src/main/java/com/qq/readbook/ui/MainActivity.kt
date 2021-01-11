package com.qq.readbook.ui

import android.content.Intent
import android.view.View
import com.hqq.core.toolbar.DefToolBar
import com.hqq.core.ui.list.BaseVmListActivity
import com.qq.readbook.BR
import com.qq.readbook.R
import com.qq.readbook.adapter.MainAdapter
import com.qq.readbook.databinding.ActivityMainBinding
import com.qq.readbook.ui.book.BookDetailActivity
import com.qq.readbook.ui.book.ReadBookActivity
import com.qq.readbook.utils.room.RoomUtils
import kotlinx.coroutines.*
import org.jsoup.nodes.Document

class MainActivity : BaseVmListActivity<MainViewModel, ActivityMainBinding>() {

    override val layoutId: Int = R.layout.activity_main

    override val bindingViewModelId: Int = BR.vm

    override val adapter: MainAdapter = MainAdapter().apply {
        setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.tv_detail -> {
                    BookDetailActivity.open(activity, getItem(position))
                }
                R.id.tv_delete -> {
                    RoomUtils.getBook().bookDao().delete(getItem(position))
                    removeAt(position)
                }
                R.id.ll_content -> {
                    ReadBookActivity.open(activity, getItem(position))
                }
                else -> {

                }
            }
        }


    }

    override fun initData() {
        (iToolBar as DefToolBar).leftView.visibility = View.GONE
        (iToolBar as DefToolBar).addRightImageView(R.mipmap.ic_search) {
            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
        }
        startActivity(Intent(this@MainActivity, SearchActivity::class.java))
    }

    private fun readSearch(doucument: Document?, className: String) {
        var list = doucument?.getElementsByClass(className)?.first()?.children()

    }
}