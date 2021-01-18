package com.qq.readbook.repository

import android.text.Html
import com.google.gson.*
import com.hqq.core.utils.log.LogUtils
import com.qq.readbook.Keys
import com.qq.readbook.bean.Book
import com.qq.readbook.bean.Chapter
import com.qq.readbook.bean.ReadSource
import com.qq.readbook.utils.MD5Utils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.util.ArrayList

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.repository
 * @Date : 下午 5:02
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
object JsoupUtils {

    /**
     * 转义成JsonObject
     * @param ruleJson String
     * @return JsonElement?
     */
    fun getJsonElement(ruleJson: String): JsonElement? {
        try {
            var jsonElement = JsonParser.parseString(ruleJson)
            if (jsonElement is JsonObject) {
                return jsonElement
            }
        } catch (e: JsonSyntaxException) {

        }
        return null
    }

    /**
     *  获取 Element 的value值
     * @param element Element?
     * @param rule JsonElement?
     * @return String
     */
    fun getElementValue(element: Element?, rule: JsonElement?): String {
        rule?.let {
            element?.let {
                if (rule is JsonArray) {
                    val jsonArray = rule.asJsonArray
                    for (jsonElement in jsonArray) {
                        return doElementValue(element, jsonElement.asJsonObject)
                    }
                } else if (rule is JsonObject) {
                    return doElementValue(element, rule.asJsonObject)
                }
            }
        }
        return "";
    }

    /**
     *  根据 TYPE 分类读取
     * @param element Element
     * @param jsonObject JsonObject?
     * @return String
     */
    fun doElementValue(element: Element, jsonObject: JsonObject?): String {
        when (jsonObject?.get(Keys.ELEMENT_TYPE)?.asString) {
            Keys.ATTR_CLASS -> {
                return getClassElementValue(element, jsonObject)
            }
            Keys.LABEL_TAG
            -> {
                return getTagElementValue(element, jsonObject)
            }
        }

        return ""
    }

    /**
     *  读取 tag  标签的值
     * @param element Element
     * @param rule JsonObject
     * @return String
     */
    fun getTagElementValue(element: Element, rule: JsonObject): String {
        val value = element.getElementsByTag(rule.get(Keys.ELEMENT_VALUE)?.asString)
        when (rule.get(Keys.ATTR_VALUE)?.asString) {
            Keys.ATTR_SRC -> {
                //预留一个Position
                return value.find {
                    it.hasAttr(Keys.ATTR_SRC)
                }?.attr(Keys.ATTR_SRC).toString()
            }
            Keys.ATTR_TEXT -> {
                return value.text()
            }
            Keys.ATTR_HREF -> {
                return doFormatHref(value, rule)
            }
        }

        return ""

    }

    /**
     *  根据Class读取类型
     * @param element Element?
     * @param rule JsonObject?
     * @return String
     */
    fun getClassElementValue(element: Element?, rule: JsonObject?): String {
        val elements = element?.getElementsByClass(rule?.get(Keys.ELEMENT_VALUE)?.asString)
        when (rule?.get(Keys.ATTR_VALUE)?.asString) {
            Keys.ATTR_TEXT -> {
                return elements?.first()?.text().toString()
            }
            Keys.ATTR_HREF -> {
                return doFormatHref(elements, rule)
            }
        }

        return ""
    }

    /**
     *  格式化 Href
     * @param elements Elements?
     * @param rule JsonObject?
     * @return String
     */
    private fun doFormatHref(elements: Elements?, rule: JsonObject?): String {
        val url = elements?.attr(Keys.ATTR_HREF).toString()
        rule?.let {
            return formatUrl(rule, url)
        }
        return ""
    }

    /**
     *  格式化 Url
     * @param rule JsonObject
     * @param url String
     * @return String
     */
    private fun formatUrl(rule: JsonObject, url: String): String {
        var url1 = url
        val jsonElement = rule.get(Keys.FORMAT_RULE);
        if (true == jsonElement?.toString()?.isEmpty()) {
            return url1
        } else {
            val jsonObject = jsonElement.asJsonObject
            when (jsonObject.get(Keys.TYPE)?.asString) {
                Keys.REPLACE_END -> {
                    url1 = url1.replace(jsonObject.get(Keys.VALUE).asString, "")
                }
                Keys.ADD_START -> {
                    url1 = (jsonObject.get(Keys.VALUE)?.asString + url1)
                }
            }
            return url1
        }
    }

    /**
     *  读取List数据  JsonArray
     * @param doc Document
     * @param rule JsonElement
     * @return Elements?
     */
    fun getElementList(doc: Element?, rule: JsonElement?): Elements? {
        if (rule!=null && doc !=null) {
            if (rule is JsonArray) {
                var jsonArray = rule.asJsonArray
                for (jsonElement in jsonArray) {
                    return doElementList(doc, jsonElement)
                }
            } else if (rule is JsonObject) {
                return doElementList(doc, rule.asJsonObject)

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
    private fun doElementList(doc: Element, jsonElement: JsonElement): Elements? {
        var elements: Elements? = getElementsByValue(jsonElement, doc)
        //  判断是否需要迭代数据
        val child = jsonElement.asJsonObject.get(Keys.RULE_CHILD)
        if (child != null && child is JsonObject) {
         val  element=  elements?.first()
            // 递归调用
            return getElementList(element, child)
        } else {
            return elements
        }
        return null
    }

    /**
     *  根据 Value 读取数据
     * @param jsonElement JsonElement
     * @param doc Document
     * @return Elements?
     */
    private fun getElementsByValue(jsonElement: JsonElement, doc: Element): Elements? {
        val type = jsonElement.asJsonObject.get(Keys.ELEMENT_TYPE)?.asString
        val value = jsonElement.asJsonObject.get(Keys.ELEMENT_VALUE)?.asString
        var elements: Elements? = null
        when (type) {
            Keys.ATTR_CLASS -> {
                elements = doc.getElementsByClass(value)
            }
            Keys.LABEL_TAG -> {
                elements = doc.getElementsByTag(value)
            }
        }
        val postion = jsonElement.asJsonObject.get(Keys.POSITION)?.asInt
        if (postion != null && postion > 0) {
            val positionValue = elements?.get(postion)
            if (positionValue is Elements) {
                return positionValue
            } else {
                LogUtils.e("getElementsByValue :  $jsonElement")
                elements = positionValue?.children()
            }
        }

        return elements
    }

    /**
     *  读取Book数据
     * @param element Element html 数据源
     * @param source ReadSource 源
     * @param jsonElement JsonElement  key
     * @return Book
     */
    fun doReadBook(element: Element, source: ReadSource, jsonElement: JsonElement): Book? {
        val book = Book()
        if (jsonElement is JsonObject) {
            jsonElement.apply {
                book.imgUrl = getElementValue(element, get(Keys.RULE_IMG))
                book.chapterUrl = (source.bookSourceUrl + getElementValue(element, get(Keys.CHAPTER_URL)))
                book.newestChapterTitle = getElementValue(element, get(Keys.NEWEST_CHAPTER_TITLE))
                book.author = getElementValue(element, get(Keys.RULE_AUTHOR))
                book.name = getElementValue(element, get(Keys.RULE_BOOK_NAME))
                book.desc = getElementValue(element, get(Keys.RULE_DESC))
                book.type = getElementValue(element, get(Keys.RULE_TYPE))
                book.source = source.bookSourceName
                book.bookId = MD5Utils.getStringMD5(book.name + book.author)
            }
        }
        if (book.name.isEmpty()&& book.author.isEmpty()){
            return null;
        }
        return book
    }

    /**
     *  读取章列表
     * @param html String
     * @param source ReadSource?
     * @return ArrayList<Chapter>
     */
    fun readChapter(html: String, source: ReadSource?, book: Book): ArrayList<Chapter> {
        val chapters = ArrayList<Chapter>()
        val doc = Jsoup.parse(html)
        if (source != null) {
            val chapterElement = getJsonElement(source.chapterRule)
            chapterElement?.let {
                if (it is JsonObject) {
                    var list = getElementList(doc, it.asJsonObject.get(Keys.CHAPTER_LIST))
                    if (list != null) {
                        for ((index, child) in list.withIndex()) {
                            val chapter = Chapter()
                            chapter.apply {
                                title = getElementValue(child, chapterElement.asJsonObject.get(Keys.TITLE))
                                url = getElementValue(child, chapterElement.asJsonObject.get(Keys.URL))
                                number = index
                                sources = source.bookSourceName
                                bookId = book.bookId
                                if (title.isNotEmpty() || url.isNotEmpty()) {
                                    chapters.add(chapter)
                                }
                            }
                        }
                    }
                }
            }
        }

        return chapters
    }

    /**
     *  读取文章内容
     * @param response String?
     * @param source ReadSource?
     * @return String
     */
    fun getArticleDetail(response: String?, source: ReadSource?): String {
        val doc = Jsoup.parse(response)
        val jsonElement = source?.articleContent?.let { getJsonElement(it) }
        jsonElement?.let {
            val divContent = getElementsByValue(it, doc)
            if (divContent != null) {
                var content = Html.fromHtml(divContent.html()).toString()
                content = content.replace(" ", "  ")
                return content
            }
        }
        return ""
    }


}