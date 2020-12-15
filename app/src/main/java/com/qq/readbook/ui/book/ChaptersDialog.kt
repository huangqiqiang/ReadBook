package com.qq.readbook.ui.book

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hqq.core.ui.dialog.BaseDialog
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
class ChaptersDialog(var mPageLoader: PageLoader, var bookChapterList: List<Chapter>) :
    BaseDialog() {
    override val layoutId: Int = R.layout.dialog_chapters
    override fun initView() {
        var listView = rootView?.findViewById<RecyclerView>(R.id.rc_list)
        listView?.layoutManager = LinearLayoutManager(context).apply {
            // scrollToPosition   才会生效
            reverseLayout = true
        }
        var adapter = ChaptersAdapter()
        adapter.addData(bookChapterList)
        listView?.adapter = adapter

        listView?.postDelayed({
            listView?.scrollToPosition(adapter.itemCount - mPageLoader.chapterPos - 1)
        }, 500)
    }


    class ChaptersAdapter : BaseQuickAdapter<Chapter, BaseViewHolder>(R.layout.item_chapter) {
        override fun convert(holder: BaseViewHolder, item: Chapter) {
            holder.setText(R.id.tv_chapter, item.title)
        }

    }

}