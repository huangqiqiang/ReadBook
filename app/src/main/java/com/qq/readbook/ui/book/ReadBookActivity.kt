package com.qq.readbook.ui.book

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.SeekBar
import com.hqq.core.ui.base.BaseVmActivity
import com.hqq.core.utils.log.LogUtils
import com.qq.readbook.BR
import com.qq.readbook.R
import com.qq.readbook.bean.Book
import com.qq.readbook.bean.Chapter
import com.qq.readbook.databinding.ActivityReadBookBinding
import com.qq.readbook.repository.BookArticleRepository
import com.qq.readbook.utils.room.RoomUtils
import com.qq.readbook.weight.page.BrightnessUtils
import com.qq.readbook.weight.page.PageView
import com.qq.readbook.weight.page.ReadSettingManager
import com.qq.readbook.weight.page.loader.OnPageChangeListener
import com.qq.readbook.weight.page.loader.PageLoader

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
                    .putExtra("book", item), -1
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
    override fun initViews() {

        var book = intent.getParcelableExtra<Book>("book")
        // 需要异步加载
        var charpters =
            RoomUtils.getChapterDataBase(book!!.name + "_" + book.author).chapterDao().getAll()
        book.bookChapterList = (charpters)
        pageLoader = binding.pageView.getPageLoader(book)
        binding.pageView.post {
            pageLoader.refreshChapterList()
        }

        pageLoader.setOnPageChangeListener(object :
            OnPageChangeListener {
            override fun onChapterChange(pos: Int) {
                LogUtils.e("onChapterChange" + pos)
            }

            override fun requestChapters(requestChapters: MutableList<Chapter>?) {
                LogUtils.e("requestChapters")
                // 理论上需要用队列去维护 避免重复请求
                requestChapters?.get(0)?.let {
                    BookArticleRepository.getChapterContent(
                        it, book.name + "_" + book.author,
                        object : BookArticleRepository.ArticleNetCallBack {
                            override fun onSuccess() {
//                                pageLoader.skipToChapter(pageLoader.chapterPos)
                                pageLoader.openChapter()
                            }

                        })
                }

            }

            override fun onCategoryFinish(chapters: MutableList<Chapter>?) {
                LogUtils.e("onCategoryFinish")
            }

            override fun onPageCountChange(count: Int) {
                LogUtils.e("onPageCountChange")
            }

            override fun onPageChange(pos: Int) {
                LogUtils.e("onPageChange")
            }

        })



        binding.pageView.setTouchListener(object : PageView.TouchListener {
            override fun onTouch(): Boolean {
                // 隐藏按钮
                hintMenu();
                return true
            }

            override fun center() {
                if (binding.llBottomMenu.visibility == View.VISIBLE) {
                    binding.llBottomMenu.visibility = View.GONE
                } else {
                    binding.llBottomMenu.visibility = View.VISIBLE
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
        binding.llBottomMenu.setOnClickListener {
            // 避免点击透传
        }
        binding.tvCategory.setOnClickListener {
            // 目录
            ChaptersDialog(pageLoader, book.bookChapterList).show(supportFragmentManager)
        }
    }

    private fun hintMenu() {
        binding.llLight.visibility = View.GONE
        binding.llBottomMenu.visibility = View.GONE
    }

}