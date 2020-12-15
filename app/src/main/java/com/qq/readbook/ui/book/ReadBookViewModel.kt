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
     *
     */
    var showLight = MutableLiveData<Boolean>(false)


    fun onClickLight(view: View) {
        showLight.value = !(showLight.value as Boolean)

    }

}