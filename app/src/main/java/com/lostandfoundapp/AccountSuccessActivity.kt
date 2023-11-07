package com.lostandfoundapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AccountSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_success)

        val button = findViewById<Button>(R.id.next_button)

        button.setOnClickListener(){
            val intent = intent
            intent.setClass(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}