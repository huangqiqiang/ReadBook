package com.qq.readbook.ui

import com.hqq.core.ui.list.BaseListViewModel
import com.qq.readbook.BookSourceUtils
import com.qq.readbook.bean.Book
import com.qq.readbook.repository.SearchRepository
import java.util.*

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.ui
 * @Date : 下午 3:09
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
class SearchViewModel : BaseListViewModel() {

    fun onSearch(key: String) {
        pageCount = 1
        pageSize = 20
        for (bookSource in BookSourceUtils.getInstance().sourceList) {
            SearchRepository.doSearch(
                bookSource,
                key,
                object : SearchRepository.SearchRepositoryCallback {
                    override fun onSearchBook(book: ArrayList<Book>?, isSuccess: Boolean) {
                        if (isSuccess) {
                            data.value = book
                            pageCount++
                        } else {
                            setShowToast("请检查网络")
                        }
                        setShowLoading(false)
                    }
                })
        }


    }

}