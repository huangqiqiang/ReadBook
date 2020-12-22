package com.qq.readbook

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.ArrayMap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.hqq.core.utils.log.LogUtils
import com.qq.readbook.bean.Book
import com.qq.readbook.bean.Chapter
import com.qq.readbook.repository.BookArticleRepository
import kotlinx.coroutines.*

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook
 * @Date : 上午 10:12
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
class DownService : BaseService() {

    private val taskBuilder = TaskBuilder()

    // 协程队列
    val map = ArrayMap<Int, Job>()

    val successMap = ArrayMap<Int, Chapter>()
    override fun onBind(intent: Intent): IBinder {
        var book = intent.getParcelableExtra<Book>("book")
        LogUtils.e("----------onBind")
        taskBuilder.dataList.observe(this@DownService, object : Observer<List<Chapter>> {
            override fun onChanged(t: List<Chapter>) {
                GlobalScope.launch(Dispatchers.Main) {
                    LogUtils.e("onBind: observe   ")
                    for (chapter in t) {
                        LogUtils.e("onBind:   " + chapter.title)
                        this@DownService.doGet(chapter, book)
                    }
                }
            }
        })

        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        return taskBuilder
    }

    suspend fun doGet(chapter: Chapter, book: Book?) {
        withContext(Dispatchers.IO) {
            LogUtils.e("----- 开始请求 : " + chapter.number + "----" + chapter.title)
            BookArticleRepository.getChapterContent(
                chapter, book?.name + "_" + book?.author,
                object : BookArticleRepository.ArticleNetCallBack {
                    override fun onSuccess(boolean: Boolean) {
                        LogUtils.e("收到回调 " + chapter.number + " ------  " + toString())
                        successMap.put(chapter.number, chapter)
                        GlobalScope.launch(Dispatchers.Main) {
                            taskBuilder.onDownloadListener?.onSuccess(
                                boolean,
                                chapter.number,
                                map.size,
                                successMap.size
                            )
                        }
                    }
                })
        }
    }


    open class TaskBuilder : Binder() {
        val dataList = MutableLiveData<List<Chapter>>(ArrayList<Chapter>())
        var onDownloadListener: OnDownloadListener? = null
    }


    open interface OnDownloadListener {
        fun onSuccess(boolean: Boolean, int: Int, totalSize: Int, successSize: Int)
    }


}