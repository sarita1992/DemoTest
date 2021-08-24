package ae.app.demotest.network.response

import java.io.Serializable


data class AlbumModel(
    val userId: Int?,
    val id: Int?,
    val title: String?,
): Serializable
