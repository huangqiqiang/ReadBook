package com.qq.readbook.ui.book

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hqq.core.ui.list.BaseListActivity
import com.hqq.core.utils.log.LogUtils
import com.qq.readbook.BookSourceUtils.Companion.getInstance
import com.qq.readbook.Keys
import com.qq.readbook.R
import com.qq.readbook.bean.Book
import com.qq.readbook.bean.ReadSource
import com.qq.readbook.repository.NewestChapterRepository
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
    override val layoutViewId: Int = R.layout.activity_booke_source

    companion object {
        fun open(context: Activity, book: Book?) {
            context.startActivityForResult(Intent(context, BookSourceActivity::class.java).apply {
                putExtra(Keys.BOOK, book)
            }, 0x2)
        }
    }

    override val adapter: BookSourceAdapter = BookSourceAdapter().apply {
        setOnItemClickListener { _, _, position ->
            setResult(Activity.RESULT_OK, Intent().apply {
                hashMap.get(getItem(position).bookSourceName)?.let {
                    putExtra(Keys.BOOK_SOURCE_NAME, getItem(position).bookSourceName)
                }
            })
            finish()
        }
    }

    lateinit var book: Book

    override fun initData() {
        book = intent.getParcelableExtra<Book>(Keys.BOOK)!!
        findViewById<TextView>(R.id.tv_currSource).text = "当前源:" + book.sourceName

        val list = ArrayList<ReadSource>()
        getInstance().sourceList?.let {
            for (readSource in it) {
                if (readSource.bookSourceName != book.sourceName) {
                    list.add(readSource)
                }
            }
        }
        listModel.fillingData(list)
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
                    NewestChapterRepository.doChapterUrl(book,
                        object : NewestChapterRepository.LatestChapter {
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