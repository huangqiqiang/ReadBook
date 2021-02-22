package com.qq.readbook.ui.book

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.hqq.core.ui.base.BaseViewModel
import com.qq.readbook.App
import com.qq.readbook.Keys
import com.qq.readbook.bean.Book
import com.qq.readbook.bean.Chapter
import com.qq.readbook.repository.BookChaptersRepository
import com.qq.readbook.repository.BookDetailRepository
import com.qq.readbook.repository.ReadRepository.getBookRecord
import com.qq.readbook.utils.CommentUtils
import com.qq.readbook.utils.room.RoomUtils

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.ui.book
 * @Date  : 上午 9:29
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
class BookDetailViewModel : BaseViewModel() {

    val book = MutableLiveData<Book>()
    val chapters = MutableLiveData<List<Chapter>>()
    var addBookMenu = MutableLiveData<Boolean>(true)
    val currChapter = MutableLiveData<Int>(0)
    override fun initData(extras: Bundle?) {
        super.initData(extras)
        extras?.getParcelable<Book>(Keys.BOOK).let {
            book.value = it
        }
    }

    override fun onCrete() {
        book.value?.let {
            readChapters(it)
            readLocalBook(it)
            getBookDetail(it)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val sourceName = data?.getStringExtra(Keys.BOOK_SOURCE_NAME)
            if (sourceName != null) {
                book.value?.let {
                    it.sourceName = sourceName
                    val bookSources = RoomUtils.getBook().bookSources().getBookSource(sourceName, it.bookId)
                    it.chapterUrl = bookSources?.bookChapterUrl
                    // 更新信息
                    readChapters(it)
                    book.value = book.value
                }
            }
        }
    }

    /**
     *  获取章节信息
     * @param it Book
     */
    private fun readChapters(it: Book) {
        // 读取本地章节
        RoomUtils.getChapterDataBase(it.name + "_" + it.author).chapterDao().apply {
            chapters.value = it.sourceName?.let { it1 -> getAll(it1) }
        }
        // 爬取新目录
        BookChaptersRepository.getBookChapters(it, object : BookChaptersRepository.BookChaptersCall {
            override fun onSuccess(arrayList: List<Chapter>) {
                chapters.value = arrayList
            }
        })
    }

    /**
     * 加载本地书本信息
     * @param it Book
     */
    private fun readLocalBook(it: Book) {
        RoomUtils.getBook().bookDao().getBookById(it.bookId)?.let { it1 ->
            //id是唯一主键  其他内容 还是用上个界面传递的  避免数据混乱
            book.value?.id = it1.id
            //有查询到本地数据  那就是有收藏的 目前是真删除
            addBookMenu.value = !(addBookMenu.value as Boolean)
            getBookRecord(it1)?.let { it2 ->
                if (chapters.value != null) {
                    currChapter.value = it2.chapter
                }
            }
        }
    }

    /**
     * 判断是否需要加载详情 部分搜索页面获取的书籍是完整的
     * @param it Book
     */
    private fun getBookDetail(it: Book) {
        // 爬取详情页面
        val readSource = App.sourceList?.first { it1 ->
            it1.bookSourceName == it.sourceName
        }
        readSource?.let { it1 ->
            if (CommentUtils.isRefresh(it, it1.searchDetail)) {
                BookDetailRepository.readBookDetail(it, readSource, object : BookDetailRepository.ILatestChapter {
                    override fun onEndCall(b: Book, isSuccess: Boolean) {
                        book.postValue(b)
                    }
                })
            }
        }
    }

    /**
     * 追更
     */
    fun onAddBook(view: View) {
        book.value?.let {
            if (addBookMenu.value == true) {
                RoomUtils.getBook().bookDao().insertAll(it)
                setShowToast("添加成功")
            } else {
                RoomUtils.getBook().bookDao().delete(it)
                setShowToast("删除成功")
            }
            addBookMenu.value = !(addBookMenu.value as Boolean)
        }
    }

    /**
     *  开始阅读
     */
    fun onReadBook(view: View) {
        book.value?.let {
            startActivity(ReadBookActivity::class.java, Bundle().apply {
                putParcelable(Keys.BOOK, book.value)
            })
        }
    }

    /**
     *  切换源
     */
    fun onOtherSources(view: View) {
        startActivity(BookSourceActivity::class.java, Bundle().apply {
            putParcelable(Keys.BOOK, book.value)
        }, 1)
    }

}