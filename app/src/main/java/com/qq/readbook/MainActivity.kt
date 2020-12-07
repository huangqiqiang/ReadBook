package com.qq.readbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.qq.readbook.repository.SearchRepository

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button1).setOnClickListener(){

            SearchRepository().search("宠魅")

        }

    }
}