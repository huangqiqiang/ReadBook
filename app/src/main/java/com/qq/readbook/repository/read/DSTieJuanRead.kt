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
 * @Package : com.qq.readbook.repository.read
 * @Date  : 下午 4:54
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
class DSTieJuanRead : Read {
    override fun readSearch(html: String, source: ReadSource): ArrayList<Book> {
        val books: ArrayList<Book> = ArrayList<Book>()
        val doc = Jsoup.parse(html)
        doc.getElementsByClass("library").first()?.let {
            for (element in it.children()) {
                val book = Book()
                element.getElementsByClass("bookimg").first()?.let {
                    book.chapterUrl =
                        source.bookSourceUrl + it.attr("href").replace(".html", "")
                    book.imgUrl = it.getElementsByTag("img").find {
                        it.hasAttr("src")
                    }?.attr("src")
                }
                book.name = element.getElementsByClass("bookname").first()?.text()
                book.desc = element.getElementsByClass("intro").first()?.text()
                book.newestChapterTitle = element.getElementsByClass("chapter").first()?.text()
                book.author = element.getElementsByClass("author").first()?.text()
                book.sourceName = source.bookSourceName
                book.bookId = MD5Utils.getStringMD5(book.name + book.author)
                books.add(book)

                val bookSources = BookSources()
                bookSources.bookId = book.bookId
                bookSources.sourcesName = book.sourceName
                bookSources.bookDetailUrl = book.chapterUrl
                RoomUtils.getBook().bookSources().insertAll(bookSources);
            }
        }
        return books
    }

    override fun readChapters(html: String?, book: Book, source: ReadSource): ArrayList<Chapter> {
        val chapters = ArrayList<Chapter>()
        val doc = Jsoup.parse(html)
        var list = doc.getElementsByClass("read").firstOrNull()?.getElementsByTag("dl")?.get(1)
        if (list != null) {
            for ((index, child) in list.children().withIndex()) {
                val chapter = Chapter()
                child.getElementsByTag("a").apply {
                    chapter.title = text();
                    chapter.url = source.bookSourceUrl + attr("href")
                }
                chapter.number = index
                chapter.bookId = book.bookId
                chapters.add(chapter)
            }
        }
        return chapters
    }

    override fun readBookDetail() {

    }


    override fun readNewestChapter() {
    }


}