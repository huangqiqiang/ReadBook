package com.qq.readbook.repository

import com.hqq.core.net.ok.OkHttp
import com.hqq.core.net.ok.OkNetCallback
import com.hqq.core.utils.log.LogUtils
import com.qq.readbook.bean.Book
import com.qq.readbook.repository.read.JsoupUtils

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.repository
 * @Date : 下午 4:23
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
object BookDetailRepository {
    fun doChapterUrl(book: Book, latestChapter: ILatestChapter) {
        LogUtils.e("最新章节:  " + book.chapterUrl)
        OkHttp.newHttpCompat()
            .getExecute(book.bookDetailUrl, OkHttp.newParamsCompat(), object : OkNetCallback {
                override fun onSuccess(statusCode: String, response: String) {
                    val b = JsoupUtils.getNewChapterFormHtml(response, book)
                    latestChapter.onEndCall(b, true)
                }

                override fun onFailure(statusCode: String?, errMsg: String?, response: String?) {
                    latestChapter.onEndCall(book, false)
                }
            })

    }



    interface ILatestChapter {
        fun onEndCall(book: Book, isSuccess: Boolean)
    }
}