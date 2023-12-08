package com.lostandfoundapp

import android.content.Intent
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

        //receive documentID and itemName from previous activity
        val documentID = intent.getStringExtra("documentID")
        val itemName = intent.getStringExtra("itemName")

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.proceedButton.setOnClickListener {
            val intent = Intent(this, ClaimItemForm::class.java)
            //pass documentID and itemName to next activity
            intent.putExtra("documentID", documentID)
            intent.putExtra("itemName", itemName)
//            Toast.makeText(this, documentID + itemName, Toast.LENGTH_SHORT).show()
            startActivity(intent)

        }



    }


}