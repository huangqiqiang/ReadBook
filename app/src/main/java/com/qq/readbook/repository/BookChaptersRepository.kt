package com.qq.readbook.repository

import android.content.ContentValues
import androidx.room.OnConflictStrategy
import com.hqq.core.CoreConfig
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
 * @Descrive :
 */
object BookChaptersRepository {
    fun getBookChapters(book: Book) {
        OkHttp.newHttpCompat()[book.chapterUrl, OkHttp.newParamsCompat(), object : OkNetCallback {
            override fun onSuccess(statusCode: String, response: String) {
                val arrayList = getChaptersFromHtml(response, book)

                RoomUtils.getDataBase(
                    CoreConfig.get().application!!
                ).run {

                    // 创建表 并插入数据
//                    var db = getOpenHelper().getWritableDatabase()
//                    getOpenHelper().writableDatabase.execSQL("delete  FROM Chapter")
//                    db.execSQL(Chapter.getCreateTableName("Chapter_" + book.name + "_" + book.author))
//                    for (chapter in arrayList) {
//                        var value = ContentValues()
//                        value.put("bookId", chapter.bookId)
//                        value.put("number", chapter.number)
//                        value.put("title", chapter.title)
//                        value.put("url", chapter.url)
//                        value.put("content", chapter.content)
//                        getOpenHelper().writableDatabase.insert(
//                            "Chapter_" + book.name + "_" + book.author,
//                            OnConflictStrategy.IGNORE, value
//                        )
//                    }

                    RoomUtils.getChapterDataBase(book.name + "_" + book.author).chapterDao().apply {
                        deleteAll()
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
                chapter.url = url
                chapters.add(chapter)
                lastTile = title
            }
        }
        return chapters
    }
}