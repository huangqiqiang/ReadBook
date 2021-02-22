package com.qq.readbook.repository.read

import android.text.Html
import com.google.gson.*
import com.hqq.core.utils.GsonUtil
import com.hqq.core.utils.log.LogUtils
import com.qq.readbook.BookSourceUtils
import com.qq.readbook.bean.*
import com.qq.readbook.utils.MD5Utils
import org.seimicrawler.xpath.JXDocument
import org.seimicrawler.xpath.JXNode
import java.util.*

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.repository
 * @Date : 下午 5:02
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
object JsoupUtils {

    /**
     *  查找资源
     * @param sourceName String?
     * @return ReadSource?
     */
    private fun findSource(sourceName: String?): ReadSource? {
        return BookSourceUtils.getInstance().sourceList?.first {
            it.bookSourceName == sourceName
        }
    }


    fun getValue4key(element: JXNode?, ruleBookName: String?): String {
        if (ruleBookName.isNullOrEmpty() || element == null) {
            return "";
        }
        val value = element.selOne(ruleBookName)?.asString()
        if (value.isNullOrEmpty()) {
            return "";
        }
        return value
    }

    private fun getValue4key(element: JXDocument?, ruleBookName: String?): String {
        if (ruleBookName.isNullOrEmpty() || element == null) {
            return "";
        }
        val value = element.selOne(ruleBookName) ?: return ""
        if (value is String) {
            if (value.isNullOrEmpty()) {
                return "";
            }
            return value
        }
        return value.toString()
    }

    private fun getElementValue(element: JXNode?, rule: String?): String {
        return getValue4key(element, rule)
    }


    /**
     *  解析文章内容
     * @param response String?
     * @param source ReadSource?
     * @return String
     */
    fun getArticleDetail(html: String?, source: ReadSource?): String {
        val ruleArticleContent =
            GsonUtil.fromJson(source?.ruleArticleContent.toString(), RuleArticleContent::class.java)
        val nodeBoolean = getValue4key(JXDocument.create(html), ruleArticleContent?.content)
        if (nodeBoolean != null) {
            var content = Html.fromHtml(nodeBoolean).toString()
            content = content.replace(" ", "  ")
            return content
        }
        return ""
    }

    /**
     *  读取Book数据
     * @param element Element html 数据源
     * @param source ReadSource 源
     * @param jsonElement JsonElement  key
     * @return Book
     */
    fun doReadBook(element: JXNode, jsonElement: RuleSearchBean): Book? {
        val book = Book()
        jsonElement.apply {
            book.name = getValue4key(element, ruleBookName)
            book.author = getValue4key(element, ruleAuthor)
            book.imgUrl = getValue4key(element, ruleImg)
            book.desc = getValue4key(element, ruleDesc)
            book.chapterUrl = getValue4key(element, ruleChapterUrl)
            book.bookDetailUrl = getValue4key(element, ruleBookDetailUrl)
            book.newestChapterTitle = getValue4key(element, ruleNewestChapterTitle)
            book.type = getValue4key(element, ruleType)
            book.wordCount = getValue4key(element, ruleWordCount)
            book.updateDate = getValue4key(element, ruleUpdateData)
            book.bookId = MD5Utils.getStringMD5(book.name + book.author)
        }
        if (book.name.isEmpty() && book.author.isEmpty()) {
            return null
        }
        return book
    }

    /**
     *  读取章节列表
     * @param html String
     * @param source ReadSource?
     * @return ArrayList<Chapter>
     */
    fun readChapter(html: String, source: ReadSource?, book: Book): ArrayList<Chapter> {
        val chapters = ArrayList<Chapter>()
        if (source != null) {
            val chapterElement = GsonUtil.fromJson(source.ruleChapter, RuleChapterBean::class.java)
            LogUtils.d("解析: " + chapterElement.toString())
            if (chapterElement != null) {
                val list = JXDocument.create(html).selN(chapterElement.chapterList)
                if (list != null) {
                    for ((index, child) in list.withIndex()) {
                        val chapter = Chapter()
                        chapter.apply {
                            title = getElementValue(child, chapterElement.title)
                            url = getElementValue(child, chapterElement.url)
                            number = index
                            sources = source.bookSourceName
                            bookId = book.bookId
                            if (title.isNotEmpty() && "null" != title) {
                                chapters.add(chapter)
                            }
                        }
                    }
                }
            }
        }

        return chapters
    }


    /**
     *  解析最新章节
     * @param html String?
     * @param book Book
     * @return Book
     */
    fun getNewChapterFormHtml(html: String?, book: Book): Book {
        LogUtils.d("-----------------------")
        LogUtils.d("解析最新章节:")
        LogUtils.d(book.name + "    :   " + book.sourceName)
        LogUtils.d("-----------------------")
        LogUtils.d(html?.replace(Regex("\r|\n|\t"), ""))
        LogUtils.d("-----------------------")
        val source = findSource(book.sourceName)
        LogUtils.d(source?.ruleNewestChapter)
        LogUtils.d("-----------------------")

        book.newestChapterTitle = getValue4key(JXDocument.create(html), source?.ruleNewestChapter)
        LogUtils.d(book.newestChapterTitle)
        LogUtils.d("解析最新章节结束 " + book.newestChapterTitle)
        LogUtils.d("-----------------------")
        return book
    }

    /**
     *  获取书籍详情
     * @param html String
     * @param book Book
     * @param readSource ReadSource
     * @return Book
     */
    fun getBookDetail(html: String, book: Book, readSource: ReadSource): Book {
        val bookDetail = GsonUtil.fromJson(readSource.ruleBookDetail.toString(), RuleSearchBean::class.java)
        book.updateDate = getValue4key(JXDocument.create(html), bookDetail?.ruleUpdateData)
        return book
    }

}

