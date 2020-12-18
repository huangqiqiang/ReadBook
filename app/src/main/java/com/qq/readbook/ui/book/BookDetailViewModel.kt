package com.qq.readbook.ui.book

import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.hqq.core.ui.base.BaseViewModel
import com.qq.readbook.Keys
import com.qq.readbook.bean.Book
import com.qq.readbook.repository.BookChaptersRepository
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
    var addBookMenu = MutableLiveData<Boolean>(true)
    override fun initData(extras: Bundle?) {
        super.initData(extras)
        extras?.getParcelable<Book>(Keys.BOOK).let {
            book.value = it
        }
    }

    override fun onCrete() {
        book.value?.let {
            BookChaptersRepository.getBookChapters(it)

            var b = RoomUtils.getDataBase().bookDao().getBookById(it.bookId)
            b?.let {
                if (it.name.equals(it.name)) {
                    //
                    //移出书架
                    addBookMenu.value = !(addBookMenu.value as Boolean)
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
        }
    }

    /**
     *  开始阅读
     */
    fun onRead(view: View) {
        book.value?.let {
            startActivity(ReadBookActivity::class.java, Bundle().apply {
                putParcelable("book", book.value)
            })
        }
    }


}