package com.qq.readbook.bean

import com.google.gson.JsonElement

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.bean
 * @Date : 下午 1:23
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
class RuleSearchBean {
    /**
     * ruleSearchList : [{"elementType":"class","elementValue":"library","ruleChild":{"elementType":"tag","elementValue":"li"}}]
     * ruleBookName : [{"elementType":"class","elementValue":"bookname","attrValue":"text"}]
     * ruleDesc : [{"elementType":"class","elementValue":"intro","attrValue":"text"}]
     * ruleAuthor : [{"elementType":"class","elementValue":"author","attrValue":"text"}]
     * newestChapterTitle : [{"elementType":"class","elementValue":"chapter","attrValue":"text"}]
     * chapterUrl : [{"elementType":"class","elementValue":"bookimg","attrValue":"href","formatRule":{"elementType":"replaceEnd","elementValue":".html","formatRule":{"elementType":"addStart","elementValue":"https://www.dstiejuan.com"}}}]
     * ruleImg : [{"elementType":"tag","elementValue":"img","attrValue":"src","position":0}]
     * ruleType : {"elementType":"tag","elementValue":"p","ruleChild":{"elementType":"tag","elementValue":"a","position":1}}
     */
    var ruleType: JsonElement? = null
    var ruleSearchList: JsonElement? = null
    var ruleBookName: JsonElement? = null
    var ruleDesc: JsonElement? = null
    var ruleAuthor: JsonElement? = null
    var newestChapterTitle: JsonElement? = null
    var chapterUrl: JsonElement? = null
    var ruleImg: JsonElement? = null
    var bookDetailUrl: JsonElement? = null
    var wordCount: JsonElement? = null
}