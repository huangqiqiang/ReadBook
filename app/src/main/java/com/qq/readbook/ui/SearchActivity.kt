package com.qq.readbook.ui

import com.hqq.core.toolbar.BaseToolBarSearch
import com.hqq.core.ui.base.BaseActivity
import com.qq.readbook.R
import com.qq.readbook.repository.SearchRepository

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.ui
 * @Date  : 下午 2:24
 * @Email : qiqiang213@gmail.com
 * @Descrive :
 */
class SearchActivity : BaseActivity() {
    override val layoutViewId: Int = R.layout.activity_search
    override fun initConfig() {
        super.initConfig()
        iCreateRootView.iRootViewImpl.iCreateToolbar = BaseToolBarSearch()

    }

    override fun initView() {
        (iCreateRootView.iRootViewImpl.iCreateToolbar as BaseToolBarSearch).setRightTextView() {
            var key =
                (iCreateRootView.iRootViewImpl.iCreateToolbar as BaseToolBarSearch).searchView.text.toString()
            SearchRepository().search(key)

        }
    }
}