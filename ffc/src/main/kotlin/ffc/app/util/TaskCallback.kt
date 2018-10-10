package ffc.app.util

class TaskCallback<TResult> {

    lateinit var result: ((TResult) -> Unit)
        private set
    var expception: ((Throwable) -> Unit)? = null
        private set

    fun onComplete(result: (TResult) -> Unit) {
        this.result = result
    }

    fun onFail(exception: (Throwable) -> Unit) {
        this.expception = exception
    }
}
