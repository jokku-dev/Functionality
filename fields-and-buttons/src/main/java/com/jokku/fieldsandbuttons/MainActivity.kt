package com.jokku.fieldsandbuttons

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    private lateinit var contentLayout: ViewGroup
    private lateinit var loginLayout: TextInputLayout
    private lateinit var loginEdit: TextInputEditText
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var passwordEdit: TextInputEditText
    private lateinit var progressBar: ProgressBar
    private lateinit var agreementCheck: CheckBox
    private lateinit var registerBtn: Button

    companion object {
        const val passwordRegex =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=\\S+$).{4,}$"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contentLayout = findViewById(R.id.contentLayout)
        loginLayout = findViewById(R.id.login_lo)
        loginEdit = loginLayout.editText as TextInputEditText
        passwordLayout = findViewById(R.id.password_lo)
        passwordEdit = passwordLayout.editText as TextInputEditText
        progressBar = findViewById(R.id.progress_bar)
        agreementCheck = findViewById(R.id.terms_cb)
        registerBtn = findViewById(R.id.login_btn)

        val agreementSpannable = SpannableString(getString(R.string.agreement))
        agreementCheck.text = agreementSpannable
        registerBtn.isEnabled = false

        validateLogin()
        validatePassword()
        implCheckBox()
        implRegisterBtn()
    }

    private fun validateLogin() {
        val invalidEmailMsg = getString(R.string.invalid_email)
        loginEdit.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val valid = Patterns.EMAIL_ADDRESS.matcher(loginEdit.text.toString()).matches()
                loginLayout.isErrorEnabled = !valid
                val error = if (valid) "" else invalidEmailMsg
                loginLayout.error = error
            } else {
                loginLayout.isErrorEnabled = false
                if (agreementCheck.isChecked)
                    registerBtn.isEnabled = true
            }
        }
    }

    private fun validatePassword() {
        val invalidPasswordMsg = getString(R.string.invalid_password)
        passwordEdit.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val valid = Pattern.compile(passwordRegex).matcher(passwordEdit.text.toString())
                    .matches()
                passwordLayout.isErrorEnabled = !valid
                val error = if (valid) "" else invalidPasswordMsg
                passwordLayout.error = error
            } else {
                passwordLayout.isErrorEnabled = false
                if (agreementCheck.isChecked)
                    registerBtn.isEnabled = true
            }
        }
    }

    private fun implCheckBox() {
        agreementCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            registerBtn.isEnabled = isChecked
        }
    }

    private fun implRegisterBtn() {
        registerBtn.setOnClickListener {
            if (Patterns.EMAIL_ADDRESS.matcher(loginEdit.text.toString()).matches()
                && Pattern.compile(passwordRegex).matcher(passwordEdit.text.toString()).matches()
            ) {
                hideKeyboard(loginEdit)
                registerBtn.isEnabled = false
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                contentLayout.visibility = GONE
                progressBar.visibility = VISIBLE
                Handler(Looper.myLooper()!!).postDelayed({
                    contentLayout.visibility = VISIBLE
                    progressBar.visibility = GONE
                    //we need to instantiate dialog layout if we need interaction with its views
                    val dialogLayout = LayoutInflater.from(this).inflate(
                        R.layout.dialog, contentLayout, false
                    )
                    BottomSheetDialog(this).run {
                        dialogLayout.findViewById<ImageButton>(R.id.closeBtn).setOnClickListener {
                            dismiss()
                        }
                        setContentView(dialogLayout)
                        setCancelable(false)
                        show()
                    }
                    MyDialogFragment().show(supportFragmentManager, "Alert")
                }, 3000)
            } else {
                Toast.makeText(this, "Login or password is invalid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun AppCompatActivity.hideKeyboard(view: View) {
        val imm = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}