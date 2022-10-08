package com.jokku.fieldsandbuttons

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    companion object {
        const val passwordRegex =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=\\S+$).{4,}$"

        const val INITIAL = 0
        const val PROGRESS = 1
        const val SUCCESS = 2
        const val FAILED = 3
    }
    private var state = INITIAL
    private lateinit var viewModel: ViewModel
    private lateinit var contentLayout: ViewGroup
    private lateinit var loginLayout: TextInputLayout
    private lateinit var loginEdit: TextInputEditText
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var passwordEdit: TextInputEditText
    private lateinit var progressBar: ProgressBar
    private lateinit var agreementCheck: CheckBox
    private lateinit var registerBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = (application as MyApplication).viewModel
        contentLayout = findViewById(R.id.contentLayout)
        loginLayout = findViewById(R.id.login_lo)
        loginEdit = loginLayout.editText as TextInputEditText
        passwordLayout = findViewById(R.id.password_lo)
        passwordEdit = passwordLayout.editText as TextInputEditText
        progressBar = findViewById(R.id.progress_bar)
        agreementCheck = findViewById(R.id.terms_cb)
        registerBtn = findViewById(R.id.login_btn)

        registerBtn.isEnabled = false

        validateLogin()
        validatePassword()
        implCheckBox()
        implRegisterBtn()

        val observable = TextObservable()
        observable.observe(object : TextCallback{
            override fun updateText(str: String) = runOnUiThread {
                TODO("implement view and view model interaction")
            }
        })

        if (savedInstanceState != null) {
            state = savedInstanceState.getInt("state")
        }
        when (state) {
            FAILED -> showBottomDialog()
            SUCCESS -> {
                Snackbar.make(contentLayout, "SuccessState", Snackbar.LENGTH_SHORT).show()
                state = INITIAL
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        loginEdit.setText(savedInstanceState.getString("login"))
        passwordEdit.setText(savedInstanceState.getString("password"))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("login", loginEdit.text.toString())
        outState.putString("password", passwordEdit.text.toString())
        outState.putInt("state", state)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
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
                state = PROGRESS
                Handler(Looper.myLooper()!!).postDelayed({
                    state = FAILED
                    contentLayout.visibility = VISIBLE
                    progressBar.visibility = GONE
                    showBottomDialog()
                    MyDialogFragment().show(supportFragmentManager, "Alert")
                }, 3000)
            } else {
                Toast.makeText(this, "Login or password is invalid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showBottomDialog() {
        //we need to instantiate dialog layout if we need interactions with its views
        val dialogLayout = LayoutInflater.from(this).inflate(
            R.layout.dialog, contentLayout, false
        )
        BottomSheetDialog(this).run {
            dialogLayout.findViewById<ImageButton>(R.id.closeBtn).setOnClickListener {
                state = INITIAL
                dismiss()
                registerBtn.isEnabled = true
            }
            setContentView(dialogLayout)
            setCancelable(false)
            show()
        }
    }

    private fun AppCompatActivity.hideKeyboard(view: View) {
        val imm = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}