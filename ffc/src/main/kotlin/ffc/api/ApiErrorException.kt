package ffc.api

import com.google.gson.JsonSyntaxException
import ffc.app.BuildConfig
import retrofit2.Response

class ApiErrorException(response: Response<*>, message: String? = when (response.code()) {
    400 -> "ข้อมูลไม่ถูกต้อง"
    401 -> "ไม่มีการยืนยันตัวตน"
    403 -> "ไม่อนุญาติให้เข้าถึงข้อมูล"
    404 -> "ไม่พบข้อมูล"
    405 -> "ไม่รองรับการทำงานที่ร้องขอ"
    502 -> "Server ปิดปรับปรุงระบบ"
    503 -> "Server ไม่สามารถตอบสนองได้ขณะนี้"
    else -> "${response.code()}-เกิดข้อผิดพลาดไม่สามารถระบุได้"
}) : RuntimeException(message) {

    val code = response.code()
    val body = response.errorBody()?.string()

    init {
        if (BuildConfig.DEBUG) {
            println("ApiErrorException code=$code message=$message body=$body")
        }
    }
}
