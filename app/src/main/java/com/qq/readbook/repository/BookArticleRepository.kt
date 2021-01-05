package com.qq.readbook.repository

import com.hqq.core.net.ok.OkHttp
import com.hqq.core.net.ok.OkNetCallback
import com.hqq.core.utils.log.LogUtils
import com.qq.readbook.bean.BookContent
import com.qq.readbook.bean.Chapter
import com.qq.readbook.repository.read.TianlaiRead
import com.qq.readbook.utils.room.RoomUtils

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.repository
 * @Date : 下午 4:34
 * @Email : qiqiang213@gmail.com
 * @Describe :  书籍正文
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
                    var content = TianlaiRead.getContentFormHtml(response)
                    if (!content.isNullOrBlank()) {
                        var bookContent = BookContent().apply {
                            number = chapter.number
                            this.content = content
                        }
                        chapter.isCache = true
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


    interface ArticleNetCallBack {

        fun onSuccess(boolean: Boolean);
    }

}