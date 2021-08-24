package ae.app.demotest.network.api

import ae.app.demotest.App
import ae.app.demotest.tools.classes.*
import ae.app.demotest.tools.utils.LOG
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

open class RestClient {
    val appApi: ApplicationApi
    private var api: Api
    val retrofit: Retrofit

    init {
        retrofit = Retrofit.Builder()
            .client(getOkHttpClient())
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        api = retrofit.create(Api::class.java)
        appApi = ApplicationApi(api)
    }

    companion object {
        val instance = RestClient()
    }

    private fun getOkHttpClient(): OkHttpClient {
        LOG.v(message = "${this.javaClass.simpleName}|${object {}.javaClass.enclosingMethod?.name}| getOkHttpClient")
        val httpCacheDirectory = File(App.instance.applicationContext.cacheDir, "responses")
        val myCache = Cache(httpCacheDirectory, 10485760)

        return OkHttpClient.Builder().run {
            cache(myCache)
                .addNetworkInterceptor { chain ->
                    val originalResponse = chain.proceed(chain.request())
                    val cacheControl = originalResponse.header("Cache-Control")
                    if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains(
                            "no-cache"
                        ) ||
                        cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")
                    ) {
                        return@addNetworkInterceptor originalResponse.newBuilder()
                            .header("Cache-Control", "public, max-age=" + 10)
                            .build()
                    } else {
                        return@addNetworkInterceptor originalResponse;
                    }
                }
                .addInterceptor { chain ->
                    var request = chain.request()
                    if (!isNetworkAvailable()) {
                        val maxStale = 60 * 60 * 24 * 28 // 4-weeks
                        request = request.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                            .build()
                    }
                    return@addInterceptor chain.proceed(request)
                }
            build()
        }
    }

    public fun isNetworkAvailable(): Boolean {
        LOG.v(message = "${this.javaClass.simpleName}|${object {}.javaClass.enclosingMethod?.name}| ")
        val connectivityManager =
            App.instance.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }
}