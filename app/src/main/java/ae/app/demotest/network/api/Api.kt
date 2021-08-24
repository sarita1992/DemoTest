package ae.app.demotest.network.api

import ae.app.demotest.network.response.*
import retrofit2.Response
import retrofit2.http.*

interface Api {
    /** REQUESTS**/

    @GET("albums")
    suspend  fun getAlbum(): Response<MutableList<AlbumModel>>

}