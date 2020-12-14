package com.qq.readbook.weight.page

import com.qq.readbook.bean.Chapter
import java.io.Serializable
import java.util.*

/**
 * 收藏的书籍
 */
class CollBookBean : Serializable {
    /**
     * _id : 53663ae356bdc93e49004474
     * title : 逍遥派
     * author : 白马出淤泥
     * shortIntro : 金庸武侠中有不少的神秘高手，书中或提起名字，或不曾提起，总之他们要么留下了绝世秘笈，要么就名震武林。 独孤九剑的创始者，独孤求败，他真的只创出九剑吗？ 残本葵花...
     * cover : /cover/149273897447137
     * hasCp : true
     * latelyFollower : 60213
     * retentionRatio : 22.87
     * updated : 2017-05-07T18:24:34.720Z
     *
     *
     * chaptersCount : 1660
     * lastChapter : 第1659章 朱长老
     */
    // 本地书籍中，path 的 md5 值作为本地书籍的 id
    var bookId: String? = null
    var title: String? = null
    var author: String? = null
    var shortIntro: String? = null

    // 在本地书籍中，该字段作为本地文件的路径
    var cover: String? = null
    var isHasCp = false
    var latelyFollower = 0
    var retentionRatio = 0.0

    //最新更新日期
    var updated: String? = null

    //最新阅读日期
    var lastRead: String? = null
    var chaptersCount = 0
    var lastChapter: String? = null

    //是否更新或未阅读
    var isUpdate = true

    //是否是本地文件
    var isLocal = false
    var isSelect = false
    var include_image = 0
    var bookChapterList: List<Chapter> = ArrayList()
    val id: String
        get() = if (bookId == null) "" else bookId!!

    fun setIsUpdate(update: Boolean) {
        isUpdate = update
    }

    val bookChapters: List<Chapter>
        get() = if (bookChapterList == null) {
            ArrayList()
        } else bookChapterList
}