/*
 * Copyright (c) 2018 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ffc.app.auth.exception

import ffc.app.BuildConfig
import retrofit2.Response

class LoginErrorException(
    response: Response<*>,
    message: String? = when (response.code()) {
        400 -> "ชื่อผู้ใช้หรือรหัสผ่านไม่อยู่ในรูปแบบที่รองรับ"
        401 -> "ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง"
        403 -> "ผู้ใช้นี้ไม่อณุญาติให้เข้าใช้งาน"
        404 -> "ไม่พบข้อมูล"
        else -> "${response.code()}-เกิดข้อผิดพลาดไม่สามารถยืนยันตัวตนได้"
    }
) : RuntimeException(message) {

    val code = response.code()
    val body = response.errorBody()?.string()

    init {
        if (BuildConfig.DEBUG) {
            println("ApiErrorException code=$code message=$message body=$body")
        }
    }
}
