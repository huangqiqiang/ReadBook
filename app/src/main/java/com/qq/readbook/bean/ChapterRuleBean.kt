package com.qq.readbook.bean

import com.google.gson.JsonElement

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.bean
 * @Date : 下午 1:31
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
class ChapterRuleBean {
    /**
     * chapterList : {"elementType":"class","elementValue":"read","ruleChild":{"elementType":"tag","elementValue":"dl","position":1}}
     * title : {"elementType":"tag","elementValue":"dd","attrValue":"text"}
     * url : {"elementType":"tag","elementValue":"a","attrValue":"href","formatRule":{"elementValue":"https://www.dstiejuan.com","elementType":"addStart"}}
     */
    var chapterList: JsonElement? = null
    var title: JsonElement? = null
    var url: JsonElement? = null
}