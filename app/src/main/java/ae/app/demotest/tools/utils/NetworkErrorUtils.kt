package ae.app.demotest.tools.utils

import ae.app.demotest.App
import ae.app.demotest.R
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.Flowable
import io.reactivex.Single
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import io.reactivex.functions.Function

object NetworkErrorUtils {
    private const val SERVER_ERROR_CODE = 500
    private const val SERVER_ERROR_CODE_1 = 502

    private val TAG = NetworkErrorUtils::class.java.simpleName

    fun <T> rxParseError() = Function<Throwable, Single<T>> {
        Single.error<T>(parseError(it))
    }

    fun <T> rxParseErrorFlowable() = Function<Throwable, Flowable<T>> {
        Flowable.error<T>(parseError(it))
    }

    private fun parseError(throwable: Throwable): Throwable? {
        return if (throwable is HttpException) {
            // return this exception in case of error with 500 code
            val code = throwable.code()
            if (code == SERVER_ERROR_CODE || code == SERVER_ERROR_CODE_1) {
                LOG.e(TAG, throwable = throwable)
                return ServerException().initCause(throwable)
            }
            return ApiException(
                throwable.code(),
                throwable.message()
            )
        } else when {
            isConnectionProblem(throwable) -> NoNetworkException()
            isServerConnectionProblem(throwable) -> ServerException()
            else -> throwable
        }
    }

    private fun isServerConnectionProblem(throwable: Throwable) =
        throwable is SocketException || throwable is SocketTimeoutException

    private fun isConnectionProblem(throwable: Throwable) =
        throwable is UnknownHostException || throwable is ConnectException
}

class ServerException : ApiException() {

    companion object {
        private val ERROR_MESSAGE = App.instance.getString(R.string.server_error)
    }

    override val message = ERROR_MESSAGE
    override var statusCode: Int? = 500
}

/**
 * Error from server.
 */
open class ApiException() : Exception() {

    open var statusCode: Int? = null
    var mMessage: String? = null
    var v: String? = null
    var errors: List<ValidationError>? = null
    var stacktrace: String? = null

    constructor(
        statusCode: Int?,
        message: String?,
        v: String? = null,
        errors: List<ValidationError>? = null,
        stacktrace: String? = null
    ) : this() {
        this.statusCode = statusCode
        this.mMessage = message
        this.v = v
        this.errors = errors
        this.stacktrace = stacktrace
    }

    override val message = mMessage
}

class ValidationError(var code: Int?, var key: String?, var message: String?)

class NoNetworkException : Exception() {

    companion object {
        // TODO change no internet connection error message
        private val ERROR_MESSAGE = App.instance.getString(R.string.no_internet_connection_error)
    }

    override val message: String = ERROR_MESSAGE
}


