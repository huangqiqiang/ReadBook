package com.qq.readbook

import com.qq.readbook.bean.SourceModel

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook
 * @Date : 下午 1:36
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
class SourceUtils {
    companion object {

        @JvmStatic
        fun getInstance(): SourceUtils {//全局访问点
            return SingletonHolder.mInstance
        }

    }

    object SingletonHolder {
        //静态内部类
        val mInstance: SourceUtils = SourceUtils()
    }

    var sourceList = ArrayList<SourceModel>()

    init {
        sourceList.add(SourceModel().apply {
            this.bookSourceName = "天籁小说"
            this.bookSearchUrl = "https://www.23txt.com/search.php?q=%s"
            this.bookSourceUrl = "https://www.23txt.com/"

        })
    }


}