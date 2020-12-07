package com.qq.readbook.ui

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hqq.core.toolbar.BaseToolBarSearch
import com.hqq.core.ui.list.BaseVmListActivity
import com.qq.readbook.BR
import com.qq.readbook.R
import com.qq.readbook.bean.Book
import com.qq.readbook.databinding.ActivitySearchBinding

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.ui
 * @Date  : 下午 2:24
 * @Email : qiqiang213@gmail.com
 * @Descrive :
 */
class SearchActivity : BaseVmListActivity<SearchViewModel, ActivitySearchBinding>() {
    override val layoutId: Int = R.layout.activity_search

    override val adapter: BookAdapter = BookAdapter().apply {
        setOnItemClickListener { adapter, view, position ->


        }
    }

    override val bindingViewModelId: Int
        get() = BR.vm

    override fun initConfig() {
        super.initConfig()
        iCreateRootView.iRootViewImpl.iCreateToolbar = BaseToolBarSearch()

    }

    override fun initData() {
        (iCreateRootView.iRootViewImpl.iCreateToolbar as BaseToolBarSearch).setRightTextView() {
            var key =
                (iCreateRootView.iRootViewImpl.iCreateToolbar as BaseToolBarSearch).searchView.text.toString()
            viewMode.onSearch(key)
        }
    }

    class BookAdapter : BaseQuickAdapter<Book, BaseViewHolder>(R.layout.item_book) {
        override fun convert(holder: BaseViewHolder, item: Book) {
            holder.setText(R.id.tv_book_name, item.name)
            holder.setText(R.id.tv_book_desc, item.desc)
            holder.setText(R.id.tv_book_author, item.author)
            holder.setText(R.id.tv_book_type, item.type)

        }

    }


}