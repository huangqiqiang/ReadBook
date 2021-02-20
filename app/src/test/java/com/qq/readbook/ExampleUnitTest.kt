package com.qq.readbook

import android.R.array
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.hqq.core.utils.GsonUtil
import org.junit.Assert.*
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {


        var str: String? = null

        var a = "1aaab".trim() {
            it <= 'a'
        }

        print(a)
        print(str?.trim { it <= ' ' })
        print("".toString().trim { it <= ' ' })
        print(' '.toString().trim { it <= ' ' })


        var tt = "{\"tt\":[{\"class\":\"library\"},{\"class\":\"library\"}]}";
        var t1= "{key:{\"class\":\"library\"},{\"class\":\"library\"}}";
        var list = GsonUtil.fromJson(tt, JsonObject::class.java)
        print("")

    }


}