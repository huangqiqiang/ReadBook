package com.qq.readbook.ui.book

import android.app.Activity
import android.content.Intent
import com.hqq.core.ui.base.BaseVmActivity
import com.hqq.core.utils.GsonUtil
import com.hqq.core.utils.log.LogUtils
import com.qq.readbook.BR
import com.qq.readbook.R
import com.qq.readbook.bean.Book
import com.qq.readbook.bean.Chapter
import com.qq.readbook.databinding.ActivityReadBookBinding
import com.qq.readbook.repository.BookArticleRepository
import com.qq.readbook.utils.room.RoomUtils
import com.qq.readbook.weight.page.CollBookBean
import com.qq.readbook.weight.page.loader.OnPageChangeListener
import com.qq.readbook.weight.page.PageView

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.ui.book
 * @Date  : 下午 2:25
 * @Email : qiqiang213@gmail.com
 * @Descrive :
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


    override fun initViews() {


        var mCollBook = CollBookBean()
        mCollBook.bookId = "sdfsdfsdfsdfsdfsd"
        var book = intent.getParcelableExtra<Book>("book")
        mCollBook?.author = book?.author
        mCollBook?.title = book?.name
        var charpters =
            RoomUtils.getChapterDataBase(book!!.name + "_" + book.author).chapterDao().getAll()
        mCollBook?.bookChapterList = (charpters)

        var loader = binding.pageView.getPageLoader(mCollBook)

        binding.pageView.post {
            loader.refreshChapterList()
        }

        loader.setOnPageChangeListener(object :
            OnPageChangeListener {
            override fun onChapterChange(pos: Int) {
                LogUtils.e("onChapterChange" + pos)
            }

            override fun requestChapters(requestChapters: MutableList<Chapter>?) {
                LogUtils.e("requestChapters")
                requestChapters?.get(0)?.let {
                    BookArticleRepository.getChapterContent(
                        it,
                        book.name + "_" + book.author,
                        object : BookArticleRepository.ArticleNetCallBack {
                            override fun onSuccess() {
                                loader.skipToChapter(loader.chapterPos)
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
                return true
            }

            override fun center() {
            }

            override fun prePage() {}

            override fun nextPage() {}

            override fun cancel() {}
        })
    }

}