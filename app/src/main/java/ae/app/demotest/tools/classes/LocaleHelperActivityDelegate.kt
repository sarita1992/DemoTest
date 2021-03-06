package ae.app.demotest.tools.classes

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.view.View
import java.util.*

/**
Created by Sarita
 **/

interface LocaleHelperActivityDelegate {
    fun setLocale(activity: Activity, newLocale: Locale)
    fun attachBaseContext(newBase: Context): Context
    fun applyOverrideConfiguration(
        baseContext: Context,
        overrideConfiguration: Configuration?
    ): Configuration?

    fun onPaused()
    fun onResumed(activity: Activity)
    fun onCreate(activity: Activity)
}

class LocaleHelperActivityDelegateImpl : LocaleHelperActivityDelegate {
    override fun onCreate(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            activity.window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
    }

    private var locale: Locale = Locale.getDefault()

    override fun setLocale(activity: Activity, newLocale: Locale) {
        LocaleHelper.setLocale(activity, newLocale)
        locale = newLocale
        activity.recreate()
    }

    override fun attachBaseContext(newBase: Context): Context = LocaleHelper.onAttach(newBase)

    override fun applyOverrideConfiguration(
        baseContext: Context, overrideConfiguration: Configuration?
    ): Configuration? {
        if (overrideConfiguration != null && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        return overrideConfiguration
    }

    override fun onPaused() {
        locale = Locale.getDefault()
    }

    override fun onResumed(activity: Activity) {
        if (locale == Locale.getDefault()) return

        activity.recreate()
    }
}

class LocaleHelperApplicationDelegate {
    fun attachBaseContext(base: Context): Context = LocaleHelper.onAttach(base)

    fun onConfigurationChanged(context: Context) {
        LocaleHelper.onAttach(context)
    }
}