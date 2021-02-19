package com.qq.readbook.repository

import com.hqq.core.net.ok.OkHttp
import com.hqq.core.net.ok.OkNetCallback
import com.qq.readbook.bean.Book
import com.qq.readbook.repository.read.JsoupUtils

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.repository
 * @Date : 下午 2:38
 * @Email : qiqiang213@gmail.com
 * @Describe : 最新章节
 */
object NewestChapterRepository {
    fun doChapterUrl(book: Book, latestChapter: LatestChapter) {
        OkHttp.newHttpCompat().getExecute(book.chapterUrl, OkHttp.newParamsCompat(), object : OkNetCallback {
            override fun onSuccess(statusCode: String, response: String) {
                    val b = JsoupUtils.getNewChapterFormHtml(response, book)
                    latestChapter.onEnd(b, true)
            }
            override fun onFailure(statusCode: String?, errMsg: String?, response: String?) {
                latestChapter.onEnd(book, false)
            }
        })
    }

    interface LatestChapter {
        fun onEnd(book: Book, isSuccess: Boolean)
    }
}