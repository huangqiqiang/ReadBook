package com.qq.readbook.repository.read

import com.qq.readbook.bean.Book
import com.qq.readbook.bean.BookSource
import com.qq.readbook.repository.read.face.Read
import com.qq.readbook.utils.MD5Utils
import org.jsoup.Jsoup
import java.util.ArrayList

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.repository.read
 * @Date  : 下午 4:54
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
object DSTieJuan : Read {
    override fun readSearch(html: String, source: BookSource): ArrayList<Book> {
        val books: ArrayList<Book> = ArrayList<Book>()
        val doc = Jsoup.parse(html)
        val lib = doc.getElementsByClass("library")
        if (lib.isNotEmpty()) {
            for (element in lib[0].children()) {
                val book = Book()
                for (div in element.children()) {
                    when (div.className()) {
                        "bookimg" -> {
                            book.chapterUrl =
                                source.bookSourceUrl + div.attr("href").replace(".html", "")
                            book.imgUrl = div.getElementsByTag("img").find {
                                it.hasAttr("src")
                            }?.attr("src")
                        }
                        "bookname" -> {
                            book.name = div.text()
                        }
                        "author" -> {
                            book.author = div.text()
                        }
                        "intro" -> {
                            book.desc = div.text()
                        }
                        "chapter" -> {
                            book.newestChapterTitle = div.text()
                        }
                    }
                }
                book.source = source.bookSourceName
                book.bookId = MD5Utils.getStringMD5(book.name + book.author)
                books.add(book)
            }
        }
        return books
    }

    override fun readBookDetail() {
    }

    override fun readNewestChapter() {
    }

    override fun readChapters() {
    }
}