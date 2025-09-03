package org.example.app.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Simple auth repository using SharedPreferences for demo purposes.
 * Not secure and not intended for production.
 */
class AuthRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("fittrack_auth", Context.MODE_PRIVATE)

    fun register(email: String, password: String): Boolean {
        val key = "user_${email.lowercase()}"
        if (prefs.contains(key)) return false
        prefs.edit()
            .putString(key, password)
            .putString("logged_in", email)
            .apply()
        return true
    }

    fun login(email: String, password: String): Boolean {
        val key = "user_${email.lowercase()}"
        val stored = prefs.getString(key, null) ?: return false
        val ok = stored == password
        if (ok) {
            prefs.edit().putString("logged_in", email).apply()
        }
        return ok
    }

    fun isLoggedIn(): Boolean = prefs.getString("logged_in", null) != null

    fun getLoggedInEmail(): String = prefs.getString("logged_in", "") ?: ""

    fun logout() {
        prefs.edit().remove("logged_in").apply()
    }
}
