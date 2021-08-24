package ae.app.demotest.tools.utils

import android.annotation.SuppressLint
import ae.app.demotest.App
import ae.app.demotest.tools.classes.*

@SuppressLint("CommitPrefEdits", "ApplySharedPref")
object PrefProvider {

    private val preferences = App.prefs

    fun saveSession(token: String) {
        preferences.edit()
            .putString(PREF_TOKEN, "$PREFIX $token")
            .commit()
    }

    fun clearSession() {
        preferences.edit()
            .clear()
            .commit()
    }

    var isFirstOpen: Boolean
        get() = preferences.getBoolean(PREF_FIRST_OPEN, true)
        set(value) {
            preferences.edit()
                .putBoolean(PREF_FIRST_OPEN, value)
                .commit()
        }

    var token: String
        get() = preferences.getString(PREF_TOKEN, EMPTY_STRING) ?: EMPTY_STRING
        set(value) {
            preferences.edit()
                .putString(PREF_TOKEN, value)
                .commit()
        }

}