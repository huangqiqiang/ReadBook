package com.qq.readbook.ui

import android.content.Intent
import android.view.View
import com.hqq.core.toolbar.DefToolBar
import com.hqq.core.ui.list.BaseVmListActivity
import com.qq.readbook.BR
import com.qq.readbook.R
import com.qq.readbook.adapter.BookAdapter
import com.qq.readbook.databinding.ActivityMainBinding
import com.qq.readbook.ui.book.BookDetailActivity
import com.qq.readbook.ui.book.ReadBookActivity
import com.qq.readbook.utils.room.RoomUtils
import kotlinx.coroutines.*

class MainActivity : BaseVmListActivity<MainViewModel, ActivityMainBinding>() {


    override val layoutId: Int = R.layout.activity_main

    override val bindingViewModelId: Int = BR.vm

    override val adapter: BookAdapter = BookAdapter().apply {

        setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_detail -> {
                    BookDetailActivity.open(activity, getItem(position))
                }
                R.id.tv_delete -> {
                    removeAt(position)
                    RoomUtils.getDataBase().bookDao().delete(getItem(position))
                }
                R.id.ll_content -> {
                    ReadBookActivity.open(activity, getItem(position))
                }
                else -> {

                }
            }
        }
//        setOnItemChildLongClickListener { _, _, position ->
//            BookDetailActivity.open(activity, getItem(position))
//            true
//        }
    }

    override fun initData() {
        (iToolBar as DefToolBar).leftView.visibility = View.GONE
        (iToolBar as DefToolBar).addRightImageView(R.mipmap.ic_search) {
            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
        }




//        var coroutineScope = CoroutineScope(Dispatchers.Main)
//        var map = HashMap<Int, Job>()
//        for (i in 0..100) {
//            var job: Job = coroutineScope.launch(
//                newFixedThreadPoolContext(3, "down"), start = CoroutineStart.LAZY
//            ) {
//                LogUtils.e(i.toString() + "协程开始运行，时间: " + System.currentTimeMillis())
//                Thread.sleep(1000L)
//                LogUtils.e(i.toString() + "协程借宿运行，时间: " + System.currentTimeMillis())
//            }
////            job.wait()
//            map.put(i, job)
//        }
//        LogUtils.e("----------------1---------------" + System.currentTimeMillis())
//        coroutineScope.launch(   newFixedThreadPoolContext(3, "down")) {
//            LogUtils.e("----------------2---------------" + System.currentTimeMillis())
//            for (i in 0..100) {
//                LogUtils.e("----------------3---------------" + System.currentTimeMillis())
//                (map.get(100 - i) as Job).join()
//                LogUtils.e("----------------4---------------" + System.currentTimeMillis())
//            }
//            LogUtils.e("----------------5---------------" + System.currentTimeMillis())
//
//        }
//        LogUtils.e("-----------6--------------------" + System.currentTimeMillis())
    }
}