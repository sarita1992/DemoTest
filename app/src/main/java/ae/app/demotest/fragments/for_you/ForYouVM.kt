package ae.app.demotest.fragments.for_you

import ae.app.demotest.network.api.RestClient
import ae.app.demotest.network.response.AlbumModel
import ae.app.demotest.tools.base.BaseVM
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.codewix.test.utils.PrefStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

class ForYouVM(application: Application) : BaseVM(application) {
    var albumList = MutableLiveData<MutableList<AlbumModel>>()
    var store: PrefStore
    var networkModule: RestClient = RestClient()
    var objec = ArrayList<AlbumModel>()

    // data source
    suspend fun getAlbums() {
        CoroutineScope(Dispatchers.IO).launch {
            val networkApi = networkModule?.appApi?.getAlbums()
            objec.clear()
            if (!networkModule.isNetworkAvailable()) {
                objec.addAll(store.getData("data") ?: ArrayList<AlbumModel>())
            } else {
                objec = networkApi?.body() as ArrayList<AlbumModel>
                objec?.sortWith(Comparator { item, t1 ->
                    val s1: String? = item!!.title
                    val s2: String? = t1!!.title
                    s1!!.compareTo(s2!!, ignoreCase = true)
                })
                store.setData("data", objec)
            }
            albumList.postValue(objec as? MutableList<AlbumModel>)
        }
    }

    init {
        store = PrefStore(application.applicationContext)
    }

}