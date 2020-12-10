package com.qq.readbook.ui.book

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.hqq.core.ui.base.BaseViewModel
import com.qq.readbook.Keys
import com.qq.readbook.bean.Book

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


    }
}