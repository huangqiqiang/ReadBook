package com.qq.readbook.repository.read.face

import com.qq.readbook.bean.Book
import com.qq.readbook.bean.BookSource
import java.util.ArrayList

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.repository.read
 * @Date : 下午 4:41
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
interface Read {
    /**
     * 搜索解析
     */
    fun readSearch(html: String, source: BookSource): ArrayList<Book>

    /**
     * 书籍详情解析
     */
    fun readBookDetail()

    /**
     * 最新章节
     */
    fun readNewestChapter()

    /**
     * 读取所有章节
     */
    fun readChapters()
}