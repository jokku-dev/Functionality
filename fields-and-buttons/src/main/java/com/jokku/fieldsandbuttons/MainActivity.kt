package com.jokku.fieldsandbuttons

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.util.Patterns
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    private lateinit var loginLayout: TextInputLayout
    private lateinit var loginEdit: TextInputEditText
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var passwordEdit: TextInputEditText
    private lateinit var registerBtn: Button

    companion object {
        const val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=\\S+$).{4,}$"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginLayout = findViewById(R.id.login_lo)
        loginEdit = loginLayout.editText as TextInputEditText
        passwordLayout = findViewById(R.id.password_lo)
        passwordEdit = passwordLayout.editText as TextInputEditText
        registerBtn = findViewById(R.id.login_btn)

        val invalidEmailMsg = getString(R.string.invalid_email)
        val invalidPasswordMsg = getString(R.string.invalid_password)

        loginEdit.onFocusChangeListener = object : OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (!hasFocus) {
                    val valid = Patterns.EMAIL_ADDRESS.matcher(loginEdit.text.toString()).matches()
                    loginLayout.isErrorEnabled = !valid
                    val error = if (valid) "" else invalidEmailMsg
                    loginLayout.error = error
                } else {
                    loginLayout.isErrorEnabled = false
                    registerBtn.isEnabled = true

                }
            }
        }

        passwordEdit.onFocusChangeListener = object : OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (!hasFocus) {
                    val valid = Pattern.compile(passwordRegex).matcher(passwordEdit.text.toString()).matches()
                    passwordLayout.isErrorEnabled = !valid
                    val error = if (valid) "" else invalidPasswordMsg
                    passwordLayout.error = error
                } else {
                    passwordLayout.isErrorEnabled = false
                    registerBtn.isEnabled = true
                }
            }
        }

        registerBtn.setOnClickListener {
            if (Patterns.EMAIL_ADDRESS.matcher(loginEdit.text.toString()).matches()
                && Pattern.compile(passwordRegex).matcher(passwordEdit.text.toString()).matches()) {
                hideKeyboard(loginEdit)
                registerBtn.isEnabled = false
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "Going to post register screen..", Toast.LENGTH_SHORT).show()
            } else {
                loginLayout.isErrorEnabled = true
                loginLayout.error = invalidEmailMsg
            }
        }

    }

    private fun AppCompatActivity.hideKeyboard(view: View) {
        val imm = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}