package com.qq.readbook.ui.book

import android.app.Activity
import android.content.Intent
import com.hqq.core.ui.base.BaseVmActivity
import com.qq.readbook.R
import com.qq.readbook.bean.Book
import com.qq.readbook.databinding.ActivityBookeDetailBinding

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.ui.book
 * @Date  : 下午 4:46
 * @Email : qiqiang213@gmail.com
 * @Descrive :
 */
class BookDetailActivity : BaseVmActivity<BookDetailViewModel, ActivityBookeDetailBinding>() {
    companion object {
        fun open(context: Activity, item: Book) {
            context.startActivityForResult(Intent(context, BookDetailActivity::class.java).apply {
                putExtra("book", item)

            }, -1)
        }
    }

    override val layoutId: Int = R.layout.activity_booke_detail
    override val bindingViewModelId: Int = 0


    override fun initViews() {
    }

}