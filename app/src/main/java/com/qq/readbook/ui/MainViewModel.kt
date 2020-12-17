package com.qq.readbook.ui

import android.os.Bundle
import com.hqq.core.CoreConfig
import com.hqq.core.ui.list.BaseListViewModel
import com.qq.readbook.utils.room.RoomUtils

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.ui
 * @Date : 上午 11:23
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
class MainViewModel : BaseListViewModel() {

    override fun initData(extras: Bundle?) {
        super.initData(extras)

        var list = RoomUtils.getDataBase().bookDao().getAll()
        setData(list)
    }

    override fun onLoadMore() {
        super.onLoadMore()
        var list = RoomUtils.getDataBase().bookDao().getAll()
        setData(list)

    }

}