package com.qq.readbook.repository

import com.google.gson.*
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
            Keys.CLASS -> {
                return getClassElementValue(element, jsonObject)
            }
            Keys.TAG -> {
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
        when (rule.get(Keys.ATTR_VALUE)?.asString) {
            Keys.SRC -> {
                //预留一个Position
                return element.getElementsByTag(rule.get(Keys.ELEMENT_VALUE)?.asString).find {
                    it.hasAttr(Keys.SRC)
                }?.attr(Keys.SRC).toString()

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
        when (rule?.get(Keys.ATTR_VALUE)?.asString) {
            Keys.TEXT -> {
                return element?.getElementsByClass(rule.get(Keys.ELEMENT_VALUE).asString)?.first()?.text().toString()
            }
            Keys.HREF -> {
                var url = element?.getElementsByClass(rule.get(Keys.ELEMENT_VALUE).asString)?.attr(Keys.HREF).toString()
                return formatUrl(rule, url)
            }
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
        val jsonElement = rule.get(Keys.CHAPTER_RULE);
        if (true == jsonElement?.toString()?.isEmpty()) {
            return url1
        } else {
            val jsonObject = jsonElement.asJsonObject
            if (jsonObject.get(Keys.END).asString.isNotEmpty()) {
                url1 = url1.replace(jsonObject.get(Keys.END).asString, "")
            }
            return url1
        }
    }

    /**
     *  读取 搜索列表
     * @param doc Document
     * @param rule JsonElement
     * @return Elements?
     */
    fun getBookElementList(doc: Document, rule: JsonElement): Elements? {
        if (rule is JsonArray) {
            var jsonArray = rule.asJsonArray
            for (jsonElement in jsonArray) {
                if (jsonElement.asJsonObject.get(Keys.ELEMENT_TYPE).asString == Keys.CLASS) {
                    return doc.getElementsByClass(
                        jsonElement.asJsonObject.get(Keys.ELEMENT_VALUE).asString
                    )
                }
            }
        } else if (rule is JsonObject) {
            val jsonObject = rule.asJsonObject
            if (jsonObject.get(Keys.ELEMENT_TYPE).asString == Keys.CLASS) {
                return doc.getElementsByClass(jsonObject.get(Keys.ELEMENT_VALUE).asString)
            }
        }
        return null
    }

    /**
     *  读取Book数据
     * @param element Element html 数据源
     * @param source ReadSource 源
     * @param jsonElement JsonElement  key
     * @return Book
     */
    fun doReadBook(element: Element, source: ReadSource, jsonElement: JsonElement): Book {
        val book = Book()
        if (jsonElement is JsonObject) {
            jsonElement.apply {
                book.imgUrl = getElementValue(element, get(Keys.RULE_IMG))
                book.chapterUrl = (source.bookSourceUrl + getElementValue(element, get(Keys.CHAPTER_URL)))
                book.newestChapterTitle = getElementValue(element, get(Keys.NEWEST_CHAPTER_TITLE))
                book.author = getElementValue(element, get(Keys.RULE_AUTHOR))
                book.name = getElementValue(element, get(Keys.RULE_BOOK_NAME))
                book.desc = getElementValue(element, get(Keys.RULE_DESC))
                book.source = source.bookSourceName
                book.bookId = MD5Utils.getStringMD5(book.name + book.author)
            }
        }
        return book
    }

    /**
     *  读取章列表
     * @param html String
     * @param source ReadSource?
     * @return ArrayList<Chapter>
     */
    fun readChapter(html: String, source: ReadSource?): ArrayList<Chapter> {
        val chapters = ArrayList<Chapter>()
        val doc = Jsoup.parse(html)
        if (source != null) {
            val searchElement = getJsonElement(source.chapterRule)


            var list = doc.getElementsByClass("read").firstOrNull()?.getElementsByTag("dl")?.get(1)
        }

        return chapters;
    }


}