package ae.app.demotest

import ae.app.demotest.tools.classes.APP_PREFERENCES
import ae.app.demotest.tools.classes.LocaleHelperApplicationDelegate
import ae.app.demotest.tools.session.UserSession
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.multidex.MultiDex
import com.securepreferences.SecurePreferences

class App : Application() {
    private var currentSession: UserSession = UserSession

    companion object {
        lateinit var instance: App
            private set
        lateinit var prefs: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) prefs = getSharedPreferences()
        else prefs = getSecurePreferences()
    }

    private fun getSecurePreferences() =
        SecurePreferences(this, BuildConfig.SECURE_PREF_PASSWORD, APP_PREFERENCES)

    private fun getSharedPreferences() =
        instance.applicationContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    private val localeAppDelegate = LocaleHelperApplicationDelegate()
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base?.let { localeAppDelegate.attachBaseContext(it) })
        MultiDex.install(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeAppDelegate.onConfigurationChanged(this)
    }
}