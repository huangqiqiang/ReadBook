package com.qq.readbook.net

import com.qq.readbook.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.net
 * @Date : 上午 10:25
 * @Email : qiqiang213@gmail.com
 * @Descrive :
 */
object ApiManager {
    /**
     * 默认超时时间(毫秒)
     */
    const val DEFAULT_TIMEOUT: Long = 900000

    private const val mWangAndroidUrl = "https://www.23txt.com/"

   private var api23Txt: Api23Txt? = null

    fun getApi23TxtInterface(): Api23Txt {
        if (api23Txt == null) {
            //设置出现错误进行重新连接
            val okHttpClient = OkHttpClient.Builder()
            okHttpClient.retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            val logInterceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                //打印拦截器
                logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                okHttpClient.addInterceptor(logInterceptor)
            } else {
                logInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            api23Txt = Retrofit.Builder()
                .client(okHttpClient.build())
                .addConverterFactory(JsoupConverterFactory)
                .baseUrl(mWangAndroidUrl)
                .build().create(Api23Txt::class.java)

        }
        return api23Txt!!
    }

}