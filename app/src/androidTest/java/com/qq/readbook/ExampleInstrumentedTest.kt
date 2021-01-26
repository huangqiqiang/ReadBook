package com.qq.readbook

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.qq.readbook.repository.SearchBookRepository
import com.qq.readbook.repository.read.JsoupUtils
import org.jsoup.Jsoup
import org.junit.Test
import org.junit.runner.RunWith
import java.io.InputStream

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
        val inputStream: InputStream = appContext.getAssets().open("test.html")
        val lenght: Int = inputStream.available()
        val buffer = ByteArray(lenght)
        inputStream.read(buffer)
        val result = String(buffer)
        val doc = Jsoup.parse(result)
        val json =JsoupUtils.getJsonElement("{\"ruleSearchList\":[{\"elementType\":\"class\",\"elementValue\":\"library\",\"ruleChild\":{\"elementType\":\"tag\",\"elementValue\":\"li\"}}],\"ruleBookName\":[{\"elementType\":\"class\",\"elementValue\":\"bookname\",\"attrValue\":\"text\"}],\"ruleDesc\":[{\"elementType\":\"class\",\"elementValue\":\"intro\",\"attrValue\":\"text\"}],\"ruleAuthor\":[{\"elementType\":\"class\",\"elementValue\":\"author\",\"attrValue\":\"text\"}],\"newestChapterTitle\":[{\"elementType\":\"class\",\"elementValue\":\"chapter\",\"attrValue\":\"text\"}],\"chapterUrl\":[{\"elementType\":\"class\",\"elementValue\":\"bookimg\",\"attrValue\":\"href\",\"formatRule\":{\"type\":\"replaceEnd\",\"value\":\".html\",\"formatRule\":{\"type\":\"addStart\",\"value\":\"https://www.dstiejuan.com\"}}}],\"ruleImg\":[{\"elementType\":\"tag\",\"elementValue\":\"img\",\"attrValue\":\"src\",\"position\":0}],\"ruleType\":{\"elementType\":\"tag\",\"elementValue\":\"p\",\"ruleChild\":{\"elementType\":\"tag\",\"elementValue\":\"a\",\"position\":1}}}")

    //        SearchBookRepository.doReadBookList4Source(json, result)

    }


}