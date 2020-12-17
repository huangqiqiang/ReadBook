package com.qq.readbook.repository

import com.hqq.core.net.ok.OkHttp
import com.hqq.core.net.ok.OkNetCallback
import com.qq.readbook.bean.Book
import com.qq.readbook.bean.Chapter
import com.qq.readbook.utils.room.RoomUtils
import com.qq.readbook.weight.page.StringUtils.isEmpty
import org.jsoup.Jsoup
import java.util.*


/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.repository
 * @Date : 下午 1:45
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
object BookChaptersRepository {
    fun getBookChapters(book: Book) {
        OkHttp.newHttpCompat()[book.chapterUrl, OkHttp.newParamsCompat(), object : OkNetCallback {
            override fun onSuccess(statusCode: String, response: String) {
                val arrayList = getChaptersFromHtml(response, book)

                RoomUtils.getDataBase().run {
                    RoomUtils.getChapterDataBase(book.name + "_" + book.author).chapterDao().apply {
                        deleteAll()
                        resetId()
                        insert(arrayList)
                    }


                }

            }

            override fun onFailure(statusCode: String, errMsg: String, response: String) {}
        }]
    }

    /**
     * 从html中获取章节列表
     *
     * @param html
     * @return
     */
    fun getChaptersFromHtml(html: String?, book: Book): ArrayList<Chapter> {
        val chapters = ArrayList<Chapter>()
        val doc = Jsoup.parse(html)
        val divList = doc.getElementById("list")
        val dl = divList.getElementsByTag("dl")[0]
        var lastTile: String? = null
        var i = 0
        for (dd in dl.getElementsByTag("dd")) {
            val `as` = dd.getElementsByTag("a")
            if (`as`.size > 0) {
                val a = `as`[0]
                val title = a.html()
                if (!isEmpty(lastTile) && title == lastTile) {
                    continue
                }
                val chapter = Chapter()
                chapter.number = i++
                chapter.title = title
                var url = a.attr("href")
                if (isEmpty(book.source) || "天籁小说" == book.source) {
                    url = "https://www.23txt.com/$url"
                } else if ("笔趣阁" == book.source) {
                    url = book.chapterUrl + url
                }
                chapter.bookId = book.bookId
                chapter.url = url
                chapters.add(chapter)
                lastTile = title
            }
        }
        return chapters
    }
}