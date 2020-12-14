package com.qq.readbook.repository

import android.text.Html
import com.hqq.core.net.ok.OkHttp
import com.hqq.core.net.ok.OkNetCallback
import com.qq.readbook.bean.Chapter
import com.qq.readbook.utils.room.RoomUtils
import org.jsoup.Jsoup

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.repository
 * @Date : 下午 4:34
 * @Email : qiqiang213@gmail.com
 * @Descrive :
 */
object BookArticleRepository {
    /**
     * 获取章节正文
     * @param url
     */
    fun getChapterContent(chapter: Chapter, s: String, param: ArticleNetCallBack) {
        OkHttp.newHttpCompat().get(
            chapter.url, OkHttp.newParamsCompat(), object : OkNetCallback {
                override fun onSuccess(statusCode: String?, response: String?) {
                    var content = getContentFormHtml(response)
                    chapter.content = content;
                    RoomUtils.getChapterDataBase(s).chapterDao().update(chapter)
                }

                override fun onFailure(statusCode: String?, errMsg: String?, response: String?) {

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

    open interface ArticleNetCallBack {

        fun onSuccess();
    }

}