package com.qq.readbook.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.qq.readbook.R
import com.qq.readbook.ui.book.ReadBookActivity
import com.qq.readbook.weight.page.SpUtil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SpUtil.init(this)
        findViewById<Button>(R.id.button1).setOnClickListener() {
           // startActivity(Intent(this@MainActivity, SearchActivity::class.java))
            ReadBookActivity.open(this)

        }

    }
}