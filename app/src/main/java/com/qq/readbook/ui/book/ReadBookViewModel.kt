package com.qq.readbook.ui.book

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.hqq.core.ui.base.BaseViewModel

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.ui.book
 * @Date : 下午 2:27
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
class ReadBookViewModel : BaseViewModel() {
    /**
     *   是否点击亮度
     */
    var showLight = MutableLiveData<Boolean>(false)
    var showCache = MutableLiveData<Boolean>(false)


    /**
     *  点击亮度
     */
    fun onClickLight(view: View) {
        showLight.value = !(showLight.value as Boolean)
    }

    fun onCache(view: View) {
        showCache.value = !(showCache.value as Boolean)
    }

    /**
     *  返回
     */
    fun onBack(view: View) {
        finish()
    }

    /**
     *  按钮空白位置
     */
    fun onLayoutMenu(view: View) {

    }
}