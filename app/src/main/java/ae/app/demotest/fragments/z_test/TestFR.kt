package ae.app.demotest.fragments.z_test

import ae.app.demotest.databinding.ActivityMainBinding
import ae.app.demotest.tools.base.BaseLifecycleFragment
import ae.app.demotest.tools.extensions.setClickListeners
import ae.app.demotest.tools.utils.bindInterfaceOrThrow
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

class TestFR : BaseLifecycleFragment<TestVM>(), View.OnClickListener {

    companion object {
        fun newInstance() = TestFR()
    }

    private lateinit var b: ActivityMainBinding
    override val viewModelClass = TestVM::class.java
    private var callback: TestCB? = null

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, savedState: Bundle?): View {
        b = ActivityMainBinding.inflate(inflater, c, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        setClickListeners()

    }

    override fun onClick(v: View?) {
        when (v) {

        }
    }

    override fun observeLiveData() {
        vm.run {
            snackTextLD.observe(this@TestFR, snackTextObserver)
        }
    }

    private val snackTextObserver = Observer<String> { showSnackbar(it) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = bindInterfaceOrThrow<TestCB>(parentFragment, context)
//        val back = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                LOG.v(message = "${this.javaClass.simpleName}|${object {}.javaClass.enclosingMethod?.name}| ")
//                activity?.supportFragmentManager?.popBackStack()
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(this, back)
    }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }
}