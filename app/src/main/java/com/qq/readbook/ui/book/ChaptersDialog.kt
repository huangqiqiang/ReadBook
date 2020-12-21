package com.qq.readbook.ui.book

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hqq.core.ui.dialog.BaseDialog
import com.hqq.core.utils.ScreenUtils
import com.qq.readbook.R
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
    override fun initView() {
        context?.let {
            rootView?.findViewById<ConstraintLayout>(R.id.cl_content)?.setPadding(
                0, ScreenUtils.getStatusBarHeight(it), 0, 0
            )
        }

        var listView = rootView?.findViewById<RecyclerView>(R.id.rc_list)
        listView?.layoutManager = LinearLayoutManager(context).apply {
            // scrollToPosition   才会生效
            reverseLayout = true
        }
        var adapter = ChaptersAdapter(mPageLoader.chapterPos)
        bookChapterList?.let { adapter.addData(it) }
        listView?.adapter = adapter.apply {
            setOnItemClickListener { adapter, view, position ->
                mPageLoader.skipToChapter(position)
                dismiss()
            }
        }

        listView?.postDelayed({
            listView?.scrollToPosition(mPageLoader.chapterPos)
        }, 500)
    }


    class ChaptersAdapter(var chapterPos: Int) :
        BaseQuickAdapter<Chapter, BaseViewHolder>(R.layout.item_chapter) {
        override fun convert(holder: BaseViewHolder, item: Chapter) {

            holder.setText(R.id.tv_chapter, item.title)

            if (holder.layoutPosition == chapterPos) {
                holder.getView<TextView>(R.id.tv_chapter).isSelected = true
                holder.setTextColorRes(R.id.tv_chapter, R.color.color_red)
            } else {
                holder.getView<TextView>(R.id.tv_chapter).isSelected = false
                if (item.isCache) {
                    holder.setTextColorRes(R.id.tv_chapter, R.color.color_333)
                } else {
                    holder.setTextColorRes(R.id.tv_chapter, R.color.color_999)
                }
            }
        }

    }

}