package com.qq.readbook.ui.book

import android.app.Activity
import android.content.*
import android.os.IBinder
import android.view.View
import android.widget.SeekBar
import com.hqq.core.ui.base.BaseVmActivity
import com.hqq.core.utils.DateUtils
import com.hqq.core.utils.ToastUtils
import com.hqq.core.utils.log.LogUtils
import com.qq.readbook.BR
import com.qq.readbook.DownService
import com.qq.readbook.Keys
import com.qq.readbook.R
import com.qq.readbook.bean.Book
import com.qq.readbook.bean.Chapter
import com.qq.readbook.databinding.ActivityReadBookBinding
import com.qq.readbook.repository.BookChaptersRepository
import com.qq.readbook.utils.room.RoomUtils
import com.qq.readbook.weight.page.BrightnessUtils
import com.qq.readbook.weight.page.PageView
import com.qq.readbook.weight.page.ReadSettingManager
import com.qq.readbook.weight.page.loader.OnPageChangeListener
import com.qq.readbook.weight.page.loader.PageLoader
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.ui.book
 * @Date  : 下午 2:25
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
class ReadBookActivity : BaseVmActivity<ReadBookViewModel, ActivityReadBookBinding>() {
    companion object {
        fun open(context: Activity, item: Book) {
            context.startActivityForResult(
                Intent(context, ReadBookActivity::class.java)
                    .putExtra(Keys.BOOK, item), -1
            )
        }
    }

    override val layoutId: Int = R.layout.activity_read_book
    override val bindingViewModelId: Int = BR.vm

    override fun initConfig() {
        super.initConfig()
        rootViewImpl.iToolBarBuilder.showToolBar = false
    }

    lateinit var pageLoader: PageLoader

    // 接收电池信息和时间更新的广播
    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (Objects.requireNonNull(intent.action) == Intent.ACTION_BATTERY_CHANGED) {
                val level = intent.getIntExtra("level", 0)
                pageLoader.updateBattery(level)
            } else if (intent.action == Intent.ACTION_TIME_TICK) {
                pageLoader.updateTime()
            }// 监听分钟的变化
        }
    }
    private val service = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            taskBuilder = service as DownService.TaskBuilder
            if (taskBuilder?.onDownloadListener == null) {
                taskBuilder?.onDownloadListener = object : DownService.OnDownloadListener {
                    override fun onSuccess(
                            boolean: Boolean,
                            int: Int,
                            totalSize: Int,
                            successSize: Int
                    ) {
                        LogUtils.e(" ----- 收到 service 回调  当前position: " + int + "      ---   总数量" + totalSize + "         已完成" + successSize)
                        if (int == pageLoader.chapterPos) {
                            hintMenu()
                            if (boolean) {
                                pageLoader.openChapter()
                            } else {
                                pageLoader.chapterError()
                            }
                        }
                    }
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }

    }
    var taskBuilder: DownService.TaskBuilder? = null;
    lateinit var book: Book

    override fun initViews() {
        book = intent.getParcelableExtra<Book>(Keys.BOOK)!!
        initService(book)
        binding.tvBarTitle.text = book.name
        pageLoader = binding.pageView.getPageLoader(book)
        pageLoader.refreshChapterList()
        pageLoader.setOnPageChangeListener(object :
            OnPageChangeListener {
            override fun onChapterChange(pos: Int) {
                LogUtils.d("onChapterChange  :    " + pos)
            }

            override fun requestChapters(requestChapters: MutableList<Chapter>?) {
                // 理论上需要用队列去维护 避免重复请求
                taskBuilder?.dataList?.value = (requestChapters)
            }

            override fun onCategoryFinish(chapters: MutableList<Chapter>?) {
                LogUtils.e("onCategoryFinish")
            }

            override fun onPageCountChange(count: Int) {
                LogUtils.e("onPageCountChange : " + count)
            }

            override fun onPageChange(pos: Int) {
                LogUtils.e("onPageChange  :  " + pos)
            }
        })
        loadingView.show()
        GlobalScope.launch(Dispatchers.IO) {
            // 需要异步加载
            var charpters =
                book?.sourceName?.let {
                    RoomUtils.getChapterDataBase(book.name + "_" + book.author).chapterDao().getAll(it)
                }
            launch(Dispatchers.Main) {
                if (charpters.isNullOrEmpty()) {
                    // 数据库中没有查询到章节数据
                    if (book != null) {
                        BookDetailActivity.open(activity, book)
                    }
                    finish()
                } else {
                    book.bookChapterList = charpters
                    pageLoader.refreshChapterList()
                }
                loadingView.dismiss()
            }

        }

        initClick(book)
        //注册广播
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED)
        intentFilter.addAction(Intent.ACTION_TIME_TICK)
        registerReceiver(mReceiver, intentFilter)
    }

    private fun initService(book: Book?) {
        bindService(Intent(this, DownService::class.java).apply {
            putExtra(Keys.BOOK, book)
        }, service, BIND_AUTO_CREATE)
    }

    private fun initClick(book: Book?) {
        binding.pageView.setTouchListener(object : PageView.TouchListener {
            override fun onTouch(): Boolean {
                // 隐藏按钮
                return hintMenu()
            }

            override fun center() {
                if (binding.flLayout.visibility == View.GONE) {
                    binding.llBottomMenu.visibility = View.VISIBLE
                    binding.flLayout.visibility = View.VISIBLE
                } else {
                    binding.flLayout.visibility = View.GONE
                    binding.llBottomMenu.visibility = View.GONE
                }
            }

            override fun prePage() {}
            override fun nextPage() {}
            override fun cancel() {}
        })
        binding.sbBrightness.progress = ReadSettingManager.getInstance().brightness
        binding.sbBrightness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                val progress = seekBar.progress
                //设置当前 Activity 的亮度
                BrightnessUtils.setBrightness(activity, progress)
                //存储亮度的进度条
                ReadSettingManager.getInstance().brightness = progress
            }

        })
        binding.tvSetting.setOnClickListener {
            // 设置
            SettingDialog(pageLoader).show(supportFragmentManager)
            binding.llBottomMenu.visibility = View.GONE
        }
        binding.tvCategory.setOnClickListener {
            // 目录
            ChaptersDialog(pageLoader, book?.bookChapterList).show(supportFragmentManager)
        }

        binding.tvCache50.setOnClickListener {
            doCache(book, 50)
        }
        binding.tvCache100.setOnClickListener {
            doCache(book, 100)
        }
        binding.tvCacheAll.setOnClickListener {
            doCache(book, 9999999)

        }
        binding.flLayout.setOnClickListener {}
        binding.tvRight.setOnClickListener {
            BookSourceActivity.open(activity, book)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val sourceName = data?.getStringExtra(Keys.BOOK_SOURCE_NAME)
            sourceName?.let {
                book.sourceName = sourceName
                val bookSources = RoomUtils.getBook().bookSources().getBookSource(it, book.bookId)
                book.chapterUrl= bookSources?.bookChapterUrl
                // 更新信息
                readChapters(book)
            }
        }
    }


    /**
     * 执行缓存
     */
    private fun doCache(book: Book?, i: Int) {
        taskBuilder?.dataList?.value = (
                getCacheList(book, i)
                )
        hintMenu()
    }

    /**
     *  获取需要缓存的章节
     */
    private fun getCacheList(book: Book?, size: Int): MutableList<Chapter>? {
        val newChapterList = ArrayList<Chapter>()
        book?.bookChapterList?.let {
            val position = pageLoader.chapterPos
            val start = if (position < it.size) position else {
                ToastUtils.showToast("全部已缓存完毕")
                return@let
            }
            val end = if ((position + size) < it.size) {
                (position + size)
            } else it.size
            // 截取部分集合
            val list = it.subList(start, end)
            LogUtils.e("---" + list.size)
            for (chapter in list) {
                if (!chapter.isCache) {
                    newChapterList.add(chapter)
                }
            }
            return newChapterList
        }
        return newChapterList;
    }

    /**
     *  隐藏按钮
     */
    private fun hintMenu(): Boolean {
        if (binding.flLayout.visibility == View.VISIBLE) {
            binding.llLight.visibility = View.GONE
            binding.llBottomMenu.visibility = View.GONE
            binding.flLayout.visibility = View.GONE
            binding.llCache.visibility = View.GONE
            return false
        } else {
            binding.llCache.visibility = View.GONE
            binding.llLight.visibility = View.GONE
            binding.llBottomMenu.visibility = View.GONE
            return true
        }
    }

    override fun onPause() {
        super.onPause()
        pageLoader.saveRecord()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(service)
        intent.getParcelableExtra<Book>(Keys.BOOK)?.apply {
            lastRead = DateUtils.nowDate
            RoomUtils.getBook().bookDao().update(this)

        }
        pageLoader.closeBook()
        unregisterReceiver(mReceiver)
    }

    /**
     *  获取章节信息
     * @param it Book
     */
    private fun readChapters(it: Book) {
        // 爬取新目录
        loadingView.show()
        BookChaptersRepository.getBookChapters(it, object : BookChaptersRepository.BookChaptersCall {
            override fun onSuccess(arrayList: List<Chapter>) {
                book.bookChapterList = arrayList
                loadingView.dismiss()

            }
        })
    }
}