package com.qq.readbook.repository

import com.qq.readbook.utils.room.RoomUtils
import com.qq.readbook.weight.page.BookRecordBean
import com.qq.readbook.weight.page.CollBookBean

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.repository
 * @Date : 下午 5:01
 * @Email : qiqiang213@gmail.com
 * @Descrive :
 */
object ReadRepository {
    @JvmStatic
    fun getBookRecord(book: CollBookBean, bookId: String): BookRecordBean? {

        var list = RoomUtils.getChapterDataBase(book.title + "_" + book.author).bookRecordBeanDao()
            .getAll()

        if (list.isNotEmpty()) {
            return list[0]
        }
        return null;
    }

    @JvmStatic
    fun saveBookRecord(
        book: CollBookBean,
        mBookRecord: BookRecordBean
    ) {
        RoomUtils.getChapterDataBase(book.title + "_" + book.author).bookRecordBeanDao().apply {
            if (mBookRecord.id > -1) {
                update(mBookRecord)
            } else {
                insert(mBookRecord)
            }

        }

    }
}