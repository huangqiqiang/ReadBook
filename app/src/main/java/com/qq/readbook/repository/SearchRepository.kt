package com.qq.readbook.repository

import com.hqq.core.net.ok.OkHttp
import com.hqq.core.net.ok.OkNetCallback
import com.hqq.core.utils.log.LogUtils
import com.qq.readbook.bean.Book
import com.qq.readbook.bean.BookSource
import java.lang.reflect.Method
import java.net.URLEncoder
import java.util.*


/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.repository
 * @Date : 上午 10:24
 * @Email : qiqiang213@gmail.com
 * @Describe :搜索小说
 */
object SearchRepository {
    /**
     *
     * @param key
     */
    @JvmStatic
    fun doSearch(source: BookSource, key: String, callback: SearchRepositoryCallback) {
        val url = if (source.searchEncode.isNotEmpty()) {
            String.format(source.bookSearchUrl, URLEncoder.encode(key, source.searchEncode))
        } else {
            String.format(source.bookSearchUrl, key)
        }
        LogUtils.e("doSearch     " + url)
        OkHttp.newHttpCompat().get(
            url,
            null,
            object : OkNetCallback {
                override fun onSuccess(statusCode: String?, response: String?) {
                    response?.let { it ->
                        val threadClazz = SearchRead::class.java
                        var books = threadClazz.methods.firstOrNull {
                            it.name == source.searchMethod
                        }?.invoke(null, it, source)
                        callback.onSearchBook(books as ArrayList<Book>?, true)
                    }
                }

                override fun onFailure(statusCode: String?, errMsg: String?, response: String?) {
                    callback.onSearchBook(null, false)
                }
            }
        )
    }


    interface SearchRepositoryCallback {
        fun onSearchBook(book: ArrayList<Book>?, isSuccess: Boolean)

    }


}