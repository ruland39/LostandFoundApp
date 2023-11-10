package com.lostandfoundapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.lostandfoundapp.databinding.ActivityClaimProceedBinding

class ClaimProceedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClaimProceedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimProceedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.proceedButton.setOnClickListener {
            Toast.makeText(this, "Claim Successful", Toast.LENGTH_SHORT).show()
        }



    }


}