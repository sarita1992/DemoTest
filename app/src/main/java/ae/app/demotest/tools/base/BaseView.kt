package ae.app.demotest.tools.base

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider

interface BaseView {

    fun showProgress()

    fun showProgress(isShow: Boolean)

    fun hideProgress()

    fun onError(error: Any)

    fun showSnackbar(message: String)

    fun showSnackbar(@StringRes res: Int)

    fun showAlert(message: String, title: String? = null, cancelable: Boolean = false, positiveRes: Int? = null, positiveFun: () -> Unit = {}, negativeRes: Int? = null, negativeFun: () -> Unit = {})

    fun hideSnackBar()

    fun showSnackBar(@StringRes res: Int, @StringRes actionRes: Int, callback: () -> Unit)

    fun createViewModelFactory(): ViewModelProvider.NewInstanceFactory?

    fun isNeedProgress(): Boolean
}