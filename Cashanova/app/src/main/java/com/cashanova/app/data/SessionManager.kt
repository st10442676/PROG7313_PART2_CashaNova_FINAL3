package com.cashanova.app.data

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("cashanova_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_ID       = "user_id"
        private const val KEY_USERNAME      = "username"
        private const val KEY_IS_LOGGED_IN  = "is_logged_in"
    }

    fun saveSession(userId: Int, username: String) {
        prefs.edit()
            .putInt(KEY_USER_ID, userId)
            .putString(KEY_USERNAME, username)
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .apply()
    }

    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, -1)

    fun getUsername(): String = prefs.getString(KEY_USERNAME, "") ?: ""

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}

//Griffiths, D. and Griffiths, D. 2021. Head First Android Development: A Learner's Guide to Building Android Apps with Kotlin. 3rd ed. Sebastopol: O'Reilly Media.