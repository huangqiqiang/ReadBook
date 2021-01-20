package com.qq.readbook.bean

import com.google.gson.JsonElement

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.bean
 * @Date : 下午 12:00
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
class ReadSource {
    /**
     *  资源名称
     */
    var bookSourceName: String = ""

    /**
     *  资源地址
     */
    var bookSourceUrl: String = ""

    /**
     *  资源搜索地址
     */
    var bookSearchUrl: String = ""

    /**
     *   是否需要encode
     */
    var searchEncode = ""

    /**
     *  搜索规则
     */
    var searchRule: JsonElement? = null

    /**
     *  章节规则
     */
    var chapterRule: JsonElement? = null

    /**
     *  解析文章详情
     */
    var articleContent: JsonElement? = null

    /**
     *  最新章节
     */
    var newestChapter: JsonElement? = null

    /**
     *  搜索详情
     *  0 直接网页可以解析出所有数据
     *  1 需要在搜索后执行详情
     *  2 需要在详情界面爬取详情数据
     *
     */
    var searchDetail: Int = 0


}