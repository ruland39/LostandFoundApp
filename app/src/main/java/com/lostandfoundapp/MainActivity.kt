package com.lostandfoundapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.lostandfoundapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val CAMERA_PERMISSION_CODE = 123



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        //Check if user is logged in
        if(firebaseAuth.currentUser == null){
            val intent = Intent(this, FeatureActivity::class.java)
            startActivity(intent)
            finish()
        }


        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)


        //ask for camera permission
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }


        binding.fab.setOnLongClickListener {

            Snackbar.make(it, "Report Lost Item", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            true
        }

        binding.fab.setOnClickListener {

            val intent = Intent(this, ReportLostItemFormActivity::class.java)
            startActivity(intent)

        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_help -> {

                val dialogView = layoutInflater.inflate(R.layout.dialog_help, null)

                MaterialAlertDialogBuilder(this)
                    .setView(dialogView)
                    .show()

                //button declaration
                val emailSecurity = dialogView.findViewById<TextView>(R.id.security_email)
                val phoneSecurity = dialogView.findViewById<TextView>(R.id.security_phone_number)
                val locationSecurity = dialogView.findViewById<TextView>(R.id.security_location)


                //mail to
                emailSecurity.setOnClickListener {
                    val emailIntent = Intent(Intent.ACTION_SENDTO)
                    emailIntent.data = Uri.parse("mailto:" + Uri.encode(resources.getString(R.string.security_nottingham_edu_my)))

                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "This is the body of the email")

                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send email"))
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(this, "No email app installed", Toast.LENGTH_SHORT).show()
                    }
                }


                //open dialer
                phoneSecurity.setOnClickListener {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:" + resources.getString(R.string._603_8924_8065))
                    startActivity(intent)
                }

                //open map
                locationSecurity.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("geo:0,0?q=" + Uri.encode(resources.getString(R.string.security_location)))
                    startActivity(intent)
                }

                true


            }

            R.id.action_about -> {

                MaterialAlertDialogBuilder(this)
                    .setView(R.layout.dialog_about)
                    .show()

                true
            }

            R.id.action_settings -> {
                // Toggle between dark mode and light mode
                val currentNightMode = AppCompatDelegate.getDefaultNightMode()
                val newNightMode = if (currentNightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                    // Switch to light mode
                    AppCompatDelegate.MODE_NIGHT_NO
                } else {
                    // Switch to dark mode
                    AppCompatDelegate.MODE_NIGHT_YES
                }

                // Apply the new night mode
                AppCompatDelegate.setDefaultNightMode(newNightMode)

                // Recreate the activity to apply the changes
                recreate()

                true
            }

            //Logout button of firebase
            R.id.action_logout -> {

                firebaseAuth.signOut()
                Toast.makeText(this, "Log Out Successful", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, FeatureActivity::class.java)
                startActivity(intent)

                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
        finish()
    }

}