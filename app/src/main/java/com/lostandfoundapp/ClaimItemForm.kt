package com.lostandfoundapp

import android.app.Activity
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
    private val PICK_IMAGES_REQUEST_CODE = 123
    private lateinit var photoUrl: String
    private lateinit var documentID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClaimItemFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val defaultPhoto = ContextCompat.getDrawable(this, R.drawable.addphoto)
        binding.submit.isEnabled = false

        // Declaration
        val idNumber = binding.idNumber
        val name = binding.name
        val email = binding.email
        val phoneNumber = binding.phoneNumber

        // Fetch data from Firebase and set the text
        fetchDataFromFirestore()

        // Fetch item details
        fetchItemDetailsFromFirestore()

        binding.backButton.setOnClickListener {
            finish()
        }

        // PHOTO UPLOAD
        binding.addPhoto.setOnClickListener {
            showPhotoOptions()
        }

        // SUBMIT
        binding.submit.setOnClickListener {
            fetchDataFromFirestore()
            saveDataToFirestore()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showPhotoOptions() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")

        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("Add Photo")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> takePhoto()
                1 -> chooseFromGallery()
                2 -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun takePhoto() {
        val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, PICK_IMAGES_REQUEST_CODE)
    }

    private fun chooseFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            PICK_IMAGES_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGES_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data

            binding.submit.isEnabled = true

            if (imageUri != null) {
                binding.addPhoto.setImageURI(imageUri)
                uploadPhotoToFirebase(imageUri)
            } else {
                val imageBitmap = data?.extras?.get("data") as Bitmap
                val imageUriFromBitmap = getImageUriFromBitmap(imageBitmap)
                binding.addPhoto.setImageURI(imageUriFromBitmap)
                uploadPhotoToFirebase(imageUriFromBitmap)
            }
        } else {
            Toast.makeText(this, "Image capture failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val imageFile = File(this.cacheDir, "temp_image.jpg")
        imageFile.createNewFile()

        val outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        return imageFile.toUri()
    }

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
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("users")
        val currentUserUid = firebaseAuth.currentUser?.uid

        collectionRef.document(currentUserUid!!)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val idNumberValue = document.getString("idNumber")
                    val nameValue = document.getString("name")
                    val emailValue = document.getString("email")
                    val phoneNumberValue = document.getString("phoneNumber")

                    // Set Text of User Claim Details
                    binding.idNumber.setText(idNumberValue)
                    binding.name.setText(nameValue)
                    binding.email.setText(emailValue)
                    binding.phoneNumber.setText(phoneNumberValue)
                } else {
                    Toast.makeText(this, "User document not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error getting user data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchItemDetailsFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        val itemRef = db.collection("items")

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
                Toast.makeText(this, "Error getting item data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveDataToFirestore() {
        val db = FirebaseFirestore.getInstance()
        val claimDetail = hashMapOf(
            "idNumber" to binding.idNumber.text.toString(),
            "name" to binding.name.text.toString(),
            "email" to binding.email.text.toString(),
            "phoneNumber" to binding.phoneNumber.text.toString(),
            "photoUrl" to photoUrl
        )

        db.collection("items")
            .document(documentID)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    db.collection("items")
                        .document(documentID)
                        .update(
                            mapOf(
                                "claimerDetails" to claimDetail,
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
                    Toast.makeText(
                        this,
                        "Document with ID $documentID does not exist. Creating a new document.",
                        Toast.LENGTH_SHORT
                    ).show()
                    db.collection("items")
                        .document(documentID)
                        .set(claimDetail)
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
                Toast.makeText(
                    this,
                    "Error checking document existence: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}