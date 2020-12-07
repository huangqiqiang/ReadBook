package com.qq.readbook.net

import org.jsoup.nodes.Document
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap
import java.util.*

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.net
 * @Date : 上午 10:32
 * @Email : qiqiang213@gmail.com
 * @Descrive :
 */
interface Api23Txt {

    @GET("search.php")
    fun getSearchList(@Query("q") key:String): Call<Document>

}