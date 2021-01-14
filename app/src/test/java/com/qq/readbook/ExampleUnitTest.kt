package com.qq.readbook

import com.google.gson.internal.LinkedTreeMap
import com.hqq.core.utils.GsonUtil
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {


        var str: String? = null

    var a=    "1aaab".trim(){
            it <= 'a'
        }

        print(a)
        print(str?.trim { it <= ' ' })
        print("".toString().trim { it <= ' ' })
        print(' '.toString().trim { it <= ' ' })


        var tt = "[{\"class\":\"library\"},{\"class\":\"library\"}]";

        var list = GsonUtil.fromJson(tt, ArrayList<String>().javaClass)


        print("")

    }
}