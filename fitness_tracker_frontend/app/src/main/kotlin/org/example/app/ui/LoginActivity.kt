package org.example.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.example.app.MainActivity
import org.example.app.R
import org.example.app.data.AuthRepository

/**
 * PUBLIC_INTERFACE
 * LoginActivity
 *
 * Allows the user to register or login with a basic email + password form.
 * Uses local storage (SharedPreferences) for demo purposes.
 *
 * No parameters. On successful authentication, navigates to MainActivity.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var authRepo: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_FitTrack_Light_NoActionBar)
        setContentView(R.layout.activity_login)
        authRepo = AuthRepository(this)

        val email: EditText = findViewById(R.id.input_email)
        val password: EditText = findViewById(R.id.input_password)
        val toggleMode: TextView = findViewById(R.id.toggle_mode)
        val actionBtn: Button = findViewById(R.id.btn_action)
        val title: TextView = findViewById(R.id.title_text)

        var isLogin = true
        fun updateMode() {
            title.text = if (isLogin) getString(R.string.login) else getString(R.string.register)
            actionBtn.text = if (isLogin) getString(R.string.login) else getString(R.string.register)
            toggleMode.text =
                if (isLogin) getString(R.string.no_account_register) else getString(R.string.have_account_login)
        }

        toggleMode.setOnClickListener {
            isLogin = !isLogin
            updateMode()
        }

        actionBtn.setOnClickListener {
            val e = email.text.toString().trim()
            val p = password.text.toString().trim()
            if (e.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val success = if (isLogin) authRepo.login(e, p) else authRepo.register(e, p)
            if (success) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(
                    this,
                    if (isLogin) R.string.error_login_failed else R.string.error_register_failed,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        updateMode()
    }
}
