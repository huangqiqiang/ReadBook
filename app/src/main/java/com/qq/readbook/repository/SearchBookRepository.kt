package com.qq.readbook.repository

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.hqq.core.net.ok.OkHttp
import com.hqq.core.net.ok.OkNetCallback
import com.hqq.core.utils.GsonUtil
import com.hqq.core.utils.log.LogUtils
import com.qq.readbook.Keys
import com.qq.readbook.bean.Book
import com.qq.readbook.bean.BookSources
import com.qq.readbook.bean.ReadSource
import com.qq.readbook.bean.RuleSearchBean
import com.qq.readbook.repository.read.JsoupUtils
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
        val searchRuleBean = GsonUtil.fromJson<RuleSearchBean>(source.ruleSearch, RuleSearchBean::class.java)
        val books = doReadBookList4Source(searchRuleBean, html)
        for (book in books) {
            book.sourceName = source.bookSourceName
            addSearchLog(book)
        }
        return books
    }

    /**
     *
     * @param searchElement RuleSearchBean?
     * @param html String
     * @return ArrayList<Book>
     */
    fun doReadBookList4Source(searchElement: RuleSearchBean?, html: String): ArrayList<Book> {
        val books: ArrayList<Book> = ArrayList<Book>()
        val doc = Jsoup.parse(html)
        searchElement?.let { it ->
            val array = it.ruleSearchList
            if (array is JsonArray) {
                for (jsonElement in array.asJsonArray) {
                    if (jsonElement is JsonObject) {
                        val nodeBean = JsoupUtils.getNodeBean(jsonElement)
                        if (nodeBean?.elementType == Keys.ATTR_CLASS) {
                            JsoupUtils.getElements(doc, jsonElement)?.let {
                                for (child in it) {
                                    val book = JsoupUtils.doReadBook(child, searchElement)
                                    book?.let { it1 ->
                                        books.add(it1)
                                    }
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
     *  保存搜索记录 以便切换数据源使用
     * @param book Book
     */
    private fun addSearchLog(book: Book) {
        val bookSources = BookSources()
        bookSources.bookId = book.bookId
        bookSources.bookName = book.name
        bookSources.sourcesName = book.sourceName
        bookSources.bookDetailUrl = book.bookDetailUrl
        bookSources.bookChapterUrl = book.chapterUrl
        RoomUtils.getBook().bookSources().apply {
            book.sourceName?.let { it1 ->
                val b = getBookSource(it1, book.bookId)
                if (b == null) {
                    insertAll(bookSources);
                } else {
                    bookSources.id = b.id
                    update(bookSources)
                }
            }
        }
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

