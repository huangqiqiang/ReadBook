package com.qq.readbook.ui

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.hqq.core.ui.list.BaseListViewModel
import com.qq.readbook.BookSourceUtils
import com.qq.readbook.bean.Book
import com.qq.readbook.bean.ReadSource
import com.qq.readbook.repository.SearchBookRepository
import kotlinx.coroutines.*
import java.util.*

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.ui
 * @Date : 下午 3:09
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
class SearchViewModel : BaseListViewModel() {
    val liveBooks = MutableLiveData<ArrayList<Book>>()
    var searchModel = MutableLiveData<Boolean>(true)
    fun onSearch(key: String) {
        pageCount = 1
        pageSize = 20
        CoroutineScope(Dispatchers.Main).launch {
            for (bookSource in BookSourceUtils.getInstance().sourceList!!) {
                doSearch(bookSource, key)
            }
        }
    }

    private suspend fun doSearch(readSource: ReadSource, key: String) {
        withContext(Dispatchers.IO) {
            SearchBookRepository.doSearch(readSource, key, object : SearchBookRepository.SearchRepositoryCallback {
                override fun onSearchBook(book: ArrayList<Book>?, isSuccess: Boolean) {
                    GlobalScope.launch(Dispatchers.Main) {
                        liveBooks.value = book
                    }
                }
            })
        }
    }

    fun onSearchModel(v: View) {
        searchModel.value = !searchModel.value!!
    }
}