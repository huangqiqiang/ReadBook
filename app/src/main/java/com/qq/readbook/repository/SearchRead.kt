package com.qq.readbook.repository

import com.qq.readbook.bean.Book
import com.qq.readbook.bean.BookSource
import com.qq.readbook.utils.MD5Utils
import org.jsoup.Jsoup
import java.util.ArrayList

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.repository
 * @Date  : 下午 3:27
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
object SearchRead {

    /**
     *  笔趣阁的解析
     */
    @JvmStatic
    fun searchFormatBiQuGe(html: String, source: BookSource): ArrayList<Book> {
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
            book.imgUrl=""
            book.setSource(source.bookSourceName)
            book.setBookId(MD5Utils.getStringMD5(book.name + book.author))
            books.add(book);
        }
        return books;
    }

    /**
     * 从搜索html中得到书列表
     *
     * @param html
     * @return
     */
    @JvmStatic
    fun searchFormatTianLai(html: String, source: BookSource): ArrayList<Book> {
        val books: ArrayList<Book> = ArrayList<Book>()
        val doc = Jsoup.parse(html)
        //        Element node = doc.getElementById("results");
//        for (Element div : node.children()) {
        val divs = doc.getElementsByClass("result-list")
        val div = divs[0]
        //        if (!StringHelper.isEmpty(div.className()) && div.className().equals("result-list")) {
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


}