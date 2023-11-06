package com.lostandfoundapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class FeatureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feature)

        //Declare Variables
        val featureText = findViewById<TextView>(R.id.feature_text)
        val featureImage = findViewById<ImageView>(R.id.feature_image)
        val button = findViewById<Button>(R.id.next_button)

        //Set the text and image
        featureText.text = "Lost your item somewhere?"
        featureImage.setImageResource(R.drawable.feature_one)

        button.setOnClickListener {

            when(featureText.text){
                "Lost your item somewhere?" -> {
                    featureText.text = "Don't know where to search?"
                    featureImage.setImageResource(R.drawable.feature_two)
                }
                "Don't know where to search?" -> {
                    featureText.text = "Don't worry we got you covered"
                    featureImage.setImageResource(R.drawable.feature_three)
                }
                "Don't worry we got you covered" -> {
                    Toast.makeText(this, "BRUH", Toast.LENGTH_SHORT).show()
                    val i = intent
                    i.setClass(this, LoginorRegisterActivity::class.java)
                }
            }


        }


    }

}