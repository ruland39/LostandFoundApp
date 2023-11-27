package com.lostandfoundapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.lostandfoundapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityLoginBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()



        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){

                //TODO: Circular Progress Bar not running
                showProgressBar()

                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->

                        handler.postDelayed({
                            hideProgressBar()
                        }, 1000)

                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                            val intent = intent
                            intent.setClass(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                        }
                    }

            }
            else{
                Toast.makeText(this, "Please fill up the fields", Toast.LENGTH_SHORT).show()
            }
        }


        binding.backButton.setOnClickListener{
            finish()
        }

        binding.loginButton.isEnabled = false
        binding.email.addTextChangedListener(textWatcher)
        binding.password.addTextChangedListener(textWatcher)


    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            val email = findViewById<EditText>(R.id.email)
            val password = findViewById<EditText>(R.id.password)

            binding.loginButton.isEnabled = email.text.isNotEmpty() && password.text.isNotEmpty()
        }

        override fun afterTextChanged(s: Editable?) {

        }
    }

    //Progress Circular
    private fun showProgressBar(){
        binding.progressBarContainer.visibility = ProgressBar.VISIBLE
        binding.progressBar.visibility = ProgressBar.VISIBLE
//        binding.progressBarContainer.visibility = ProgressBar.VISIBLE
//        binding.progressBar.showAnimationBehavior = CircularProgressIndicator.SHOW_INWARD
    }

    private fun hideProgressBar(){
        binding.progressBarContainer.visibility = ProgressBar.GONE
        binding.progressBar.visibility = ProgressBar.GONE
    }

    //Input Validation Checking
    private fun checkEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    }
    private fun checkPassword(password: String): Boolean {
        return if(password.length < 8){
            Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
            false
        } else{
            true
        }
    }


    //TODO: Option to login using phone number

}