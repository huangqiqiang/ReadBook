package com.qq.readbook

import com.qq.readbook.bean.ReadSource

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook
 * @Date : 下午 1:36
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
class BookSourceUtils {
    companion object {

        @JvmStatic
        fun getInstance(): BookSourceUtils {//全局访问点
            return SingletonHolder.M_INSTANCE
        }

    }

    object SingletonHolder {
        //静态内部类
        val M_INSTANCE: BookSourceUtils = BookSourceUtils()
    }

    var sourceList = ArrayList<ReadSource>()

    init {
        sourceList.add(ReadSource().apply {
            this.bookSourceName = "丹书铁卷⑫"
            this.bookSearchUrl = "https://www.dstiejuan.com/search.html@searchkey"
            this.bookSourceUrl = "https://www.dstiejuan.com"
            this.sourcesClass = "DSTieJuanRead"
        })
        sourceList.add(ReadSource().apply {
            this.bookSourceName = "天籁小说"
            this.bookSearchUrl = "https://www.23txt.com/search.php?q=%s"
            this.bookSourceUrl = "https://www.23txt.com/"
            this.sourcesClass = "TianlaiRead"
            this.ruleChapterList = "result-game-item-detail"
        })
        sourceList.add(ReadSource().apply {
            this.bookSourceName = "笔趣阁"
            this.bookSearchUrl = "https://www.52bqg.net/modules/article/search.php?searchkey=%s"
            this.bookSourceUrl = "https://www.23txt.com/"
            this.sourcesClass = "BiQuGeRead"
            this.searchEncode = "gb2312"
        })
    }


}