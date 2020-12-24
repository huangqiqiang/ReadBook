package com.qq.readbook.ui.book

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hqq.core.ui.dialog.BaseDialog
import com.hqq.core.utils.ScreenUtils
import com.qq.readbook.R
import com.qq.readbook.adapter.ChaptersAdapter
import com.qq.readbook.bean.Chapter
import com.qq.readbook.weight.page.loader.PageLoader

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.ui.book
 * @Date : 上午 11:06
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
class ChaptersDialog(var mPageLoader: PageLoader, var bookChapterList: List<Chapter>?) :
    BaseDialog() {

    override val layoutId: Int = R.layout.dialog_chapters

    /**
     *  true  升序
     *  false 降序
     */
    var soft = false
    override fun initView() {
        context?.let {
            rootView?.findViewById<ConstraintLayout>(R.id.cl_content)?.setPadding(
                0, ScreenUtils.getStatusBarHeight(it), 0, 0
            )
        }
        var listView = rootView?.findViewById<RecyclerView>(R.id.rc_list)
        listView?.apply {
            layoutManager = LinearLayoutManager(context).apply {
                // scrollToPosition   才会生效
                reverseLayout = true
            }
            this.adapter = ChaptersAdapter(mPageLoader.chapterPos).apply {
                setOnItemClickListener { adapter, view, position ->
                    mPageLoader.skipToChapter(position)
                    dismiss()
                }
                bookChapterList?.let { this.addData(it) }
            }
            postDelayed({
                goCurrPosition()
            }, 500)
        }

        rootView?.findViewById<ImageView>(R.id.iv_close)?.setOnClickListener {
            dismiss()
        }
        rootView?.findViewById<ImageView>(R.id.iv_locate)?.setOnClickListener {
            goCurrPosition()
        }

        rootView?.findViewById<TextView>(R.id.ll_right)?.setOnClickListener {
            var listView = rootView?.findViewById<RecyclerView>(R.id.rc_list)
            var manager = listView?.layoutManager as? LinearLayoutManager
            if (soft) {
                (it as TextView).text = "降序"
            } else {
                (it as TextView).text = "升序"
            }
            soft = !soft
            manager?.stackFromEnd = !soft
            manager?.reverseLayout = !soft
            goCurrPosition()
        }
    }

    private fun goCurrPosition() {
        var listView = rootView?.findViewById<RecyclerView>(R.id.rc_list)
        var manager = listView?.layoutManager as? LinearLayoutManager
        var position = mPageLoader.chapterPos;
        context?.let {
            // 设置偏移量  为什么是4  我也不懂
            manager?.scrollToPositionWithOffset(
                position,
                ScreenUtils.getScreenHeight(it) / 4
            )
        }
    }


}