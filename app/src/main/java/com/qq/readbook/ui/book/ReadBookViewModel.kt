package com.qq.readbook.ui.book

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.hqq.core.ui.base.BaseViewModel
import com.qq.readbook.Keys

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

    /**
     *显示缓存
     */
    var showCache = MutableLiveData<Boolean>(false)

    /**
     *  点击亮度
     */
    fun onClickLight(view: View) {
        showLight.value = !(showLight.value as Boolean)
    }


    /**
     *  是否是日间模式
     */
    val themeMode = MutableLiveData<Boolean>(true)

    /**
     * 是否显示缓存
     * @param view View
     */
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
     * 切换主题
     * @param view View
     */
    fun onThemeMode(view: View) {
        themeMode.value = !(themeMode.value as Boolean)
    }

    /**
     *  按钮空白位置
     */
    fun onLayoutMenu(view: View) {

    }


}