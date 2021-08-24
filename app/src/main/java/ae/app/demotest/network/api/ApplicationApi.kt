package ae.app.demotest.network.api

import ae.app.demotest.network.response.*
import retrofit2.Response

class ApplicationApi(private val mRequest: Api) {

    /** REQUESTS**/
suspend  fun getAlbums(): Response<MutableList<AlbumModel>> = mRequest.getAlbum()


}