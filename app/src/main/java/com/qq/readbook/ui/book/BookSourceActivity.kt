package com.qq.readbook.ui.book

import android.app.Activity
import android.content.Intent
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hqq.core.ui.list.BaseListActivity
import com.hqq.core.utils.log.LogUtils
import com.qq.readbook.BookSourceUtils.Companion.getInstance
import com.qq.readbook.Keys
import com.qq.readbook.R
import com.qq.readbook.bean.Book
import com.qq.readbook.bean.ReadSource
import com.qq.readbook.repository.BookDetailRepository
import com.qq.readbook.utils.room.RoomUtils

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.ui.book
 * @Date : 下午 3:00
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
class BookSourceActivity : BaseListActivity() {

    companion object {
        fun open(context: Activity) {
            context.startActivityForResult(Intent(context, BookSourceActivity::class.java), -1)
        }
    }


    override val adapter: BookSourceAdapter = BookSourceAdapter().apply {
        setOnItemClickListener { adapter, view, position ->

        }
    }

    lateinit var book: Book

    override fun initData() {
        book = intent.getParcelableExtra<Book>(Keys.BOOK)!!

        listModel.fillingData(getInstance().sourceList)
    }

    inner class BookSourceAdapter :
        BaseQuickAdapter<ReadSource, BaseViewHolder>(R.layout.item_book_source) {
        override fun convert(baseViewHolder: BaseViewHolder, readSource: ReadSource) {
            baseViewHolder.setText(R.id.tv_title, readSource.bookSourceName)

            var url = RoomUtils.getBook().bookSources()
                .getBookSource(readSource.bookSourceName, book.bookId)?.bookDetailUrl

            if (url.isNullOrEmpty()) {
                // 搜索查询详情页面
                LogUtils.e("BookSourceAdapter   " + book.name + "---" + readSource.bookSourceName + " :   空的 ")
            } else {
                // 请求url   查询源是否可用
                LogUtils.e("BookSourceAdapter   " + book.name + "---" + readSource.bookSourceName + " :   " + url)
                BookDetailRepository.doChapterUrl(book,object :BookDetailRepository.LatestChapter{
                    override fun onEnd(book: Book, isSuccess: Boolean) {

                    }
                })

            }
        }
    }
}