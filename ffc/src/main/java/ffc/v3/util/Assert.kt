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

package ffc.v3.util

import android.support.design.widget.TextInputLayout
import android.text.TextUtils
import android.widget.EditText

inline fun assertThat(value: Boolean, lazyMessage: () -> String) {
    if (!value) throw IllegalArgumentException(lazyMessage())
}

fun String?.notNullOrBlank() = !this.isNullOrBlank()

fun assertionNotEmpty(textInputLayout: TextInputLayout, textInput: String,
                      text: String): Boolean {
    return if (TextUtils.isEmpty(textInput.trim())) {
        textInputLayout.isErrorEnabled = true
        textInputLayout.error = text
        false
    } else {
        textInputLayout.isErrorEnabled = false
        true
    }
}
