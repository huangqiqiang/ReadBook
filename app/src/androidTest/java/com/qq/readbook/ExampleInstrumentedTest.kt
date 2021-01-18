package com.qq.readbook

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.qq.readbook.bean.ReadSource
import com.qq.readbook.repository.SearchBookRepository
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
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
        assertEquals("com.qq.readbook", appContext.packageName)
        var sourceList=ArrayList<ReadSource>()
        try {
            val inputStream = appContext.assets.open("test.json")
            val inputStreamReader = InputStreamReader(inputStream)
            val jsonReader = JsonReader(inputStreamReader)
            sourceList = Gson().fromJson<ArrayList<ReadSource>>(
                jsonReader,
                object : TypeToken<ArrayList<ReadSource>>() {}.type
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val `is`: InputStream = appContext.getAssets().open("test.html")
        val lenght: Int = `is`.available()
        val buffer = ByteArray(lenght)
        `is`.read(buffer)
        val result = String(buffer)



        SearchBookRepository.doReadBookList(result,sourceList[0])
    }




}