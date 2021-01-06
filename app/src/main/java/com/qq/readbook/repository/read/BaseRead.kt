package com.qq.readbook.repository.read

import com.qq.readbook.bean.Book
import com.qq.readbook.bean.Chapter
import com.qq.readbook.weight.page.StringUtils
import org.jsoup.Jsoup
import java.util.ArrayList

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.repository.read
 * @Date  : 上午 10:15
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
object BaseRead {
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
                if (!StringUtils.isEmpty(lastTile) && title == lastTile) {
                    continue
                }
                val chapter = Chapter()
                chapter.number = i++
                chapter.title = title
                var url = a.attr("href")
                if (StringUtils.isEmpty(book.source) || "天籁小说" == book.source) {
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