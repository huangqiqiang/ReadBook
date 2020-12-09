package com.qq.readbook.ui

import android.view.inputmethod.EditorInfo
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hqq.core.glide.ImageLoadUtils
import com.hqq.core.toolbar.BaseToolBarSearch
import com.hqq.core.ui.list.BaseVmListActivity
import com.qq.readbook.BR
import com.qq.readbook.R
import com.qq.readbook.bean.Book
import com.qq.readbook.databinding.ActivitySearchBinding
import com.qq.readbook.ui.book.BookDetailActivity

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
        setOnItemClickListener { _, view, position ->
            BookDetailActivity.open(activity, getItem(position))
        }
    }

    override val bindingViewModelId: Int = BR.vm

    override fun initConfig() {
        super.initConfig()
        iCreateRootView.iRootViewImpl.iCreateToolbar = BaseToolBarSearch()
    }

    override fun initData() {
        iCreateRootView.iRootViewImpl.iCreateToolbar as BaseToolBarSearch
        (iCreateRootView.iRootViewImpl.iCreateToolbar as BaseToolBarSearch).searchView.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onSearch()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        (iCreateRootView.iRootViewImpl.iCreateToolbar as BaseToolBarSearch).setRightTextView() {
            onSearch()
        }
    }

    private fun onSearch() {
        var key =
            (iCreateRootView.iRootViewImpl.iCreateToolbar as BaseToolBarSearch).searchView.text.toString()
        viewMode.onSearch(key)
    }

    class BookAdapter : BaseQuickAdapter<Book, BaseViewHolder>(R.layout.item_book) {
        override fun convert(holder: BaseViewHolder, item: Book) {
            holder.setText(R.id.tv_book_name, item.name)
            holder.setText(R.id.tv_book_desc, item.desc)
            holder.setText(R.id.tv_book_author, item.author)
            holder.setText(R.id.tv_book_type, item.type)
            ImageLoadUtils.with(item.imgUrl,holder.getView(R.id.iv_book_img))
        }
    }


}