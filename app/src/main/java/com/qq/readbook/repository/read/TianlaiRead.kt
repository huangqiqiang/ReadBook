package com.qq.readbook.repository.read

import android.text.Html
import com.qq.readbook.bean.Book
import com.qq.readbook.bean.Chapter
import com.qq.readbook.bean.BookSource
import com.qq.readbook.repository.read.face.Read
import com.qq.readbook.utils.MD5Utils
import com.qq.readbook.weight.page.StringUtils
import org.jsoup.Jsoup
import java.util.ArrayList

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.repository
 * @Date  : 上午 10:40
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
class TianlaiRead : Read {

    /**
     * 获取最新章节
     *
     * @param html
     * @return
     */
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


    /**
     * 从html中获取章节正文
     *
     * @param html
     * @return
     */
    fun getContentFormHtml(html: String?): String? {
        val doc = Jsoup.parse(html)
        val divContent = doc.getElementById("content")
        return if (divContent != null) {
            var content = Html.fromHtml(divContent.html()).toString()
            val c = 160.toChar()
            val spaec = "" + c
            content = content.replace(spaec, "  ")
            content
        } else {
            ""
        }
    }

    override fun readSearch(html: String, source: BookSource): ArrayList<Book> {
        val books: ArrayList<Book> = ArrayList<Book>()
        val doc = Jsoup.parse(html)
        val divs = doc.getElementsByClass("result-list")
        val div = divs[0]
        for (element in div.children()) {
            val book = Book()
            val img = element.child(0).child(0).child(0)
            book.setImgUrl(img.attr("src"))
            val title = element.getElementsByClass("result-item-title result-game-item-title")[0]
            book.setName(title.child(0).attr("title"))
            book.setChapterUrl(source.bookSourceUrl + title.child(0).attr("href"))
            val desc = element.getElementsByClass("result-game-item-desc")[0]
            book.setDesc(desc.text())
            val info = element.getElementsByClass("result-game-item-info")[0]
            for (element1 in info.children()) {
                val infoStr = element1.text()
                if (infoStr.contains("作者：")) {
                    book.setAuthor(infoStr.replace("作者：", "").replace(" ", ""))
                } else if (infoStr.contains("类型：")) {
                    book.setType(infoStr.replace("类型：", "").replace(" ", ""))
                } else if (infoStr.contains("更新时间：")) {
                    book.setUpdateDate(infoStr.replace("更新时间：", "").trim())
                } else {
                    val newChapter = element1.child(1)
                    book.setNewestChapterUrl(newChapter.attr("href"))
                    book.setNewestChapterTitle(newChapter.text())
                }
            }
            book.setSource(source.bookSourceName)
            book.setBookId(MD5Utils.getStringMD5(book.name + book.author))
            books.add(book)

        }
        return books
    }

    override fun readChapters(html: String?, book: Book,source: BookSource): ArrayList<Chapter> {
        return BaseRead.getChaptersFromHtml(html,book)
    }

    override fun readBookDetail() {
    }

    override fun readNewestChapter() {
    }


}