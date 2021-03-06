package com.qq.readbook.utils

import com.qq.readbook.bean.Book

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.utils
 * @Date : 下午 2:44
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
object CommentUtils {
    fun isRefresh(book: Book, searchDetail: Int): Boolean {
        if (searchDetail == 2) {
            if (System.currentTimeMillis() - book.refreshTime > 1000 * 60) {
                return true
            }
        }
        return false
    }

}