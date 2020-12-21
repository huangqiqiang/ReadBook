package com.qq.readbook.ui

import android.view.inputmethod.EditorInfo
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hqq.core.glide.ImageLoadUtils
import com.hqq.core.toolbar.BaseToolBarSearch
import com.hqq.core.ui.list.BaseVmListActivity
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

    override val adapter: BookAdapter = BookAdapter().apply {
        setOnItemChildClickListener { adapter, view, position ->

            when(view.id){
                R.id.ll_content ->{
                    BookDetailActivity.open(activity, getItem(position))
                }
            }

        }
    }

    override val bindingViewModelId: Int = BR.vm

    override fun initConfig() {
        super.initConfig()
        rootViewImpl.iCreateToolbar = BaseToolBarSearch()
    }

    override fun initData() {
        rootViewImpl.iCreateToolbar as BaseToolBarSearch
        (rootViewImpl.iCreateToolbar as BaseToolBarSearch).searchView.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onSearch()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        (rootViewImpl.iCreateToolbar as BaseToolBarSearch).setRightTextView() {
            onSearch()
        }
    }

    private fun onSearch() {
        var key = (rootViewImpl.iCreateToolbar as BaseToolBarSearch).searchView.text.toString()
        viewMode.onSearch(key)
    }




}