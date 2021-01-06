package com.qq.readbook.repository.read

import com.qq.readbook.bean.Book
import com.qq.readbook.bean.BookSource
import com.qq.readbook.bean.Chapter
import com.qq.readbook.repository.read.face.Read
import com.qq.readbook.utils.MD5Utils
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
    override fun readSearch(html: String, source: BookSource): ArrayList<Book> {
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
                            book.newestChapterUrl = child.children().attr("href");
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
            book.setSource(source.bookSourceName)
            book.setBookId(MD5Utils.getStringMD5(book.name + book.author))
            books.add(book);
        }
        return books;
    }

    override fun readChapters(html: String?, book: Book, source: BookSource): ArrayList<Chapter> {
        return BaseRead.getChaptersFromHtml(html,book)
    }

    override fun readBookDetail() {

    }

    override fun readNewestChapter() {
    }




}