package com.qq.readbook.repository

import com.hqq.core.net.ok.OkHttp
import com.hqq.core.net.ok.OkNetCallback
import com.hqq.core.utils.log.LogUtils
import com.qq.readbook.BookSourceUtils
import com.qq.readbook.bean.BookContent
import com.qq.readbook.bean.Chapter
import com.qq.readbook.repository.read.JsoupUtils
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
    fun getChapterContent(chapter: Chapter, bookName: String, param: ArticleNetCallBack) {
        try {
            OkHttp.newHttpCompat().getExecute(
                chapter.url, OkHttp.newParamsCompat(), object : OkNetCallback {
                    override fun onSuccess(statusCode: String?, response: String?) {
                        val source = BookSourceUtils.getInstance().sourceList?.first {
                            it.bookSourceName == chapter.sources
                        }
                        val content = JsoupUtils.getArticleDetail(response, source)
                        if (!content.isNullOrBlank()) {
                            val bookContent = BookContent().apply {
                                number = chapter.number
                                this.content = content
                            }
                            chapter.isCache = true
                            RoomUtils.getChapterDataBase(bookName).bookContentDao().insert(bookContent)
                            RoomUtils.getChapterDataBase(bookName).chapterDao().update(chapter)
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

        } catch (e: Exception) {
            if (e.message == "Expected URL scheme 'http' or 'https' but no colon was found") {
                LogUtils.e("Expected URL scheme 'http' or 'https' but no colon was found")
                LogUtils.e("删除当前对应的资源")
                param.onSuccess(false)
                RoomUtils.getBook().run {
                    RoomUtils.getChapterDataBase(bookName).chapterDao().apply {
                        // 删除所有的章节
                        deleteAll()
                    }
                }
            }
        }
    }
    interface ArticleNetCallBack {
        fun onSuccess(boolean: Boolean);
    }

}