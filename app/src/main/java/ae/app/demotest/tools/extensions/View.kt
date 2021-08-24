package ae.app.demotest.tools.extensions

import androidx.annotation.DrawableRes
import android.view.View
import android.view.View.*
import ae.app.demotest.App

fun View.OnClickListener.setClickListeners(vararg views: View) {
    views.forEach { view -> view.setOnClickListener(this) }
}

fun View.setDrawable(@DrawableRes drawableRes: Int) {
    background = App.instance.getDrawable(drawableRes)
}

fun View.hide(gone: Boolean = true) {
    visibility = if (gone) GONE else INVISIBLE
}

fun View.show() {
    visibility = VISIBLE
}