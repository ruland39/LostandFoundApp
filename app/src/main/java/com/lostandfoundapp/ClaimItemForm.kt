package com.lostandfoundapp

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import com.lostandfoundapp.databinding.ActivityClaimItemFormBinding
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class ClaimItemForm : AppCompatActivity() {
    private lateinit var binding: ActivityClaimItemFormBinding
    private lateinit var firebaseAuth: FirebaseAuth
    val PICK_IMAGES_REQUEST_CODE = 123
    private lateinit var photoUrl: String
    private lateinit var documentID: String


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
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("users")
        collectionRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {

                    //Set Text of User Claim Details
                    idNumber.setText(document.getString("idNumber"))
                    name.setText(document.getString("name"))
                    email.setText(document.getString("email"))
                    phoneNumber.setText(document.getString("phoneNumber"))
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error getting data", Toast.LENGTH_SHORT).show()
            }

        //fetch item details
        val database = FirebaseFirestore.getInstance()
        val itemRef = database.collection("items")
        itemRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    documentID = document.getString("documentID") ?: ""
                    val itemPhoto = document.getString("photoUrl")
                    val itemName = document.getString("name")
                    val itemCategory = document.getString("category")
                    val itemDateTime = document.getString("dateTime")
                    val itemLocation = document.getString("location")
                    val itemDetails = document.getString("details")
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error getting data", Toast.LENGTH_SHORT).show()
            }


        binding.backButton.setOnClickListener {
            finish()
        }

        //PHOTO UPLOAD
        binding.addPhoto.setOnClickListener {

            val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")

            val builder = MaterialAlertDialogBuilder(this)
            builder.setTitle("Add Photo")
            builder.setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
//                        Toast.makeText(this, "Take Photo", Toast.LENGTH_SHORT).show()
                        takePhoto()
                    }

                    1 -> {
//                        Toast.makeText(this, "Choose from Gallery", Toast.LENGTH_SHORT).show()
                        choosefromGallery()
                    }

                    2 -> {
                        dialog.dismiss()
                    }
                }
            }
            builder.show()
        }


        // SUBMIT
        binding.submit.setOnClickListener {
//            Toast.makeText(this, "Item Claimed", Toast.LENGTH_SHORT).show()


            fetchDataFromFirestore()
            saveDatatoFirestore()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun takePhoto() {
        val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, PICK_IMAGES_REQUEST_CODE)
    }

    private fun choosefromGallery() {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            PICK_IMAGES_REQUEST_CODE
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGES_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val imageUri = data?.data

                binding.submit.isEnabled = true

                if (imageUri != null) {
                    binding.addPhoto.setImageURI(imageUri)
                    uploadPhotoToFirebase(imageUri)
                } else {
                    // If data is null, it means the image was captured using the camera
                    // You can access the captured image using the extras from the intent
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    val imageUriFromBitmap = getImageUriFromBitmap(imageBitmap)
                    binding.addPhoto.setImageURI(imageUriFromBitmap)
                    uploadPhotoToFirebase(imageUriFromBitmap)
                }
            } else {
                Toast.makeText(this, "Image capture failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Helper method to convert Bitmap to Uri
    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val imageFile = File(this.cacheDir, "temp_image.jpg")
        imageFile.createNewFile()

        val outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        return imageFile.toUri()
    }

    // Helper method to upload photo to Firebase
    private fun uploadPhotoToFirebase(imageUri: Uri) {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val lostItemsRef = storageRef.child("items/${UUID.randomUUID()}.jpg")

        lostItemsRef.putFile(imageUri)
            .addOnSuccessListener { _ ->
                lostItemsRef.downloadUrl
                    .addOnSuccessListener { downloadUri ->
                        photoUrl = downloadUri.toString()
                        Log.d("TAG", "onActivityResult: $downloadUri")
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to get download URL", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Photo Upload Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchDataFromFirestore() {

        //Declaration
        val idNumber = binding.idNumber
        val name = binding.name
        val email = binding.email
        val phoneNumber = binding.phoneNumber

        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("users")


        collectionRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    document.getString("documentID")
                    idNumber.setText(document.getString("idNumber"))
                    name.setText(document.getString("name"))
                    email.setText(document.getString("email"))
                    phoneNumber.setText(document.getString("phoneNumber"))
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error getting data", Toast.LENGTH_SHORT).show()
            }

    }


    private fun saveDatatoFirestore() {
        val db = FirebaseFirestore.getInstance()

        //TODO: if documentID is equal, add claimDetails (the one on top) to the existing items document

        //check if documentID is equal to the one in the database, if true then add claimDetails to the existing items document
        //if false then create a new document with the claimDetails

        val claimDetail = hashMapOf(
            "idNumber" to binding.idNumber.text.toString(),
            "name" to binding.name.text.toString(),
            "email" to binding.email.text.toString(),
            "phoneNumber" to binding.phoneNumber.text.toString(),
            "photoUrl" to photoUrl
        )

        // Check if the document with the given documentID already exists
        db.collection("items")
            .document(documentID)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Document with the given documentID exists, update the existing document
                    db.collection("items")
                        .document(documentID)
                        .update(
                            mapOf(
                                "claimDetails" to claimDetail,
                                "isClaimed" to true
                            )
                        )
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Item Claimed Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                this,
                                "Failed to update claim detail",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    // Document with the given documentID does not exist, create a new document
                    Toast.makeText(
                        this,
                        "Document with ID $documentID does not exist. Creating a new document.",
                        Toast.LENGTH_SHORT
                    ).show()
                    db.collection("items")
                        .document(documentID)
                        .set(claimDetail) // Use set() instead of add() for an existing document
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Claim Detail Saved to Firebase",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                // Handle errors
                Toast.makeText(
                    this,
                    "Error checking document existence: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }


    }
}


