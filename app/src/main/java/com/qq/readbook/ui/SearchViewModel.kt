package com.qq.readbook.ui

import com.hqq.core.ui.list.BaseListViewModel
import com.qq.readbook.bean.Book
import com.qq.readbook.net.ok.OkNetCallback
import com.qq.readbook.repository.SearchRepository
import org.jsoup.Jsoup
import java.util.*

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.ui
 * @Date : 下午 3:09
 * @Email : qiqiang213@gmail.com
 * @Descrive :
 */
class SearchViewModel : BaseListViewModel() {

    fun onSearch(key: String) {
        SearchRepository().search(key, object : OkNetCallback {
            override fun onSuccess(statusCode: String?, response: String?) {
                response?.let {
                    data.value = getBooksFromSearchHtml(it)

                }


            }

            override fun onFailure(statusCode: String?, errMsg: String?, response: String?) {


            }

        })
    }


    /**
     * 从搜索html中得到书列表
     *
     * @param html
     * @return
     */
    fun getBooksFromSearchHtml(html: String): ArrayList<Book> {
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
            book.setChapterUrl("https://www.23txt.com/" + title.child(0).attr("href"))
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
                    book.setUpdateDate(infoStr.replace("更新时间：", "").replace(" ", ""))
                } else {
                    val newChapter = element1.child(1)
                    book.setNewestChapterUrl(newChapter.attr("href"))
                    book.setNewestChapterTitle(newChapter.text())
                }
            }
            book.setSource("天籁小说")
            books.add(book)
        }
        return books
    }


}