package ffc.v3.authen

interface LoginPresenter {

    fun onLoginSuccess(callback: () -> Unit)

    fun error(message: String)
}
