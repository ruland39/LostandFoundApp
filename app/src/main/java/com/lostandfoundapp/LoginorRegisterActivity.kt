package com.lostandfoundapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginorRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginor_register)

        val loginButton = findViewById<Button>(R.id.login_button)
        val registerButton = findViewById<Button>(R.id.register_button)

        loginButton.setOnClickListener {
            openLoginPage()
        }

        registerButton.setOnClickListener {
            openRegisterPage()
        }

    }

    fun openLoginPage(){
        //TODO: Open Login Page
    }

    fun openRegisterPage(){
        //TODO: Open Register Page
    }

}