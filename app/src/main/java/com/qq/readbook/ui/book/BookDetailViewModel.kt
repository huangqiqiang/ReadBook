package com.qq.readbook.ui.book

import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.hqq.core.ui.base.BaseViewModel
import com.qq.readbook.Keys
import com.qq.readbook.bean.Book
import com.qq.readbook.bean.Chapter
import com.qq.readbook.repository.BookChaptersRepository
import com.qq.readbook.repository.ReadRepository.getBookRecord
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

            RoomUtils.getChapterDataBase(it.name + "_" + it.author).chapterDao().apply {
                chapters.value = getAll()
            }

            // 爬取最新目录
            BookChaptersRepository.getBookChapters(it,
                object : BookChaptersRepository.BookChaptersCall {
                    override fun onSuccess(arrayList: List<Chapter>) {
                        chapters.value = arrayList
                    }
                })
            var b = RoomUtils.getDataBase().bookDao().getBookById(it.bookId)
            b?.let {
                // t
                book.value = b
                //有查询到本地数据  那就是有收藏的 目前是真删除
                addBookMenu.value = !(addBookMenu.value as Boolean)
                //todo 获取阅读记录
                getBookRecord(b)?.let {
                    if (chapters.value != null) {
                        currChapter.value=it.chapter
                    }
                }

            }

        }
    }

    /**
     * 追更
     */
    fun onAddBook(view: View) {
        book.value?.let {
            if (addBookMenu.value == true) {
                RoomUtils.getDataBase().bookDao().insertAll(it)
                setShowToast("添加成功")
            } else {
                RoomUtils.getDataBase().bookDao().delete(it)
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
                putParcelable("book", book.value)
            })
        }
    }


}