package com.lostandfoundapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.lostandfoundapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.registerButton.setOnClickListener {
            val idNumber = binding.idNumber.text.toString()
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val phoneNumber = binding.phoneNumber.text.toString()
            val password = binding.password.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()

            binding.registerButton.isEnabled = false

            if(idNumber.isNotEmpty() && name.isNotEmpty() && email.isNotEmpty() && phoneNumber.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){

                showProgressBar()

                if(checkEmail(email)){
                    if(checkPassword(password, confirmPassword)){
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this) { task ->

                                handler.postDelayed({
                                    hideProgressBar()
                                }, 1000)

                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show()
                                    val intent = intent
                                    intent.setClass(this, AccountSuccessActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                                    Toast.makeText(this, "Register Failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                    else{
                        Toast.makeText(this, "Password and Confirm Password are not the same", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this, "Email is not valid", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "Please fill up the fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backButton.setOnClickListener() {
            val intent = intent
            intent.setClass(this, LoginorRegisterActivity::class.java)
            startActivity(intent)
        }

        binding.registerButton.isEnabled = false

        binding.idNumber.addTextChangedListener(textWatcher)
        binding.name.addTextChangedListener(textWatcher)
        binding.email.addTextChangedListener(textWatcher)
        binding.phoneNumber.addTextChangedListener(textWatcher)
        binding.password.addTextChangedListener(textWatcher)
        binding.confirmPassword.addTextChangedListener(textWatcher)

    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val email = findViewById<EditText>(R.id.email)
            val password = findViewById<EditText>(R.id.password)

            binding.registerButton.isEnabled = email.text.isNotEmpty() && password.text.isNotEmpty()
        }

        override fun afterTextChanged(s: Editable?) {

        }
    }

    //Progress Circular
    private fun showProgressBar(){
        binding.progressBarContainer.visibility = ProgressBar.VISIBLE
        binding.progressBar.showAnimationBehavior = CircularProgressIndicator.SHOW_INWARD
    }

    private fun hideProgressBar(){
        binding.progressBarContainer.visibility = ProgressBar.INVISIBLE
        binding.progressBar.visibility = ProgressBar.INVISIBLE
    }

    //Input Validation Checking
    //Function to check if the email is valid
    private fun checkEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    //Function to check if the password and confirm password are the same
    private fun checkPassword(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

}