package com.qq.readbook

import android.app.Application
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.qq.readbook.bean.ReadSource
import com.qq.readbook.weight.page.SpUtil
import java.io.IOException
import java.io.InputStreamReader
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook
 * @Date : 上午 11:04
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
class App : Application() {


    override fun onCreate() {
        super.onCreate()
        SpUtil.init(this)
        handleSSLHandshake()
        try {
            val inputStream = applicationContext.assets.open("1.json")
            val inputStreamReader = InputStreamReader(inputStream)
            val jsonReader = JsonReader(inputStreamReader)
            sourceList = Gson().fromJson<ArrayList<ReadSource>>(
                jsonReader,
                object : TypeToken<ArrayList<ReadSource>>() {}.type
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        var sourceList : ArrayList<ReadSource> ?=null

        fun handleSSLHandshake() {
            val TAG = "trustAllHosts"
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }

                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                    Log.i(TAG, "checkClientTrusted")
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                    Log.i(TAG, "checkServerTrusted")
                }
            })

            // Install the all-trusting trust manager
            try {
                val sc = SSLContext.getInstance("TLS")
                sc.init(null, trustAllCerts, SecureRandom())
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
                HttpsURLConnection.setDefaultHostnameVerifier { hostname, session -> true }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}