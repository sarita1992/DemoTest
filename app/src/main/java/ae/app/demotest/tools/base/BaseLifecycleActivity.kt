package ae.app.demotest.tools.base

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import ae.app.demotest.databinding.ActivityMainBinding
import ae.app.demotest.tools.classes.LocaleHelperActivityDelegateImpl
import ae.app.demotest.tools.extensions.hide
import ae.app.demotest.tools.extensions.show
import com.google.android.material.snackbar.Snackbar
import java.util.*

abstract class BaseLifecycleActivity<T : BaseVM, VB : ViewBinding> : FragmentActivity(), BaseView {

    protected abstract val viewModelClass: Class<T>

    protected abstract val containerId: Int

    protected abstract val binding: VB

    protected val vm: T by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this).get(
            viewModelClass
        )
    }

    private val progressObserver = Observer<Boolean> { it?.let { showProgress(it) } }

    private val errorObserver = Observer<Any> {}

    abstract fun observeLiveData()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeAllLiveData()
        localeDelegate.onCreate(this)
    }

    private fun observeAllLiveData() {
        observeLiveData()
        with(vm) {
            isLoadingLD.observe(this@BaseLifecycleActivity, progressObserver)
            errorLD.observe(this@BaseLifecycleActivity, errorObserver)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.findFragmentById(containerId)
            ?.onActivityResult(requestCode, resultCode, data)
    }

    protected open fun replaceFR(fragment: Fragment, needToAddToBackStack: Boolean = true) {
        val name = fragment.javaClass.simpleName
        with(supportFragmentManager.beginTransaction()) {
            replace(containerId, fragment, name)
            if (needToAddToBackStack) {
                addToBackStack(name)
            }
            commit()
        }
    }

    protected open fun addFR(fragment: Fragment, needToAddToBackStack: Boolean = true) {
        val name = fragment.javaClass.simpleName
        with(supportFragmentManager.beginTransaction()) {
            add(containerId, fragment, name)
            if (needToAddToBackStack) {
                addToBackStack(name)
            }
            commit()
        }
    }

    override fun showProgress() {
        when (binding) {
            is ActivityMainBinding -> (binding as ActivityMainBinding).progressView.show()
        }
    }

    override fun showProgress(isShow: Boolean) {
        if (isShow) showProgress() else hideProgress()
    }

    override fun hideProgress() {
        when (binding) {
            is ActivityMainBinding -> (binding as ActivityMainBinding).progressView.hide(false)
        }
    }

    override fun onError(error: Any) {
        hideProgress()
        // showSnackbar(error.toString(), 0)
    }

    override fun showSnackbar(message: String) {
        Snackbar.make(findViewById(containerId), message, Snackbar.LENGTH_LONG).show()
    }

    override fun showSnackbar(@StringRes res: Int) {
        Snackbar.make(findViewById(containerId), getString(res), Snackbar.LENGTH_LONG).show()
    }

    override fun showAlert(
        message: String,
        title: String?,
        cancelable: Boolean,
        positiveRes: Int?,
        positiveFun: () -> Unit,
        negativeRes: Int?,
        negativeFun: () -> Unit
    ) {
        // not implemented yet
    }

    override fun hideSnackBar() {
        // not implemented yet
    }

    override fun showSnackBar(res: Int, actionRes: Int, callback: () -> Unit) {
        // not implemented yet
    }

    override fun createViewModelFactory(): ViewModelProvider.NewInstanceFactory? = null

    override fun isNeedProgress(): Boolean = true

    private val localeDelegate = LocaleHelperActivityDelegateImpl()

    override fun onResume() {
        super.onResume()
        localeDelegate.onResumed(this)
    }

    override fun onPause() {
        super.onPause()
        localeDelegate.onPaused()
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        super.applyOverrideConfiguration(
            localeDelegate.applyOverrideConfiguration(baseContext, overrideConfiguration)
        )
    }

    open fun updateLocale(locale: Locale) {
        localeDelegate.setLocale(this, locale)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(localeDelegate.attachBaseContext(newBase))
    }
}

