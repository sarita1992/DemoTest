package ae.app.demotest.fragments.for_you

import ae.app.demotest.databinding.FrForYouBinding
import ae.app.demotest.fragments.home.AlbumAdapter
import ae.app.demotest.fragments.home.AlbumAdapterCB
import ae.app.demotest.network.response.AlbumModel
import ae.app.demotest.tools.base.BaseLifecycleFragment
import ae.app.demotest.tools.utils.bindInterfaceOrThrow
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForYouFR : BaseLifecycleFragment<ForYouVM>(),AlbumAdapterCB{

    companion object {
        fun newInstance() = ForYouFR()
    }

    lateinit var b: FrForYouBinding
    override val viewModelClass = ForYouVM::class.java
    private var callback: ForYouCB? = null

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, savedState: Bundle?): View {
        b = FrForYouBinding.inflate(inflater, c, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        b.rvAlbum.layoutManager = LinearLayoutManager(activity)
        CoroutineScope(Dispatchers.IO).launch {
            vm.getAlbums()
        }
    }

    override fun observeLiveData() {
        vm.run {
            albumList.observe(this@ForYouFR, albumObserver)
        }
    }

    private val albumObserver = Observer<MutableList<AlbumModel>> {
        // init recycler view with album data
        b.rvAlbum.apply {
            adapter = AlbumAdapter(this@ForYouFR, it)
            setHasFixedSize(true)
        }
    }

    // implementation of AlbumAdapterCB
    override fun openAlbum(albumId: Int) {
        // TODO("Not yet implemented")
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = bindInterfaceOrThrow<ForYouCB>(parentFragment, context)
    }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }

}