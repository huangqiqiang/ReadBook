package com.qq.readbook

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.hqq.core.utils.GsonUtil
import com.qq.readbook.bean.ReadSource
import com.qq.readbook.bean.RuleSearchBean
import com.qq.readbook.repository.read.JsoupUtils
import org.jsoup.Jsoup
import org.junit.Test
import org.junit.runner.RunWith
import org.seimicrawler.xpath.JXDocument
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val inputStream: InputStream = appContext.getAssets().open("testSearch.html")
        val lenght: Int = inputStream.available()
        val buffer = ByteArray(lenght)
        inputStream.read(buffer)
        val result = String(buffer)
        val doc = Jsoup.parse(result)

        val inputStream2 = appContext.assets.open("testSearch.json")
        val inputStreamReader = InputStreamReader(inputStream2)
        val jsonReader = JsonReader(inputStreamReader)
        var sourceList = Gson().fromJson<ReadSource>(
            jsonReader,
            object : TypeToken<ReadSource>() {}.type
        )
        val searchRuleBean = GsonUtil.fromJson<RuleSearchBean>(sourceList.ruleSearch, RuleSearchBean::class.java)
        println("---开始------")
        var list = JXDocument.create(doc).selN(searchRuleBean?.ruleSearchList)
        for (jxNode in list) {
            println("---开始单个书籍------")
            println(jxNode.toString())
            searchRuleBean?.apply {
                println("   ")
                println("   ")
                println("ruleBookName:")
                println(ruleBookName)
                println(JsoupUtils.getValue4key(jxNode, ruleBookName))
                println("   ")
                println("ruleAuthor:")
                println(ruleAuthor)
                println(JsoupUtils.getValue4key(jxNode, ruleAuthor))
                println("   ")
                println("ruleImg:")
                println(ruleImg)
                println(JsoupUtils.getValue4key(jxNode, ruleImg))
                println("   ")
                println("ruleDesc:")
                println(ruleDesc)
                println(JsoupUtils.getValue4key(jxNode, ruleDesc))
                println("   ")
                println("ruleChapterUrl:")
                println(ruleChapterUrl)
                println(JsoupUtils.getValue4key(jxNode, ruleChapterUrl))
                println("   ")
                println("ruleBookDetailUrl:")
                println(ruleBookDetailUrl)
                println(JsoupUtils.getValue4key(jxNode, ruleBookDetailUrl))
                println("   ")
                println("ruleNewestChapterTitle:")
                println(ruleNewestChapterTitle)
                println(JsoupUtils.getValue4key(jxNode, ruleNewestChapterTitle))
                println("   ")
                println("ruleType:")
                println(ruleType)
                println(JsoupUtils.getValue4key(jxNode, ruleType))
                println("   ")
                println("ruleWordCount:")
                println(ruleWordCount)
                println(JsoupUtils.getValue4key(jxNode, ruleWordCount))
                println("   ")
                println("ruleUpdateData:")
                println(ruleUpdateData)
                println(JsoupUtils.getValue4key(jxNode, ruleUpdateData))
                println("   ")
            }

            println("---结束单个书籍------")

        }

        println("---结束------")

    }


}