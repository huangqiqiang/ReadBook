package com.qq.readbook.ui

import android.os.Bundle
import com.hqq.core.CoreConfig
import com.hqq.core.ui.list.BaseListViewModel
import com.qq.readbook.bean.Book
import com.qq.readbook.repository.LatestChapterRepository
import com.qq.readbook.utils.room.RoomUtils
import kotlinx.coroutines.*

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.ui
 * @Date : 上午 11:23
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
class MainViewModel : BaseListViewModel() {


    override fun onResume() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(500)
            onLoadMore()
        }
    }

    override fun onLoadMore() {
        super.onLoadMore()
        var list = RoomUtils.getDataBase().bookDao().getAll()
        setData(list)
        CoroutineScope(Dispatchers.IO).launch {
            for (book in list) {
                doChapterUrl(book)
            }
        }
    }

    private suspend fun doChapterUrl(book: Book) {
        withContext(Dispatchers.IO) {
            LatestChapterRepository.doChapterUrl(book,
                object : LatestChapterRepository.LatestChapter {
                    override fun onEnd(book: Book, isSuccess: Boolean) {

                    }
                })

        }
    }

}