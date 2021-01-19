package com.qq.readbook.ui.book

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ProgressBar
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        setOnItemClickListener { _, _, position ->
            setResult(Activity.RESULT_OK, Intent().apply {
                hashMap.get(getItem(position).bookSourceName)?.let {
                    if (it) {
                        putExtra(Keys.BOOK_SOURCE_NAME, getItem(position).bookSourceName)
                        finish()
                    }
                }

            })
        }
    }

    lateinit var book: Book

    override fun initData() {
        book = intent.getParcelableExtra<Book>(Keys.BOOK)!!

        getInstance().sourceList?.let { listModel.fillingData(it) }
    }

    inner class BookSourceAdapter : BaseQuickAdapter<ReadSource, BaseViewHolder>(R.layout.item_book_source) {
        val hashMap = HashMap<String, Boolean>()

        override fun convert(baseViewHolder: BaseViewHolder, readSource: ReadSource) {
            baseViewHolder.setText(R.id.tv_title, readSource.bookSourceName)
            val url =
                RoomUtils.getBook().bookSources().getBookSource(readSource.bookSourceName, book.bookId)?.bookDetailUrl

            doNet(url, readSource, baseViewHolder)
        }

        private fun doNet(url: String?, readSource: ReadSource, baseViewHolder: BaseViewHolder) {
            if (url.isNullOrEmpty()) {
                // 搜索查询详情页面
                LogUtils.e("BookSourceAdapter   " + book.name + "---" + readSource.bookSourceName + " :   空的 ")
            } else {
                // 请求url   查询源是否可用
                LogUtils.e("BookSourceAdapter   " + book.name + "---" + readSource.bookSourceName + " :   " + url)
                CoroutineScope(Dispatchers.IO).launch {
                    BookDetailRepository.doChapterUrl(book,
                        object : BookDetailRepository.LatestChapter {
                            override fun onEnd(book: Book, isSuccess: Boolean) {
                                CoroutineScope(Dispatchers.Main).launch {
                                    baseViewHolder.getView<ProgressBar>(R.id.pb_bar).visibility = View.GONE
                                    hashMap.put(readSource.bookSourceName, true)
                                }
                            }
                        })
                }
            }
        }
    }

}