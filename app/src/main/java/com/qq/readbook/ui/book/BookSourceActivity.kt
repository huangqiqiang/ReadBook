package com.qq.readbook.ui.book

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hqq.core.ui.list.BaseListActivity
import com.qq.readbook.BookSourceUtils.Companion.getInstance
import com.qq.readbook.R
import com.qq.readbook.bean.BookSource

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.ui.book
 * @Date : 下午 3:00
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
class BookSourceActivity : BaseListActivity() {
    override val adapter: BookSourceAdapter = BookSourceAdapter().apply {
        setOnItemClickListener { adapter, view, position ->



        }
    }

    override fun initData() {
        listModel.fillingData(getInstance().sourceList)
    }

    inner class BookSourceAdapter :
        BaseQuickAdapter<BookSource, BaseViewHolder>(R.layout.item_book_source) {
        override fun convert(baseViewHolder: BaseViewHolder, bookSource: BookSource) {
            baseViewHolder.setText(R.id.tv_title, bookSource.bookSourceName)
        }
    }
}