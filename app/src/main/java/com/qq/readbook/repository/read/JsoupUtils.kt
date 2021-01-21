package com.qq.readbook.repository.read

import android.text.Html
import com.google.gson.*
import com.hqq.core.utils.GsonUtil
import com.hqq.core.utils.log.LogUtils
import com.qq.readbook.BookSourceUtils
import com.qq.readbook.Keys
import com.qq.readbook.bean.*
import com.qq.readbook.utils.MD5Utils
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
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


    /**
     *  转义成节点对象
     * @param jsonElement JsonElement?
     * @return NodeBean?
     */
    fun getNodeBean(jsonElement: JsonElement?): NodeBean? {
        return GsonUtil.fromJson(jsonElement.toString(), NodeBean::class.java)
    }

    /**
     * 转义成JsonObject
     * @param ruleJson String
     * @return JsonElement?
     */
    fun getJsonElement(ruleJson: String): JsonElement? {
        try {
            return JsonParser.parseString(ruleJson)
        } catch (e: JsonSyntaxException) {
        }
        return null
    }


    /**
     *  读取List数据  JsonArray
     * @param doc Document
     * @param rule JsonElement
     * @return Elements?
     */
    fun getElements(doc: Element?, rule: JsonElement?): Elements? {
        if (rule != null && doc != null) {
            if (rule is JsonArray) {
                val jsonArray = rule.asJsonArray
                for (jsonElement in jsonArray) {
                    val nodeBean = getNodeBean(jsonElement)
                    if (nodeBean != null) {
                        return doElements(doc, nodeBean)
                    }
                }
            } else if (rule is JsonObject) {
                val nodeBean = getNodeBean(rule)
                if (nodeBean != null) {
                    return doElements(doc, nodeBean)
                }

            }
        }
        return null
    }

    /**
     *  读取逻辑
     * @param doc Document
     * @param jsonElement JsonElement
     * @return Elements?
     */
    private fun doElements(doc: Element, jsonElement: NodeBean): Elements? {
        val elements: Elements? = getElements4Value(jsonElement, doc)
        //  判断是否需要迭代数据
        val child = jsonElement.ruleChild
        if (child != null) {
            val element = elements?.first()
            // 递归调用
            return getElements(element, child)
        } else {
            return elements
        }
    }

    /**
     *  根据 Value 读取数据
     * @param jsonElement JsonElement
     * @param doc Document
     * @return Elements?
     */
    private fun getElements4Value(jsonElement: NodeBean, doc: Element): Elements? {
        val type = jsonElement.elementType
        val value = jsonElement.elementValue
        var elements: Elements? = null
        when (type) {
            Keys.ATTR_CLASS -> {
                elements = doc.getElementsByClass(value)
            }
            Keys.LABEL_TAG -> {
                elements = doc.getElementsByTag(value)
            }
            Keys.ID -> {
                val element = doc.getElementById(value)
                elements = Elements(element)

            }
        }
        val position = jsonElement.position
        if (position > 0) {
            val positionValue = elements?.get(position)
            if (positionValue is Elements) {
                return positionValue
            } else {
                elements = positionValue?.children()
            }
        }

        return elements
    }


    /**
     *  获取 Element 的value值
     * @param element Element?
     * @param rule JsonElement?
     * @return String
     */
    fun getElementValue(element: Element?, rule: JsonElement?): String {
        if (rule != null && element != null) {
            if (rule is JsonArray) {
                val jsonArray = rule.asJsonArray
                for (jsonElement in jsonArray) {
                    return doElementValue(element, getNodeBean(jsonElement.asJsonObject))
                }
            } else if (rule is JsonObject) {
                return doElementValue(element, getNodeBean(rule.asJsonObject))
            }
        }
        return "";
    }

    /**
     *  根据 TYPE 分类读取
     * @param element Element
     * @param nodeBean JsonObject?
     * @return String
     */
    fun doElementValue(element: Element, nodeBean: NodeBean?): String {
        val jsonElement = nodeBean?.ruleChild
        if (jsonElement != null && jsonElement is JsonObject) {
            val elements = getElements4Value(nodeBean, element)
            if (!elements.isNullOrEmpty()) {
                val position = nodeBean.position as? Int
                if (position != null && position > 0) {
                    if (elements.size > position) {
                        return doElementValue(elements.get(position), getNodeBean(jsonElement))
                    }
                } else {
                    return doElementValue(elements.get(0), getNodeBean(jsonElement))
                }
            }
        } else {
            when (nodeBean?.elementType) {
                Keys.ATTR_CLASS -> {
                    return getElementValue4Class(element, nodeBean)
                }
                Keys.LABEL_TAG -> {
                    return getElementValue4Tag(element, nodeBean)
                }
                Keys.TEXT_NODE -> {
                    return getElementValue4TextNode(element, nodeBean)
                }
            }
        }

        return ""
    }

    private fun getElementValue4TextNode(element: Element, nodeBean: NodeBean): String {
        val nodes = element.textNodes()
        for (node in nodes.withIndex()) {
            if (node.index == nodeBean.position) {
                return node.value.text()
            }
        }
        return "";
    }

    /**
     *  读取 tag  标签的值
     * @param element Element
     * @param rule JsonObject
     * @return String
     */
    fun getElementValue4Tag(element: Element, rule: NodeBean): String {
        val values = element.getElementsByTag(rule.elementValue)
        return doElementValue4Format(values, rule)
    }

    /**
     *  根据Class读取类型
     * @param element Element?
     * @param rule JsonObject?
     * @return String
     */
    fun getElementValue4Class(element: Element?, rule: NodeBean?): String {
        val values = element?.getElementsByClass(rule?.elementValue)
        return doElementValue4Format(values, rule)
    }

    /**
     *  统一转String
     * @param values Elements?
     * @param rule NodeBean?
     * @return String
     */
    private fun doElementValue4Format(values: Elements?, rule: NodeBean?): String {
        if (values == null) {
            return ""
        } else if (rule == null) {
            return ""
        }
        when (rule.attrValue) {
            Keys.ATTR_SRC, Keys.TITLE, Keys.ATTR_HREF -> {
                val str = values.attr(rule.attrValue)?.toString()
                if (str.isNullOrEmpty()) {
                    return ""
                }
                return formatString(rule, str)
            }
            Keys.ATTR_TEXT -> {
                return formatText(values, rule)
            }
        }
        return ""
    }

    /**
     *  格式化 text
     * @param elements Elements?
     * @param rule NodeBean?
     * @return String
     */
    private fun formatText(elements: Elements?, rule: NodeBean?): String {
        var text = ""
        if (elements != null) {
            if (rule != null) {
                if (rule.position <= 0) {
                    text = elements.first()?.text().toString()
                } else {
                    for ((index, element) in elements.withIndex()) {
                        if (index == rule.position) {
                            text = element.text()
                        }
                    }
                }
            } else {
                text = elements.text()
            }
        }
        if (rule?.formatRule == null) {
            return text
        } else {
            return formatString(rule, text)
        }
    }

    /**
     *  格式化 Url
     * @param rule JsonObject
     * @param url String
     * @return String
     */
    private fun formatString(rule: NodeBean, url: String): String {
        var url1 = url
        val jsonElement = rule.formatRule
        if (true == jsonElement?.toString()?.isNotEmpty()) {
            val jsonObject = getNodeBean(jsonElement.asJsonObject)
            when (jsonObject?.elementType) {
                Keys.REPLACE_END -> {
                    val value = jsonObject.elementValue
                    if (value != null) {
                        url1 = url1.replace(value, "")
                    }
                }
                Keys.REPLACE_START -> {
                    val value = jsonObject.elementValue
                    if (value != null) {
                        url1 = url1.replace(value, "")
                    }
                }
                Keys.ADD_START -> {
                    url1 = (jsonObject.elementValue + url1)
                }
            }
        }
        if (rule.formatRule != null) {
            if (rule.formatRule is JsonObject) {
                val nodeBean = getNodeBean(rule.formatRule)
                if (nodeBean != null) {
                    return formatString(nodeBean, url1)
                }
            }
        }
        return url1
    }


    /**
     *  解析文章内容
     * @param response String?
     * @param source ReadSource?
     * @return String
     */
    fun getArticleDetail(response: String?, source: ReadSource?): String {
        val doc = Jsoup.parse(response)
        val jsonElement = source?.ruleArticleContent
        val nodeBoolean = getNodeBean(jsonElement)
        if (nodeBoolean != null) {
            val divContent = getElements4Value(nodeBoolean, doc)
            if (divContent != null) {
                var content = Html.fromHtml(divContent.html()).toString()
                content = content.replace(" ", "  ")
                return content
            }
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
    fun doReadBook(element: Element, jsonElement: RuleSearchBean): Book? {
        val book = Book()
        jsonElement.apply {
            book.name = getElementValue(element, ruleBookName)
            book.author = getElementValue(element, ruleAuthor)
            book.imgUrl = getElementValue(element, ruleImg)
            book.desc = getElementValue(element, ruleDesc)
            book.chapterUrl = getElementValue(element, chapterUrl)
            book.bookDetailUrl = getElementValue(element, bookDetailUrl)
            book.newestChapterTitle = getElementValue(element, newestChapterTitle)
            book.type = getElementValue(element, ruleType)
            book.wordCount = getElementValue(element, wordCount)
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
        val doc = Jsoup.parse(html)
        if (source != null) {
            val chapterElement = GsonUtil.fromJson(source.ruleChapter, RuleChapterBean::class.java)
            if (chapterElement != null) {
                val list = getElements(doc, chapterElement.chapterList)
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
        LogUtils.e("-----------------------")
        LogUtils.e("解析最新章节")
        LogUtils.e(book.name)
        LogUtils.e(book.sourceName)
        val source = findSource(book.sourceName)
        book.newestChapterTitle = getElementValue(Jsoup.parse(html), source?.ruleNewestChapter)
        LogUtils.e("解析最新章节结束")
        LogUtils.e("-----------------------")
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
        val bookDetail = GsonUtil.fromJson(readSource.ruleBookDetail.toString(), RuleBookDetail::class.java)
        val element = Jsoup.parse(html)
        book.updateDate = getElementValue(element, bookDetail?.updateTime)
        return book
    }


}

