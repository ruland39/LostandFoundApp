package com.lostandfoundapp

import android.widget.EditText
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(RegisterActivity::class.java)

    private lateinit var registerActivity: RegisterActivity

    @Before
    fun setUp() {
        registerActivity = activityRule.activity
    }

    @Test
    fun registerWithValidInput_Success() {
        val email = "test@example.com"
        val password = "password123"
        val confirmPassword = "password123"

        inputText(registerActivity.binding.idNumber, "123456")
        inputText(registerActivity.binding.name, "John Doe")
        inputText(registerActivity.binding.email, email)
        inputText(registerActivity.binding.phoneNumber, "1234567890")
        inputText(registerActivity.binding.password, password)
        inputText(registerActivity.binding.confirmPassword, confirmPassword)

        onView(withId(registerActivity.binding.registerButton.id)).perform(click())

        // TODO: Add assertions based on your app's logic
        // Example: Verify that the registration was successful
    }

    @Test
    fun registerWithInvalidEmail_Failure() {
        // Attempt to register with an invalid email
        inputText(registerActivity.binding.email, "invalidemail")

        onView(withId(registerActivity.binding.registerButton.id)).perform(click())

        // TODO: Add assertions based on your app's logic
        // Example: Verify that appropriate error message is displayed
//        onView(withText("Email is not valid")).check(matches(isDisplayed()))
    }

    @Test
    fun registerWithMismatchedPasswords_Failure() {
        // Attempt to register with mismatched passwords
        inputText(registerActivity.binding.password, "password123")
        inputText(registerActivity.binding.confirmPassword, "differentpassword")

        onView(withId(registerActivity.binding.registerButton.id)).perform(click())

        // TODO: Add assertions based on your app's logic
        // Example: Verify that appropriate error message is displayed
//        onView(withText("Password and Confirm Password are not the same")).check(matches(isDisplayed()))
    }

    @Test
    fun registerWithEmptyFields_Failure() {
        // Attempt to register with empty fields
        clearEditText(registerActivity.binding.email)

        onView(withId(registerActivity.binding.registerButton.id)).perform(click())

        // TODO: Add assertions based on your app's logic
        // Example: Verify that appropriate error message is displayed
//        onView(withText("Please fill up the fields")).check(matches(isDisplayed()))
    }

    @Test
    fun registerWithValidInputAndExistingEmail_Failure() {
        // Use an email that already exists in your Firebase database
        inputText(registerActivity.binding.email, "existinguser@example.com")
        inputText(registerActivity.binding.password, "password123")
        inputText(registerActivity.binding.confirmPassword, "password123")

        onView(withId(registerActivity.binding.registerButton.id)).perform(click())

        // Verify that appropriate error message is displayed
//        onView(withText("Email already in use.")).check(matches(isDisplayed()))
    }

    @Test
    fun registerWithValidInputAndShortPassword_Failure() {
        // Attempt to register with a short password
        inputText(registerActivity.binding.email, "test@example.com")
        inputText(registerActivity.binding.password, "short")
        inputText(registerActivity.binding.confirmPassword, "short")

        onView(withId(registerActivity.binding.registerButton.id)).perform(click())

        // Verify that appropriate error message is displayed
//        onView(withText("Password must be at least 6 characters.")).check(matches(isDisplayed()))
    }

    @Test
    fun registerWithValidInputAndInvalidPhoneNumber_Failure() {
        // Attempt to register with an invalid phone number
        inputText(registerActivity.binding.email, "test@example.com")
        inputText(registerActivity.binding.password, "password123")
        inputText(registerActivity.binding.confirmPassword, "password123")
        inputText(registerActivity.binding.phoneNumber, "invalidphone")

        onView(withId(registerActivity.binding.registerButton.id)).perform(click())

        // Verify that appropriate error message is displayed
//        onView(withText("Invalid phone number.")).check(matches(isDisplayed()))
    }

    // Helper function to clear text in an EditText
    private fun clearEditText(editText: EditText) {
        onView(withId(editText.id)).perform(clearText())
    }

    // Helper function to input text in an EditText
    private fun inputText(editText: EditText, text: String) {
        onView(withId(editText.id)).perform(typeText(text), closeSoftKeyboard())
    }
}
