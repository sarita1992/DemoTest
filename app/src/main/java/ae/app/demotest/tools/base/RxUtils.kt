package ae.app.demotest.tools.base

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers


/**
 * Unsubscribe subscription if it is not null
 * @param subscription some subscription
 */
fun unsubscribeIfNotNull(subscription: Disposable?) {
    subscription?.dispose()
}

/**
 * Returns new Subscription if current is null or unsubscribed
 * @param subscription some subscription
 * @return new Subscription if current is null or unsubscribed
 */
fun getNewCompositeSubIfUnsubscribed(subscription: CompositeDisposable?) =
    if (subscription == null || subscription.isDisposed) {
        CompositeDisposable()
    } else subscription

/**
 * ? -> IO -> MAIN
 * Subscribes in the worker IO thread
 * Observes in the Main thread
 * @param observable some observable
 * @param <T> any type; input type = output type
 * @return input observable, which subscribes in the worker IO thread and observes in the Main thread
</T> */
fun <T> ioToMain(observable: Observable<T>): Observable<T> = observable
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())

fun <T> ioToMain(single: Single<T>): Single<T> = single
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())

fun <T> ioToMain(flowable: Flowable<T>): Flowable<T> = flowable
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())

fun completToMain(completable: Completable): Completable = completable
    .subscribeOn(Schedulers.computation())
    .observeOn(AndroidSchedulers.mainThread())

/**
 * ? -> IO -> IO
 * Subscribes in the worker IO thread
 * Observes in the worker IO thread
 * @param observable some observable
 * @param <T> any type; input type = output type
 * @return input observable, which subscribes in the worker IO thread and observes in the worker IO thread
</T> */
fun <T> toWorkerThread(observable: Flowable<T>): Flowable<T> = observable
    .subscribeOn(Schedulers.io())
    .observeOn(Schedulers.io())

fun <T> ioToMainTransformer() = FlowableTransformer<T, T> {
    it
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> ioToMainTransformerSingle() = SingleTransformer<T, T> {
    it
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> ioToMainTransformerMaybe() = MaybeTransformer<T, T> {
    it
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> ioToMainTransformerObservable() = ObservableTransformer<T, T> {
    it
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> toWorkerThreadTransformer() = FlowableTransformer<T, T> {
    it
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
}

fun <T> ioToMainSingle() = SingleTransformer<T, T> {
    it
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> ioToMainFlowable() = FlowableTransformer<T, T> { inObservable ->
    inObservable
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> ioToMainObservable() = ObservableTransformer<T, T> {
    it
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.doAsyncSeparateThread(
    successful: (t: T) -> Unit,
    error: (error: Throwable?) -> Unit
): Disposable =
    subscribeOn(Schedulers.io())
        .subscribe({ successful(it) }, { error(it) })

fun <T> Single<T>.doAsyncSeparateThread(
    successful: Consumer<T>,
    error: Consumer<Throwable>
): Disposable =
    subscribeOn(Schedulers.io())
        .subscribe(successful, error)

fun <T> Flowable<T>.doAsync(
    successful: Consumer<T>,
    error: Consumer<Throwable>,
    loading: MediatorLiveData<Boolean>? = null,
    isShowProgress: Boolean = true
): Disposable =
    preSubscribe(loading, isShowProgress)
        .subscribe(successful, error)

fun <T> Flowable<T>.doAsync(
    successful: MutableLiveData<T>,
    error: Consumer<Throwable>,
    loading: MediatorLiveData<Boolean>? = null,
    isShowProgress: Boolean = true
): Disposable =
    preSubscribe(loading, isShowProgress)
        .subscribe(Consumer { successful.value = it }, error)

fun <T> Single<T>.doAsync(
    successful: Consumer<T>,
    error: Consumer<Throwable>,
    loading: MediatorLiveData<Boolean>? = null,
    isShowProgress: Boolean = true
): Disposable =
    preSubscribe(loading, isShowProgress)
        .subscribe(successful, error)

fun <T> Single<T>.doAsync(
    successful: MutableLiveData<T>,
    error: Consumer<Throwable>,
    loading: MediatorLiveData<Boolean>? = null,
    isShowProgress: Boolean = true
): Disposable =
    preSubscribe(loading, isShowProgress)
        .subscribe(Consumer { successful.value = it }, error)

fun <T> Observable<T>.doAsync(
    successful: Consumer<T>,
    error: Consumer<Throwable>,
    loading: MediatorLiveData<Boolean>? = null,
    isShowProgress: Boolean = true
): Disposable =
    preSubscribe(loading, isShowProgress)
        .subscribe(successful, error)

fun <T> Observable<T>.doAsync(
    successful: MutableLiveData<T>,
    error: Consumer<Throwable>,
    loading: MediatorLiveData<Boolean>? = null,
    isShowProgress: Boolean = true
): Disposable =
    preSubscribe(loading, isShowProgress)
        .subscribe(Consumer { successful.value = it }, error)

private fun <T> Single<T>.preSubscribe(loading: MediatorLiveData<Boolean>?, isShowProgress: Boolean = true): Single<T> {
    loading?.showProgress(isShowProgress)
    return compose(ioToMainSingle()).doOnEvent { _, _ -> loading?.hideProgress(isShowProgress) }
}

private fun <T> Flowable<T>.preSubscribe(
    loading: MediatorLiveData<Boolean>?, isShowProgress: Boolean = true
): Flowable<T> {
    loading?.showProgress(isShowProgress)
    return compose(ioToMainFlowable()).doOnEach { loading?.hideProgress(isShowProgress) }
}

private fun <T> Observable<T>.preSubscribe(
    loading: MediatorLiveData<Boolean>?,
    isShowProgress: Boolean = true
): Observable<T> {
    loading?.showProgress(isShowProgress)
    return compose(ioToMainObservable()).doOnEach { loading?.hideProgress(isShowProgress) }
}

private fun MediatorLiveData<Boolean>.hideProgress(isShowProgress: Boolean) {
    takeIf { isShowProgress }?.value = false
}

private fun MediatorLiveData<Boolean>.showProgress(isShowProgress: Boolean) {
    takeIf { isShowProgress }?.value = true
}

class BaseSingleTransformer<T>(val action: (T) -> Single<T>) : SingleTransformer<T, T> {

    override fun apply(upstream: Single<T>): SingleSource<T> =
        upstream.flatMap { it: T -> action(it) }

}

class BaseFlowableTransformer<T>(val action: (T) -> Flowable<T>) : FlowableTransformer<T, T> {

    override fun apply(upstream: Flowable<T>): Flowable<T> =
        upstream.flatMap { it: T -> action(it) }
}
