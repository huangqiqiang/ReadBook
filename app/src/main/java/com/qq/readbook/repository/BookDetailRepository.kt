package com.qq.readbook.repository

import com.hqq.core.net.ok.OkHttp
import com.hqq.core.net.ok.OkNetCallback
import com.hqq.core.utils.log.LogUtils
import com.qq.readbook.bean.Book
import com.qq.readbook.bean.ReadSource
import com.qq.readbook.repository.read.JsoupUtils
import com.qq.readbook.utils.room.RoomUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.repository
 * @Date : 下午 4:23
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
object BookDetailRepository {
    fun readBookDetail(book: Book, readSource: ReadSource, latestChapter: ILatestChapter) {
        CoroutineScope(Dispatchers.IO).launch {
            LogUtils.e("详情页面:  " + book.bookDetailUrl)
            OkHttp.newHttpCompat()
                .getExecute(book.bookDetailUrl, OkHttp.newParamsCompat(), object : OkNetCallback {
                    override fun onSuccess(statusCode: String, response: String) {
                        val b = JsoupUtils.getBookDetail(response, book, readSource)
                        b.refreshTime=System.currentTimeMillis()
                        // 更新书籍
                        RoomUtils.getBook().bookDao().update(b)
                        CoroutineScope(Dispatchers.Main).launch {
                            latestChapter.onEndCall(b, true)
                        }
                    }
                    override fun onFailure(statusCode: String?, errMsg: String?, response: String?) {
                        latestChapter.onEndCall(book, false)
                    }
                })
        }
    }

    interface ILatestChapter {
        fun onEndCall(book: Book, isSuccess: Boolean)
    }
}