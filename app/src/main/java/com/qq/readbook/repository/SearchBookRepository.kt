package com.qq.readbook.repository

import com.google.gson.*
import com.hqq.core.net.ok.OkHttp
import com.hqq.core.net.ok.OkNetCallback
import com.hqq.core.utils.GsonUtil
import com.hqq.core.utils.log.LogUtils
import com.qq.readbook.Keys
import com.qq.readbook.bean.Book
import com.qq.readbook.bean.BookSources
import com.qq.readbook.bean.ReadSource
import com.qq.readbook.repository.JsoupUtils.getJsonElement
import com.qq.readbook.utils.room.RoomUtils
import org.jsoup.Jsoup
import java.net.URLEncoder
import java.util.*


/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.repository
 * @Date : 上午 10:24
 * @Email : qiqiang213@gmail.com
 * @Describe :搜索小说
 */
object SearchBookRepository {
    /**
     *
     * @param key
     */
    @JvmStatic
    fun doSearch(source: ReadSource, key: String, callback: SearchRepositoryCallback) {
        val call = object : OkNetCallback {
            override fun onSuccess(statusCode: String?, response: String?) {
                response?.let { it ->

                    LogUtils.d("-------------start-------------------")
                    LogUtils.d(GsonUtil.toJsonString(source))
                    LogUtils.d("--------------------------------")
                    LogUtils.d("--------------Html------------------")
                    LogUtils.d(response)
                    LogUtils.d("--------------------------------")
                    LogUtils.d("----------------end----------------")
                    val books = doReadBookList(it, source)
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

    fun doReadBookList(html: String, source: ReadSource): ArrayList<Book> {
        val books: ArrayList<Book> = ArrayList<Book>()
        val doc = Jsoup.parse(html)
        val searchElement = getJsonElement(source.searchRule)
        searchElement?.let {
            val array = it.asJsonObject.get(Keys.RULE_SEARCH_LIST)
            if (array is JsonArray) {
                for (jsonElement in array.asJsonArray) {
                    if (jsonElement is JsonObject) {
                        if ((jsonElement.asJsonObject).get(Keys.ELEMENT_TYPE).asString == Keys.ATTR_CLASS) {
                            JsoupUtils.getElementList(doc, jsonElement)?.first()?.let {
                                for (child in it.children()) {
                                    val book = JsoupUtils.doReadBook(child, source, searchElement)
                                    books.add(book)
                                    // 保存搜索源记录
                                    val bookSources = BookSources()
                                    bookSources.bookId = book.bookId
                                    bookSources.sourcesName = book.source
                                    bookSources.bookDetailUrl = book.chapterUrl
                                    RoomUtils.getBook().bookSources().insertAll(bookSources);
                                }
                            }
                        }
                    }
                }
            }
        }
        return books


    }


    /**
     *  post 提交
     *
     */
    private fun doPost(source: ReadSource, key: String, callback: OkNetCallback) {
        val keys = source.bookSearchUrl.split("@")
        OkHttp.newHttpCompat()
            .postExecute(keys[0], OkHttp.newParamsCompat().apply { put(keys[1], key) }, callback)
    }

    /**
     *  get 请求
     */
    private fun doGet(source: ReadSource, key: String, callback: OkNetCallback) {
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

