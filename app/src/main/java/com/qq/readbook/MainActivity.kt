package com.qq.readbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.qq.readbook.repository.SearchRepository
import com.qq.readbook.ui.SearchActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.button1).setOnClickListener() {
            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
        }
    }
}