package com.qq.readbook.repository

import com.hqq.core.net.ok.OkHttp
import com.hqq.core.net.ok.OkNetCallback
import com.hqq.core.utils.log.LogUtils
import com.qq.readbook.BookSourceUtils
import com.qq.readbook.bean.Book
import org.jsoup.Jsoup

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.repository
 * @Date : 下午 4:23
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
object BookDetailRepository {
    fun doChapterUrl(book: Book, latestChapter: LatestChapter) {
        LogUtils.e("最新章节:  " + book.chapterUrl)
        OkHttp.newHttpCompat()
            .getExecute(book.chapterUrl, OkHttp.newParamsCompat(), object : OkNetCallback {
                override fun onSuccess(statusCode: String, response: String) {
                    val b = getNewChapterFormHtml(response, book)
                    latestChapter.onEnd(b, true)
                }

                override fun onFailure(statusCode: String?, errMsg: String?, response: String?) {
                    latestChapter.onEnd(book, false)
                }
            })

    }

    fun getNewChapterFormHtml(html: String?, book: Book): Book {
        LogUtils.e("-----------------------")
        LogUtils.e("解析最新章节")
        LogUtils.e(book.name)
        LogUtils.e(book.sourceName)
        val doc = Jsoup.parse(html)
        var source = BookSourceUtils.getInstance().sourceList?.first {
            it.bookSourceName == book.sourceName
        }
        book.newestChapterTitle =
            JsoupUtils.getElementValue(doc, source?.newestChapter?.let { it })

//        for (elementsByClass in doc.getElementsByClass("headline")) {
//            if (elementsByClass.text().contains("更新：")) {
//                book.updateDate = elementsByClass.text().replace("更新：", "").trim()
//            }
//        }
        LogUtils.e("解析最新章节结束")
        LogUtils.e("-----------------------")

        return book;
    }


    interface LatestChapter {
        fun onEnd(book: Book, isSuccess: Boolean)
    }
}