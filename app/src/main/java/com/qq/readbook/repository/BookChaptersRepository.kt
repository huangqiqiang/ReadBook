package com.qq.readbook.repository

import com.hqq.core.net.ok.OkHttp
import com.hqq.core.net.ok.OkNetCallback
import com.hqq.core.utils.log.LogUtils
import com.qq.readbook.BookSourceUtils
import com.qq.readbook.bean.Book
import com.qq.readbook.bean.Chapter
import com.qq.readbook.repository.read.JsoupUtils
import com.qq.readbook.utils.room.RoomUtils


/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.repository
 * @Date : 下午 1:45
 * @Email : qiqiang213@gmail.com
 * @Describe : 数据目录
 */
object BookChaptersRepository {
    /**
     *  读取数据量 目录列表
     *  根据源名称 查询对应的url
     *  爬取目录列表
     */
    fun getBookChapters(book: Book, bookChaptersCall: BookChaptersCall?) {
        // 解析规则
        val source = BookSourceUtils.getInstance().sourceList?.find {
            it.bookSourceName == book.sourceName
        }
        LogUtils.d(source?.bookSourceName + "     加载目录  :   " + book.chapterUrl)
        OkHttp.newHttpCompat()[book.chapterUrl, OkHttp.newParamsCompat(), object : OkNetCallback {
            override fun onSuccess(statusCode: String, response: String) {
                LogUtils.d("-----目录-------")
//                LogUtils.d(response.replace(Regex("\r|\n\t"), ""))
                LogUtils.d("-----结束-------")
                val arrayList = JsoupUtils.readChapter(response, source, book)
                bookChaptersCall?.onSuccess(arrayList)
                RoomUtils.getBook().run {
                    RoomUtils.getChapterDataBase(book.name + "_" + book.author).chapterDao().apply {
                        // 获取的章节数量不一致 则出现了新的章节
                        if (arrayList.isNotEmpty()) {
                            deleteAll()
                            resetId()
                            insert(arrayList)
                        }
                    }
                }
            }

            override fun onFailure(statusCode: String, errMsg: String, response: String) {}
        }]
    }


    interface BookChaptersCall {
        fun onSuccess(arrayList: List<Chapter>);
    }

}