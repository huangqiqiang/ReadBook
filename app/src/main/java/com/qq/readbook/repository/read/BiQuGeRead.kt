package com.qq.readbook.repository.read

import com.qq.readbook.bean.Book
import com.qq.readbook.bean.BookSources
import com.qq.readbook.bean.ReadSource
import com.qq.readbook.bean.Chapter
import com.qq.readbook.repository.read.face.Read
import com.qq.readbook.utils.MD5Utils
import com.qq.readbook.utils.room.RoomUtils
import org.jsoup.Jsoup
import java.util.ArrayList

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.repository
 * @Date : 上午 10:45
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
class BiQuGeRead  : Read{
    override fun readSearch(html: String, source: ReadSource): ArrayList<Book> {
        val books: ArrayList<Book> = ArrayList<Book>()
        val doc = Jsoup.parse(html)
        var lis = doc.getElementById("main").getElementsByTag("li")
        for (li in lis) {
            var book = Book()
            if (li.childrenSize() == 6) {
                for (child in li.children()) {
                    when (child.className()) {
                        "s1" -> {
                            book.type = child.text()
                        }
                        "s2" -> {
                            book.chapterUrl = child.children().attr("href");
                            book.name = child.text()
                        }
                        "s3" -> {
                            book.newestChapterTitle = child.text()
                        }
                        "s4" -> {
                            book.author = child.text()
                        }
                        "s5" -> {
                            book.updateDate = child.text()
                        }
                    }
                }
            }
            book.imgUrl = ""
            book.setSourceName(source.bookSourceName)
            book.setBookId(MD5Utils.getStringMD5(book.name + book.author))
            books.add(book);
            val bookSources = BookSources()
            bookSources.bookId = book.bookId
            bookSources.sourcesName = book.sourceName
            bookSources.bookDetailUrl = book.chapterUrl
            RoomUtils.getBook().bookSources().insertAll(bookSources);
        }
        return books;
    }

    override fun readChapters(html: String?, book: Book, source: ReadSource): ArrayList<Chapter> {
        return BaseRead.getChaptersFromHtml(html,book)
    }

    override fun readBookDetail() {

    }

    override fun readNewestChapter() {
    }




}