package com.qq.readbook.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.hqq.core.ui.base.BaseActivity
import com.qq.readbook.R
import com.qq.readbook.ui.book.ReadBookActivity
import com.qq.readbook.weight.page.SpUtil

class MainActivity : BaseActivity() {
    override val layoutViewId: Int=R.layout.activity_main


    override fun initConfig() {
        super.initConfig()
        iCreateRootView.iRootViewImpl.iToolBarBuilder.showToolBar=false

    }

    override fun initView() {
        findViewById<Button>(R.id.button1).setOnClickListener() {
            // startActivity(Intent(this@MainActivity, SearchActivity::class.java))
            ReadBookActivity.open(this)

        }
    }


}