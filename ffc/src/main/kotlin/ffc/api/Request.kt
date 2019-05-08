/*
 * Copyright (c) 2019 NECTEC
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

package ffc.api

import okhttp3.Request

/**
 * Sets the header named {@code name} to {@code value}. If this request not already has any headers
 * with that name.
 */
internal fun Request.Builder.addHeaderOptional(name: String, value: String): Request.Builder {
    if (build().header(name) == null) {
        this.header(name, value)
    }
    return this
}
