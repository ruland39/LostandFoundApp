package com.lostandfoundapp

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
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
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import com.lostandfoundapp.databinding.ActivityReportLostItemFormBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReportLostItemFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportLostItemFormBinding
    private val PICK_IMAGES_REQUEST_CODE = 123


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportLostItemFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val defaultPhoto = ContextCompat.getDrawable(this, R.drawable.addphoto)

        binding.backButton.setOnClickListener {
            finish()
        }


        //Firestore
        val db = FirebaseFirestore.getInstance()

        //Variables Declaration
        val photo = binding.addPhoto
        val name = binding.name
        val category = binding.category
        val dateTime = binding.dateTime
        val location = binding.location
        val details = binding.details
        val submit = binding.submit

        //PHOTO UPLOAD
        photo.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGES_REQUEST_CODE
            )
        }


        //NAME
        binding.name.addTextChangedListener(object : TextWatcher {
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
        // Create an ArrayAdapter using the string array and a default spinner layout
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
        binding.dateTime.setOnClickListener {
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
        binding.location.addTextChangedListener(object : TextWatcher {
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
        binding.details.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    binding.details.error = "Please Enter Details"
                } else {
                    binding.details.error = null
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
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
            override fun afterTextChanged(s: Editable?) {
                submit.isEnabled = s.toString().isNotEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        binding.details.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                submit.isEnabled = s.toString().isNotEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
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

            } else if (category.selectedItem.toString().isEmpty()) {
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
                //TODO: Upload to Firebase

                // Assuming you have variables for other fields (name, category, etc.)
                val lostItem = LostItem(
                    photoUrl = binding.addPhoto.toString(), // Replace with the actual URL or reference
                    name = binding.name.text.toString(),
                    category = binding.category.selectedItem.toString(),
                    dateTime = binding.dateTime.text.toString(),
                    location = binding.location.text.toString(),
                    details = binding.details.text.toString()
                )

                // Create a new item
                val item = hashMapOf(
                    "photoUrl" to lostItem.photoUrl,
                    "name" to lostItem.name,
                    "category" to lostItem.category,
                    "dateTime" to lostItem.dateTime,
                    "location" to lostItem.location,
                    "details" to lostItem.details
                )

                // Add a new document with a generated ID
                db.collection("items")
                    .add(item)
                    .addOnSuccessListener { documentReference ->
                        Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w("TAG", "Error adding document", e)
                    }
                Toast.makeText(this, "Item Reported", Toast.LENGTH_SHORT).show()



                // Save data locally
//                saveDataLocally(lostItem)

                // Save data to Firebase
//                saveDataToFirebase(lostItem)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

    }

    //Photo Upload Function
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGES_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            binding.addPhoto.setImageURI(imageUri)
        }

    }

    //DATAAAAAAAAAAAA

    data class LostItem(
        val photoUrl: String, // URL or reference to the photo
        val name: String,
        val category: String,
        val dateTime: String,
        val location: String,
        val details: String
    )


    // Save data locally
//    private fun saveDataLocally(lostItem: LostItem) {
//        val sharedPreferences = getSharedPreferences("LostItemPrefs", Context.MODE_PRIVATE)
//        val gson = Gson()
//        val json = gson.toJson(lostItem)
//        sharedPreferences.edit().putString("lostItem", json).apply()
//    }

    // Save data to Firebase
//    private fun saveDataToFirebase(lostItem: LostItem) {
//        val database = FirebaseApp.getInstance().database
//        val lostItemsRef = database.getReference("lostItems")
//
//        // Generate a unique key for the lost item
//        val lostItemId = lostItemsRef.push().key
//
//        // Save the lost item to Firebase
//        if (lostItemId != null) {
//            lostItemsRef.child(lostItemId).setValue(lostItem)
//                .addOnSuccessListener {
//                    // Data successfully saved to Firebase
//                    Toast.makeText(this, "Data saved to Firebase", Toast.LENGTH_SHORT).show()
//                }
//                .addOnFailureListener {
//                    // Handle failure
//                    Toast.makeText(this, "Failed to save data to Firebase", Toast.LENGTH_SHORT).show()
//                }
//        }
//    }




}