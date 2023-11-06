package com.lostandfoundapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val idNumber = findViewById<EditText>(R.id.id_number)
        val name = findViewById<EditText>(R.id.name)
        val email = findViewById<EditText>(R.id.email)
        val phoneNumber = findViewById<EditText>(R.id.phone_number)
        val password = findViewById<EditText>(R.id.password)
        val confirmPassword = findViewById<EditText>(R.id.confirm_password)
        val registerButton = findViewById<Button>(R.id.register_button)

        val backButton = findViewById<androidx.appcompat.widget.AppCompatImageButton>(R.id.back_button)

        backButton.setOnClickListener() {
            val intent = intent
            intent.setClass(this, LoginorRegisterActivity::class.java)
            startActivity(intent)
        }

        registerButton.isEnabled = false

        idNumber.addTextChangedListener(textWatcher)
        name.addTextChangedListener(textWatcher)
        email.addTextChangedListener(textWatcher)
        phoneNumber.addTextChangedListener(textWatcher)
        password.addTextChangedListener(textWatcher)
        confirmPassword.addTextChangedListener(textWatcher)

        registerButton.setOnClickListener {
            Toast.makeText(this, "Register Button Clicked", Toast.LENGTH_SHORT).show()
            val intent = intent
            intent.setClass(this, MainActivity::class.java)
            startActivity(intent)
        }


    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val idNumber = findViewById<EditText>(R.id.id_number)
            val name = findViewById<EditText>(R.id.name)
            val email = findViewById<EditText>(R.id.email)
            val phoneNumber = findViewById<EditText>(R.id.phone_number)
            val password = findViewById<EditText>(R.id.password)
            val confirmPassword = findViewById<EditText>(R.id.confirm_password)
            val registerButton = findViewById<Button>(R.id.register_button)

            registerButton.isEnabled = email.text.isNotEmpty() && password.text.isNotEmpty()
        }

        override fun afterTextChanged(s: Editable?) {

        }
    }
}