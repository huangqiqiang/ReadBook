package com.qq.readbook.bean

import com.google.gson.JsonElement

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.bean
 * @Date : 上午 11:17
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
class NodeBean {
    /**
     * elementType : id
     * elementValue : content
     */
    var elementType: String? = null
    var elementValue: String? = null
    var attrValue: String? = null
    var position :Int=0
    /**
     * ruleChild : {"elementType":"tag","elementValue":"a","attrValue":"text"}
     */
    var ruleChild: JsonElement? = null
    /**
     * {"formatRule":{"elementType":"replaceEnd","elementValue":".html","formatRule":{"elementType":"addStart","elementValue":"https://www.dstiejuan.com"}}}
     */
    var formatRule: JsonElement? = null
}