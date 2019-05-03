package ffc.api

data class ErrorResponse(
    val code: Int,
    val message: String,
    val t: Throwable?,
    val tType: String?
)
