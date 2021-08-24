package ae.app.demotest.app

import ae.app.demotest.R
import ae.app.demotest.databinding.ActivityMainBinding
import ae.app.demotest.fragments.for_you.ForYouCB
import ae.app.demotest.fragments.for_you.ForYouFR
import ae.app.demotest.tools.base.BaseLifecycleActivity
import ae.app.demotest.tools.classes.*
import ae.app.demotest.tools.extensions.hide
import ae.app.demotest.tools.extensions.setClickListeners

import android.os.Bundle
import android.view.View

class MainActivity : BaseLifecycleActivity<MainVM, ActivityMainBinding>(), View.OnClickListener,
    ForYouCB{
    private lateinit var b: ActivityMainBinding
    override val binding: ActivityMainBinding get() = b
    override val containerId = R.id.mContainer
    override val viewModelClass = MainVM::class.java
    override fun observeLiveData() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)
        setClickListeners(b.progressView)
        replaceFR(ForYouFR.newInstance(), false)
    }

    override fun onClick(v: View?) {}


    // leave in stack index fragments
    private fun clearStack(index: Int) {
        var count = supportFragmentManager.backStackEntryCount
        while (count > index) {
            supportFragmentManager.popBackStack()
            count--
        }
    }

    override fun onBackPressed() {
        b.progressView.hide()
        super.onBackPressed()
    }

    // returns tag of current fragment
    private fun getCurrentTag(): String =
        if ((supportFragmentManager.findFragmentById(R.id.mContainer) != null)) {
            (supportFragmentManager.findFragmentById(R.id.mContainer))?.tag ?: EMPTY_STRING
        } else EMPTY_STRING

}
