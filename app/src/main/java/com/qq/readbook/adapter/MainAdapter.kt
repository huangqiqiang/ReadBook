package com.qq.readbook.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hqq.core.glide.ImageLoadUtils
import com.hqq.core.utils.DateUtils
import com.qq.readbook.R
import com.qq.readbook.bean.Book

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.adapter
 * @Date : 上午 10:52
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
class MainAdapter : BaseQuickAdapter<Book, BaseViewHolder>(R.layout.item_book_main),
    LoadMoreModule {
    init {
        addChildClickViewIds(R.id.tv_delete, R.id.tv_detail, R.id.ll_content)
    }

    override fun convert(holder: BaseViewHolder, item: Book) {
        holder.setText(R.id.tv_book_name, item.name)
        holder.setText(R.id.tv_book_author, item.author)
        holder.setText(R.id.tv_book_type, item.type)
        holder.setText(
            R.id.tv_update_time, com.qq.readbook.weight.page.DateUtils.dateConvert(
                DateUtils.string2Millisecond(item.updateDate) / 1000,
                0
            ) + "  " + item.newestChapterTitle
        )
        ImageLoadUtils.withFillet(item.imgUrl, holder.getView(R.id.iv_book_img))
        if (item.topTime.isNullOrEmpty()) {
            holder.setText(R.id.tv_top, "置顶")
        } else {
            holder.setText(R.id.tv_top, "取消置顶")
        }

    }
}