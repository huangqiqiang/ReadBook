package com.qq.readbook.repository

import com.hqq.core.net.ok.OkHttp
import com.hqq.core.net.ok.OkNetCallback
import com.hqq.core.utils.log.LogUtils
import com.qq.readbook.bean.Book
import com.qq.readbook.bean.ReadSource
import java.net.URLEncoder
import java.util.*


/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.repository
 * @Date : 上午 10:24
 * @Email : qiqiang213@gmail.com
 * @Describe :搜索小说
 */
object BookSearchRepository {
    /**
     *
     * @param key
     */
    @JvmStatic
    fun doSearch(source: ReadSource, key: String, callback: SearchRepositoryCallback) {
        val call = object : OkNetCallback {
            override fun onSuccess(statusCode: String?, response: String?) {
                response?.let { it ->
                    val clas =
                        Class.forName("com.qq.readbook.repository.read." + source.sourcesClass)
                    val method = clas.methods.firstOrNull {
                        it.name == "readSearch"
                    }
                    val books = method?.invoke(clas.newInstance(), it, source)
                    callback.onSearchBook(books as ArrayList<Book>?, true)
                }
            }

            override fun onFailure(statusCode: String?, errMsg: String?, response: String?) {
                callback.onSearchBook(null, false)
            }
        }
        LogUtils.e("doSearch     " + source.bookSearchUrl + "  " + key)
        if (source.bookSearchUrl.contains("@")) {
            //post 表单请求
            doPost(source, key, call)
        } else {
            //get 表单请求
            doGet(source, key, call)
        }
    }


    /**
     *  post 提交
     *
     */
    private fun doPost(
        source: ReadSource,
        key: String,
        callback: OkNetCallback
    ) {
        val keys = source.bookSearchUrl.split("@")
        OkHttp.newHttpCompat()
            .postExecute(keys[0], OkHttp.newParamsCompat().apply { put(keys[1], key) }, callback)


    }

    /**
     *  get 请求
     */
    private fun doGet(
        source: ReadSource,
        key: String,
        callback: OkNetCallback
    ) {
        val url = if (source.searchEncode.isNotEmpty()) {
            String.format(source.bookSearchUrl, URLEncoder.encode(key, source.searchEncode))
        } else {
            String.format(source.bookSearchUrl, key)
        }
        OkHttp.newHttpCompat().getExecute(url, null, callback)
    }


    interface SearchRepositoryCallback {
        fun onSearchBook(book: ArrayList<Book>?, isSuccess: Boolean)
    }


}