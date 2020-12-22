package com.qq.readbook.repository

import android.text.Html
import com.hqq.core.net.ok.OkHttp
import com.hqq.core.net.ok.OkNetCallback
import com.hqq.core.utils.log.LogUtils
import com.qq.readbook.bean.BookContent
import com.qq.readbook.bean.Chapter
import com.qq.readbook.utils.room.RoomUtils
import org.jsoup.Jsoup

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.repository
 * @Date : 下午 4:34
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
object BookArticleRepository {
    /**
     * 获取章节正文
     * @param url
     */
    fun getChapterContent(chapter: Chapter, s: String, param: ArticleNetCallBack) {
        OkHttp.newHttpCompat().getExecute(
            chapter.url, OkHttp.newParamsCompat(), object : OkNetCallback {
                override fun onSuccess(statusCode: String?, response: String?) {
                    var content = getContentFormHtml(response)
                    if (!content.isNullOrBlank()) {
                        var bookContent = BookContent().apply {
                            number = chapter.number
                            this.content = content
                        }
                        chapter.isCache=true
                        RoomUtils.getChapterDataBase(s).bookContentDao().insert(bookContent)
                        RoomUtils.getChapterDataBase(s).chapterDao().update(chapter)
                        param.onSuccess(true)
                    } else {
                        LogUtils.d(chapter.title + "   :  " + chapter.url)
                        param.onSuccess(false)
                    }
                }

                override fun onFailure(statusCode: String?, errMsg: String?, response: String?) {
                    param.onSuccess(false)
                }

            }
        )


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

    interface ArticleNetCallBack {

        fun onSuccess(boolean: Boolean);
    }

}