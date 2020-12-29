package com.qq.readbook.repository

import com.hqq.core.net.ok.OkHttp
import com.hqq.core.net.ok.OkNetCallback
import com.qq.readbook.bean.Book
import org.jsoup.Jsoup

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.repository
 * @Date : 下午 2:38
 * @Email : qiqiang213@gmail.com
 * @Describe : 最新章节
 */
object LatestChapterRepository {
    fun doChapterUrl(book: Book, latestChapter: LatestChapter) {
        OkHttp.newHttpCompat()[book.chapterUrl, OkHttp.newParamsCompat(), object : OkNetCallback {
            override fun onSuccess(statusCode: String, response: String) {
                var b = getChaptersFromHtml(response, book)
                latestChapter.onEnd(b, true)

            }

            override fun onFailure(statusCode: String?, errMsg: String?, response: String?) {
                latestChapter.onEnd(book, false)
            }
        }]

    }

    /**
     * 从html中获取章节列表
     *
     * @param html
     * @return
     */
    fun getChaptersFromHtml(html: String?, book: Book): Book {
        val doc = Jsoup.parse(html)
        val info = doc.getElementById("info")
        for (child in info.children()) {
            val infoStr = child.text()
            if (infoStr.contains("最后时间：")) {
                book.updateDate = infoStr.replace("最后时间：", "").trim()
            } else if (infoStr.contains("最新章节：")) {
                if (child.childrenSize() > 1) {
                    var c = child.child(1)
                    book.newestChapterTitle = c.text()
                }
            }
        }
        return book;
    }

    open interface LatestChapter {
        fun onEnd(book: Book, isSuccess: Boolean)
    }
}