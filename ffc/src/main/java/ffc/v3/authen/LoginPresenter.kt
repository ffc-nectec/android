package ffc.v3.authen

interface LoginPresenter {

    fun onLoginSuccess()

    fun onError(message: String)

    fun onError(message: Int)
}
