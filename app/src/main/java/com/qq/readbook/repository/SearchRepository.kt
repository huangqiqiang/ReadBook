package com.qq.readbook.repository

import com.qq.readbook.net.ok.OkNetCallback
import com.qq.readbook.net.ok.HOkHttp

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.repository
 * @Date : 上午 10:24
 * @Email : qiqiang213@gmail.com
 * @Descrive :
 */
class SearchRepository {
    /**
     * 搜索小说
     * @param key
     */
    fun search(key: String,callback: OkNetCallback) {
        HOkHttp.newHttpCompat().get(
            "https://www.23txt.com/search.php",
            HOkHttp.newParamsCompat("q", key),callback
        )

    }
}