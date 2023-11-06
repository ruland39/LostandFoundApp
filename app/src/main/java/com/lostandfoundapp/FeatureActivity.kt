package com.lostandfoundapp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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

        //Animation
        val textFadeOut = ObjectAnimator.ofFloat(featureText, "alpha", 1f, 0f)
        val imageFadeOut = ObjectAnimator.ofFloat(featureImage, "alpha", 1f, 0f)

        val textFadeIn = ObjectAnimator.ofFloat(featureText, "alpha", 0f, 1f)
        val imageFadeIn = ObjectAnimator.ofFloat(featureImage, "alpha", 0f, 1f)

        val fadeInOut = AnimatorSet()
        fadeInOut.playTogether(textFadeOut, imageFadeOut, textFadeIn, imageFadeIn)
        fadeInOut.duration = 500


        button.setOnClickListener {

            when(featureText.text){
                "Lost your item somewhere?" -> {
                    fadeInOut.start()
                    featureText.text = "Don't know where to search?"
                    featureImage.setImageResource(R.drawable.feature_two)
                }
                "Don't know where to search?" -> {
                    fadeInOut.start()
                    featureText.text = "Don't worry we got you covered"
                    featureImage.setImageResource(R.drawable.feature_three)
                    button.text = "Get Started"
                }
                "Don't worry we got you covered" -> {
                    val intent = intent
                    intent.setClass(this, LoginorRegisterActivity::class.java)
                    startActivity(intent)
                }
            }


        }


    }

}