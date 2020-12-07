package com.qq.readbook.repository

import com.qq.readbook.net.ok.HOKNetCallback
import com.qq.readbook.net.ok.HOkHttp
import com.qq.readbook.net.ok.HttpCompat
import java.util.*
import kotlin.collections.HashMap

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
    fun search(key: String,) {
//        ApiManager.getApi23TxtInterface().getSearchList(key)
//            .enqueue(object : Callback<Document> {
//                override fun onResponse(call: Call<Document>, response: Response<Document>) {
//                    LogUtils.e("onResponse")
//
//                }
//                override fun onFailure(call: Call<Document>, t: Throwable) {
//                    LogUtils.e("onFailure")
//                }
//            })
        HOkHttp.newHttpCompat().get(
            "https://www.23txt.com/search.php",
            HOkHttp.newParamsCompat("q", key),
            object : HOKNetCallback {
                override fun onSuccess(statusCode: String?, response: String?) {

                }

                override fun onFailure(statusCode: String?, errMsg: String?, response: String?) {

                }
            }
        )

    }
}