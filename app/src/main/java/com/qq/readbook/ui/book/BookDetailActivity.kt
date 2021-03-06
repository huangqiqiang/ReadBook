package com.qq.readbook.ui.book

import android.app.Activity
import android.content.Intent
import com.hqq.core.ui.base.BaseVmActivity
import com.qq.readbook.BR
import com.qq.readbook.Keys
import com.qq.readbook.R
import com.qq.readbook.bean.Book
import com.qq.readbook.databinding.ActivityBookeDetailBinding
import com.qq.readbook.repository.BookChaptersRepository

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.ui.book
 * @Date  : 下午 4:46
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
class BookDetailActivity : BaseVmActivity<BookDetailViewModel, ActivityBookeDetailBinding>() {
    companion object {
        fun open(context: Activity, item: Book) {
            context.startActivityForResult(Intent(context, BookDetailActivity::class.java).apply {
                putExtra(Keys.BOOK, item)
            }, -1)
        }
    }

    override val layoutId: Int = R.layout.activity_booke_detail

    override val bindingViewModelId: Int = BR.vm

    override fun initViews() {

    }


}