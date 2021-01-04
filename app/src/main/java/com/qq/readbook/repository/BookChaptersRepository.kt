package com.qq.readbook.repository

import com.hqq.core.net.ok.OkHttp
import com.hqq.core.net.ok.OkNetCallback
import com.hqq.core.utils.log.LogUtils
import com.qq.readbook.bean.Book
import com.qq.readbook.bean.Chapter
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
     */
    fun getBookChapters(book: Book, bookChaptersCall: BookChaptersCall?) {
        LogUtils.d("加载目录  :   " + book.chapterUrl)
        OkHttp.newHttpCompat()[book.chapterUrl, OkHttp.newParamsCompat(), object : OkNetCallback {
            override fun onSuccess(statusCode: String, response: String) {
                val arrayList = TianlaiReadUtils.getChaptersFromHtml(response, book)
                bookChaptersCall?.onSuccess(arrayList)
                RoomUtils.getDataBase().run {
                    RoomUtils.getChapterDataBase(book.name + "_" + book.author).chapterDao().apply {
                        // 获取的章节数量不一致 则出现了新的章节
                        if (getAll().size != arrayList.size) {
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