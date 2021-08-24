package ae.app.demotest.tools.utils

inline fun <reified T> bindInterfaceOrThrow(vararg objects: Any?): T = objects.find { it is T }
    ?.let { it as T }
    ?: throw NotImplementedInterfaceException(T::class.java)

class NotImplementedInterfaceException(clazz: Class<*>) : ClassCastException(String.format(MESSAGE, clazz)) {

    companion object {
        private const val MESSAGE = "You need to implement %s"
    }
}