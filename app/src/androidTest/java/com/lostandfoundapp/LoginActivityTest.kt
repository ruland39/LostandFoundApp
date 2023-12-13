package com.lostandfoundapp

import android.text.Editable
import android.widget.EditText
import android.widget.Toast
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.google.android.material.textfield.TextInputEditText
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.internal.matchers.Null

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(LoginActivity::class.java)

    private lateinit var loginActivity: LoginActivity

    @Before
    fun setUp() {
        loginActivity = activityRule.activity
    }

    @Test
    fun checkEmail_ValidEmail_ReturnsTrue() {
        val result = loginActivity.checkEmail("test@example.com")
        assertTrue(result)
    }

    @Test
    fun checkEmail_InvalidEmail_ReturnsFalse() {
        val result = loginActivity.checkEmail("invalidemail")
        assertFalse(result)
    }

    @Test
    fun checkPassword_ValidPassword_ReturnsTrue() {
        val result = loginActivity.checkPassword("password123")
        assertTrue(result)
    }

    @Test
    fun checkPassword_LongPassword_ReturnsTrue() {
        val result = loginActivity.checkPassword("verylongpassword")
        assertTrue(result)
    }

    @Test
    fun checkPassword_MinimumLength_ReturnsTrue() {
        val result = loginActivity.checkPassword("abcdefgh")
        assertTrue(result)
    }

//    @Test
//    fun checkPassword_EmptyPassword_ReturnsFalse() {
//        val result = loginActivity.checkPassword("")
//        assertFalse(result)
//    }

//    @Test
//    fun checkPassword_WhitespacePassword_ReturnsFalse() {
//        val result = loginActivity.checkPassword("   ") // Password with only whitespace
//        assertFalse(result)
//    }

    @Test
    fun checkPassword_SpecialCharacters_ReturnsTrue() {
        val result = loginActivity.checkPassword("P@ssw0rd!")
        assertTrue(result)
    }

//    @Test
//    fun checkPassword_ShortPassword_ReturnsFalse() {
//        val result = loginActivity.checkPassword("123")
//        assertFalse(result)
//    }

//    @Test
//    fun afterTextChanged_EnableLoginButton() {
//        val editTextEmail = mock(EditText::class.java)
//        val editTextPassword = mock(EditText::class.java)
//
//        loginActivity.binding.email.setText("test@example.com")
//        loginActivity.binding.password.setText("password123")
//
//        assertTrue(loginActivity.binding.loginButton.isEnabled)
//    }

//    @Test
//    fun afterTextChanged_DisableLoginButton() {
//        val editTextEmail = mock(EditText::class.java)
//        val editTextPassword = mock(EditText::class.java)
//
//        loginActivity.binding.email.setText("")
//        loginActivity.binding.password.setText("")
//
//        assertFalse(loginActivity.binding.loginButton.isEnabled)
//    }

    //TODO: Change Login Activity to return Boolean False rather than Toast Message
    //FIXME

}

