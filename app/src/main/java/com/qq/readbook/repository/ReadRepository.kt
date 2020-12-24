package com.qq.readbook.repository

import com.qq.readbook.bean.Book
import com.qq.readbook.utils.room.RoomUtils
import com.qq.readbook.weight.page.BookRecordBean

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.repository
 * @Date : 下午 5:01
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
object ReadRepository {
    @JvmStatic
    fun getBookRecord(book: Book, bookId: String): BookRecordBean? {
        var list = RoomUtils.getChapterDataBase(book.name + "_" + book.author).bookRecordBeanDao().getAll()
        if (list.isNotEmpty()) {
            return list[0]
        }
        return null;
    }

    @JvmStatic
    fun saveBookRecord(
        book: Book,
        mBookRecord: BookRecordBean
    ) {
        RoomUtils.getChapterDataBase(book.name + "_" + book.author).bookRecordBeanDao().apply {
            if (mBookRecord.id > -1) {
                update(mBookRecord)
            } else {
                insert(mBookRecord)
            }

        }

    }
}