package com.qq.readbook.repository

import com.hqq.core.net.ok.OkHttp
import com.hqq.core.net.ok.OkNetCallback


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
    fun search(key: String, callback: OkNetCallback) {
        OkHttp.newHttpCompat().get(
            "https://www.23txt.com/search.php", OkHttp.newParamsCompat("q", key), callback
        )

    }
}