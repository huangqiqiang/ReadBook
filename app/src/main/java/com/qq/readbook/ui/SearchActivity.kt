package com.qq.readbook.ui

import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hqq.core.glide.ImageLoadUtils
import com.hqq.core.toolbar.BaseToolBarSearch
import com.hqq.core.ui.list.BaseVmListActivity
import com.hqq.core.utils.log.LogUtils
import com.qq.readbook.BR
import com.qq.readbook.R
import com.qq.readbook.adapter.BookAdapter
import com.qq.readbook.bean.Book
import com.qq.readbook.databinding.ActivitySearchBinding
import com.qq.readbook.ui.book.BookDetailActivity

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.ui
 * @Date  : 下午 2:24
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
class SearchActivity : BaseVmListActivity<SearchViewModel, ActivitySearchBinding>() {
    override val layoutId: Int = R.layout.activity_search
    override val bindingViewModelId: Int = BR.vm
    override val adapter: BookAdapter = BookAdapter().apply {
        setOnItemClickListener { _, _, position ->
            BookDetailActivity.open(activity, getItem(position))
        }
    }

    override fun initConfig() {
        super.initConfig()
        rootViewImpl.iToolBarBuilder.showToolBar = false
        rootViewImpl.iToolBarBuilder.showLine = false
    }


    val rawData = ArrayList<Book>()
    override fun initData() {
        onClickInit()
        binding.edtSearch.setText("宠魅")
        viewMode.liveBooks.observe(this, object : Observer<ArrayList<Book>> {
            override fun onChanged(t: ArrayList<Book>?) {
                t?.let {
                    rawData.addAll(it)
                    // 迭代 去除重复数据
                    addData2Adapter(it)
                }
            }
        })
        viewMode.searchModel.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean) {
                adapter.setNewInstance(ArrayList<Book>())
                addData2Adapter(rawData)
            }
        })
    }

    private fun addData2Adapter(it: ArrayList<Book>) {
        for (book in it) {
            LogUtils.e("SearchActivity   迭代书籍 " + book.name)
            if (viewMode.searchModel.value == true) {
                if (binding.edtSearch.text.toString() != book.name) {
                    LogUtils.e("SearchActivity   排除书籍 " + book.name)
                    continue
                }
            }
            adapter.data.find {
                it.name == book.name
            }.let {
                if (it == null) {
                    LogUtils.e("SearchActivity   未找到书籍 添加到列表 " + book.name)
                    adapter.addData(book)
                } else {
                    LogUtils.e("SearchActivity   找到书籍 排除重复数据 " + book.name)
                }
            }
        }
    }

    private fun onClickInit() {
        binding.edtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onSearch()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        binding.tvBarRight.setOnClickListener {
            onSearch()
        }
        binding.ivBarBack.setOnClickListener { onBackPressed() }
    }

    private fun onSearch() {
        var key = binding.edtSearch.text.toString()
        rawData.clear()
        adapter.data.clear()
        adapter.notifyDataSetChanged()
        viewMode.onSearch(key)
    }


}