package ffc.v3.authen

interface LoginPresenter {

    fun onLoginSuccess()

    fun onError(throwable: Throwable)
}
