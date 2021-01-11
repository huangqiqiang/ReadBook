package com.qq.readbook.ui

import com.hqq.core.ui.list.BaseListViewModel
import com.hqq.core.utils.log.LogUtils
import com.qq.readbook.bean.Book
import com.qq.readbook.repository.NewestChapterRepository
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

    var updateTime = System.currentTimeMillis()


    override fun onLoadMore() {
        super.onLoadMore()
        var list = RoomUtils.getBook().bookDao().getAll()
        //1分钟
        if (System.currentTimeMillis() - updateTime > 1000 * 60) {
            // 同步更新数据
            val newList = ArrayList<Book>()
            CoroutineScope(Dispatchers.Main).launch {
                for (book in list) {
                    doChapterUrl(book, newList)
                }
                setData(newList)
            }
            updateTime = System.currentTimeMillis()
        } else {
            setData(list)
        }
    }

    private suspend fun doChapterUrl(book: Book, newList: ArrayList<Book>) {
        withContext(Dispatchers.IO) {
            NewestChapterRepository.doChapterUrl(book,
                object : NewestChapterRepository.LatestChapter {
                    override fun onEnd(book: Book, isSuccess: Boolean) {
                        LogUtils.e(book.name + "   " + book.newestChapterTitle)
                        newList.add(book)
                    }
                })

        }
    }

}