package com.lostandfoundapp

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
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

        //Variables Declaration
        val photo = binding.addPhoto
        val name = binding.name
        val category = binding.category
        val dateTime = binding.dateTime
        val location = binding.location
        val details = binding.details
        val submit = binding.submit

        //PHOTO UPLOAD
        binding.addPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGES_REQUEST_CODE)
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

                val spinner: Spinner = findViewById(R.id.category)
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
                            val formattedDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(calendar.time)
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

        binding.submit.isEnabled = false


        binding.name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.submit.isEnabled = s.toString().isNotEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        binding.category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                binding.submit.isEnabled = pos != 0
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback.
            }
        }

        binding.dateTime.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.submit.isEnabled = s.toString().isNotEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        binding.location.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.submit.isEnabled = s.toString().isNotEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        binding.details.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.submit.isEnabled = s.toString().isNotEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })


        // SUBMIT
        binding.submit.setOnClickListener {

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

}