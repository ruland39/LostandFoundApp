package com.lostandfoundapp

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import com.google.gson.Gson
import com.lostandfoundapp.databinding.ActivityReportLostItemFormBinding
import java.io.File
import java.io.FileOutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class ReportLostItemFormActivity : AppCompatActivity() {

    lateinit var binding: ActivityReportLostItemFormBinding
    private val PICK_IMAGES_REQUEST_CODE = 123
    private lateinit var photoUrl: String
    private var firstFragment: FirstFragment? = null
    private val timestamp = System.currentTimeMillis()

    // Format the timestamp into yyyy-MM-dd HH:mm:ss
    private val formattedTimestamp: String
        get() {
            val dateFormat = DateFormat.getDateTimeInstance()
            return dateFormat.format(Date(timestamp))
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportLostItemFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TODO: Add Dialog Layout
        MaterialAlertDialogBuilder(this)
            .setView(R.layout.dialog_report)
            .setPositiveButton("Got it!") { dialog, _ ->
                // Respond to positive button press
                dialog.dismiss()
            }
            .show()


        val defaultPhoto = ContextCompat.getDrawable(this, R.drawable.addphoto)

        binding.backButton.setOnClickListener {
            finish()
        }

        //TODO: Add Dialog Layout
        MaterialAlertDialogBuilder(this)
            .setView(R.layout.dialog_report)
            .setPositiveButton("Got it!") { dialog, _ ->
                // Respond to positive button press
                dialog.dismiss()

                if (!isNetworkConnected(this)) {
                    //no internet
                    MaterialAlertDialogBuilder(this)
                        .setTitle("No Internet Connection")
                        .setMessage("Item will be saved locally. Item will be uploaded once there is Internet Connection")
                        .setPositiveButton("Okay") { dialog, _ ->
                            // Respond to positive button press
                            dialog.dismiss()
                        }
                        .show()
                }

            }
            .show()


        //Variables Declaration
        val photo = binding.addPhoto
        val name = binding.name
        val category = binding.category
        val dateTime = binding.dateTime
        val location = binding.location
        val details = binding.details
        val submit = binding.submit

        // Initialize or find the FirstFragment
        firstFragment = supportFragmentManager.findFragmentByTag("FirstFragment") as? FirstFragment


        //PHOTO UPLOAD
        photo.setOnClickListener {

            val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")

            val builder = MaterialAlertDialogBuilder(this)
            builder.setTitle("Add Photo")
            builder.setItems(options) {
                dialog, which ->
                when(which) {
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


        //NAME
        name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    binding.name.error = "Please Enter Name"
                } else {
                    binding.name.error = null
                }
            }
        })


        // CATEGORY
        val spinnerTitle: Spinner = findViewById(R.id.category)
        val spinner: Spinner = findViewById(R.id.category)

        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter.createFromResource(
            this,
            R.array.category_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner.adapter = adapter
        }

        //Spinner Adapter
        class SpinnerActivity : Activity(), AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                // An item is selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos).
                spinnerTitle.visibility = if (pos == 0) View.VISIBLE else View.GONE
                spinner.onItemSelectedListener = this

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback.
            }
        }

        // DATE AND TIME
        dateTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this,
                { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                    val timePickerDialog = TimePickerDialog(
                        this,
                        { _: TimePicker, hourOfDay: Int, minute: Int ->
                            calendar.set(year, month, dayOfMonth, hourOfDay, minute)
                            val formattedDate =
                                SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(
                                    calendar.time
                                )
                            binding.dateTime.setText(formattedDate)
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        false
                    )
                    timePickerDialog.show()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        // LOCATION
        location.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    binding.location.error = "Please Enter Location"
                } else {
                    binding.location.error = null
                }
            }
        })

        //TODO: Use Google Maps API to get Location Coordinate

        // DETAILS
        details.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    binding.details.error = "Please Enter Details"
                } else {
                    binding.details.error = null
                }
            }
        })

        //TODO: Set Submit button to disabled, if all fields are filled, then enable button

        submit.isEnabled = false


        name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                submit.isEnabled = s.toString().isNotEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        binding.category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                submit.isEnabled = pos != 0
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback.
            }
        }

        binding.dateTime.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                submit.isEnabled = s.toString().isNotEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        binding.location.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                submit.isEnabled = s.toString().isNotEmpty()
            }
        })

        binding.details.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                submit.isEnabled = s.toString().isNotEmpty()
            }
        })


        // SUBMIT
        submit.setOnClickListener {

            //change to if photo is default image
            if (binding.addPhoto.drawable == defaultPhoto) {
                Toast.makeText(this, "Please Upload Photo", Toast.LENGTH_SHORT).show()

            } else if (name.text.toString().isEmpty()) {
                name.error = "Please Enter Name"
                Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show()

            } else if (category.selectedItem.toString() == resources.getString(R.string.category)) {
//                category.error = "Please Select Category"
                Toast.makeText(this, "Please Select Category", Toast.LENGTH_SHORT).show()

            } else if (dateTime.text.toString().isEmpty()) {
                dateTime.error = "Please Select Date and Time"
                Toast.makeText(this, "Please Select Date and Time", Toast.LENGTH_SHORT).show()


            } else if (location.text.toString().isEmpty()) {
                location.error = "Please Enter Location"
                Toast.makeText(this, "Please Enter Location", Toast.LENGTH_SHORT).show()


            } else if (details.text.toString().isEmpty()) {
                details.error = "Please Enter Details"
                Toast.makeText(this, "Please Enter Details", Toast.LENGTH_SHORT).show()


            } else {


                // Create a LostItem object
                val lostItem = LostItem(
                    //TODO: Change documentID to readeable format
                    documentID = "LostItem = $formattedTimestamp",
                    photoUrl = photoUrl,
                    name = binding.name.text.toString(),
                    category = binding.category.selectedItem.toString(),
                    dateTime = binding.dateTime.text.toString(),
                    location = binding.location.text.toString(),
                    details = binding.details.text.toString(),
                    isClaimed = false
                )

                // Convert LostItem to CardViewItem
                val cardViewItem = lostItem.toCardViewItem()

                // Call the addCard function in FirstFragment to add the new card
                val firstFragment = supportFragmentManager.fragments.firstOrNull { it is FirstFragment } as? FirstFragment
                firstFragment?.addCard(cardViewItem)


                // Save data locally
                saveDataLocally(lostItem)

                // Save data to Firebase
                saveDataToFirestore(lostItem)


                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
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



    //Photo Upload Function
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGES_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val imageUri = data?.data

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
        val lostItemsRef = storageRef.child("items/LostItem = $formattedTimestamp.jpg")

        lostItemsRef.putFile(imageUri)
            .addOnSuccessListener { _ ->
                lostItemsRef.downloadUrl
                    .addOnSuccessListener { downloadUri ->
                        photoUrl = downloadUri.toString()
                        Log.d("TAG", "onActivityResult: $downloadUri")

                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to get download URL", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Photo Upload Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveDataToFirestore(lostItem: LostItem) {
        //TODO: Fetch currentUser UID, Get the 'users' document with the UID, Get (email, idNumber, Name, phoneNumber) from the document, save it to the lostItem object as reporterDetails
        val db = FirebaseFirestore.getInstance()
        val currentUser = Firebase.auth.currentUser

        val userRef = db.collection("users").document(currentUser?.uid.toString())
        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val reporterDetails = hashMapOf(
                        "email" to document.getString("email"),
                        "idNumber" to document.getString("idNumber"),
                        "name" to document.getString("name"),
                        "phoneNumber" to document.getString("phoneNumber")
                    )
                    lostItem.reporterDetails = reporterDetails

                    // Create a new item
                    val item = hashMapOf(
                        "documentID" to lostItem.documentID,
                        "photoUrl" to lostItem.photoUrl,
                        "name" to lostItem.name,
                        "category" to lostItem.category,
                        "dateTime" to lostItem.dateTime,
                        "location" to lostItem.location,
                        "details" to lostItem.details,
                        "isClaimed" to lostItem.isClaimed,
                        "reporterDetails" to lostItem.reporterDetails
                    )

                    val documentID = lostItem.documentID
                    val itemsCollection = db.collection("items").document(documentID)


                    //check if there is internet connection
                    if (isNetworkConnected(this)) {
                        //yes intenet
                        // Add a new document with documentID
                        itemsCollection.set(item)
                            .addOnSuccessListener { documentReference ->
//                    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
//                    Toast.makeText(this, "Data saved to Firebase", Toast.LENGTH_SHORT).show()
                                Toast.makeText(
                                    this,
                                    "${lostItem.name} has been reported successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener { e ->
                                Log.w("TAG", "Error adding document", e)
                                Toast.makeText(
                                    this,
                                    "Failed to save data to Firebase",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        //no internet
                        saveDataLocally(lostItem)
                        Toast.makeText(
                            this,
                            "No Internet Connection. Item saved locally. Item will be uploaded once there is Internet Connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
    }

    // Save Data Locally
    private fun saveDataLocally(lostItem: LostItem) {
        val sharedPreferences = getSharedPreferences("LostItemPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(lostItem)
        sharedPreferences.edit().putString("lostItem", json).apply()
    }

}


    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

//DATAAAAAAAAAAAA

//TODO: Add isClaimed Boolean to check if item has been claimed or not, if claimed, disable claim button
data class LostItem(
    val documentID: String,
    val photoUrl: String, // URL or reference to the photo
    val name: String,
    val category: String,
    val dateTime: String,
    val location: String,
    val details: String,
    var isClaimed: Boolean = false
){
    lateinit var reporterDetails: HashMap<String, String?>

    fun toCardViewItem(): CardViewItem {
        return CardViewItem(
            documentID,
            photoUrl,
            name,
            category,
            dateTime,
            location,
            details,
            isClaimed
        )
    }

    fun markAsClaimed() {
        isClaimed = true
    }
}
