package com.lostandfoundapp

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lostandfoundapp.databinding.ActivityClaimItemFormBinding

class ClaimItemForm : AppCompatActivity() {
    private lateinit var binding: ActivityClaimItemFormBinding
    private lateinit var firebaseAuth: FirebaseAuth
    val PICK_IMAGES_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClaimItemFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val defaultPhoto = ContextCompat.getDrawable(this, R.drawable.addphoto)
        binding.submit.isEnabled = false

        //Declaration
        val idNumber = binding.idNumber
        val name = binding.name
        val email = binding.email
        val phoneNumber = binding.phoneNumber

        //fetch data from firebase and set the text
        //TODO: wrong data being fetched
//        val user = firebaseAuth.currentUser
//
//        idNumber.setText(user?.uid) //should be Id number not UID
//        name.setText(user?.displayName) //not being displayed
//        email.setText(user?.email) //correct
//        phoneNumber.setText(user?.phoneNumber) //not being displayed

        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("users")
        collectionRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    idNumber.setText(document.getString("idNumber"))
                    name.setText(document.getString("name"))
                    email.setText(document.getString("email"))
                    phoneNumber.setText(document.getString("phoneNumber"))
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting data", Toast.LENGTH_SHORT).show()
            }






        binding.backButton.setOnClickListener {
            finish()
        }

        binding.addPhoto.setOnClickListener {
            //open image gallery and can allow 1 photo
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGES_REQUEST_CODE)
        }

        if (binding.addPhoto.drawable == defaultPhoto){
            Toast.makeText(this, "Please Upload Photo", Toast.LENGTH_SHORT).show()
        }
        else{
            binding.submit.isEnabled = true
        }

        // SUBMIT
        binding.submit.setOnClickListener {
            Toast.makeText(this, "Item Claimed", Toast.LENGTH_SHORT).show()
                //TODO: Upload to Firebase
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGES_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            binding.addPhoto.setImageURI(imageUri)
        }

    }

    }

