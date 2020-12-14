package com.qq.readbook.ui.book

import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.hqq.core.CoreConfig
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
 * @Descrive :
 */
class BookDetailViewModel : BaseViewModel() {

    val book = MutableLiveData<Book>()

    override fun initData(extras: Bundle?) {
        super.initData(extras)
        extras?.getParcelable<Book>(Keys.BOOK).let {
            book.value = it
        }
    }

    override fun onCrete() {


        book.value?.let { BookChaptersRepository.getBookChapters(it) }


    }

    /**
     * 追更
     */
    fun onAddBook(view: View) {
        book.value?.let {
            RoomUtils.getDataBase(CoreConfig.get().application!!).bookDao().insertAll(it)
            setShowToast("添加成功")
        }
    }

}