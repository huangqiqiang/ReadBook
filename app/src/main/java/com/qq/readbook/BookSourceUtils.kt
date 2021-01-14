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

    val sourceList: ArrayList<ReadSource>?
        get() {
            return App.sourceList
        }

    init {
//        sourceList.add(ReadSource().apply {
//            this.bookSourceName = "丹书铁卷⑫"
//            this.bookSearchUrl = "https://www.dstiejuan.com/search.html@searchkey"
//            this.bookSourceUrl = "https://www.dstiejuan.com"
//            this.ruleSearchList = "[{\"elementType\":\"class\",\"elementValue\":\"library\"}]"
//            this.ruleBookName = "[{\"elementType\":\"class\",\"elementValue\":\"bookname\",\"attrValue\":\"text\"}]"
//            this.ruleDesc = "[{\"elementType\":\"class\",\"elementValue\":\"intro\",\"attrValue\":\"text\"}]"
//            this.ruleAuthor = "[{\"elementType\":\"class\",\"elementValue\":\"author\",\"attrValue\":\"text\"}]"
//            this.newestChapterTitle = "[{\"elementType\":\"class\",\"elementValue\":\"chapter\",\"attrValue\":\"text\"}]"
//            this.chapterUrl = "[{\"elementType\":\"class\",\"elementValue\":\"bookimg\",\"attrValue\":\"href\",\"chapterRule\":{\"end\":\".html\"}}]"
//            this.ruleImg = "[{\"elementType\":\"tag\",\"elementValue\":\"img\",\"attrValue\":\"src\",\"position\": 0}]"
//       })
//        sourceList.add(ReadSource().apply {
//            this.bookSourceName = "天籁小说"
//            this.bookSearchUrl = "https://www.23txt.com/search.php?q=%s"
//            this.bookSourceUrl = "https://www.23txt.com/"
//            this.sourcesClass = "TianlaiRead"
//            this.ruleChapterList = "result-game-item-detail"
//            this.ruleSearchList = "[{\"elementType\":\"class\",\"elementValue\":\"result-list\"}]"
//            this.ruleBookName =
//                "[{\"elementType\":\"class\",\"elementValue\":\"bookname\",\"attrValue\":\"text\"}]"
//            this.ruleDesc =
//                "[{\"elementType\":\"class\",\"elementValue\":\"intro\",\"attrValue\":\"text\"}]"
//            this.ruleAuthor =
//                "[{\"elementType\":\"class\",\"elementValue\":\"author\",\"attrValue\":\"text\"}]"
//            this.newestChapterTitle =
//                "[{\"elementType\":\"class\",\"elementValue\":\"result-game-item-info-tag-item\",\"attrValue\":\"text\"}]"
//        })
//        sourceList.add(ReadSource().apply {
//            this.bookSourceName = "笔趣阁"
//            this.bookSearchUrl = "https://www.52bqg.net/modules/article/search.php?searchkey=%s"
//            this.bookSourceUrl = "https://www.23txt.com/"
//            this.sourcesClass = "BiQuGeRead"
//            this.searchEncode = "gb2312"
//            this.ruleSearchList = "[{\"elementType\":\"class\",\"elementValue\":\"result-list\"}]"
//            this.ruleBookName =
//                "[{\"elementType\":\"class\",\"elementValue\":\"bookname\",\"attrValue\":\"text\"}]"
//            this.ruleDesc =
//                "[{\"elementType\":\"class\",\"elementValue\":\"intro\",\"attrValue\":\"text\"}]"
//            this.ruleAuthor =
//                "[{\"elementType\":\"class\",\"elementValue\":\"author\",\"attrValue\":\"text\"}]"
//            this.newestChapterTitle =
//                "[{\"elementType\":\"class\",\"elementValue\":\"chapter\",\"attrValue\":\"text\"}]"
//        })
    }


}