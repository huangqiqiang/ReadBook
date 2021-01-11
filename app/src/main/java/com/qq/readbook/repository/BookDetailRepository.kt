package com.qq.readbook.repository

import com.hqq.core.net.ok.OkHttp
import com.hqq.core.net.ok.OkNetCallback
import com.qq.readbook.bean.Book
import com.qq.readbook.repository.read.TianlaiRead
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
        val doc = Jsoup.parse(html)
        val info = doc.getElementById("info")
        for (child in info.children()) {
            val infoStr = child.text()
            if (infoStr.contains("最后更新：")) {
                book.updateDate = infoStr.replace("最后更新：", "").trim()
            } else if (infoStr.contains("最新章节：")) {
                if (child.childrenSize() > 0) {
                    var c = child.child(0)
                    book.newestChapterTitle = c.text()
                }
            }
        }
        return book;
    }


    interface LatestChapter {
        fun onEnd(book: Book, isSuccess: Boolean)
    }
}