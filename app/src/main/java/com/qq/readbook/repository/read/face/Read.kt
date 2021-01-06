package com.qq.readbook.repository.read.face

import com.qq.readbook.bean.Book
import com.qq.readbook.bean.BookSource
import com.qq.readbook.bean.Chapter
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
     * 读取搜索数据
     */
    fun readSearch(html: String, source: BookSource): ArrayList<Book>

    /**
     * 读取所有章节
     */
    fun readChapters(html: String?, book: Book,source: BookSource): ArrayList<Chapter>

    /**
     * 书籍详情
     */
    fun readBookDetail()

    /**
     * 最新章节
     */
    fun readNewestChapter()


}