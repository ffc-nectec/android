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

package ffc.app.util

class RepoCallback<T> {

    var always: (() -> Unit)? = null
        private set
    var onFound: ((T) -> Unit)? = null
        private set
    var onNotFound: (() -> Unit)? = null
        private set
    var onFail: ((Throwable) -> Unit)? = null
        private set

    fun onFound(onFound: (T) -> Unit) {
        this.onFound = onFound
    }

    fun onNotFound(onNotFound: () -> Unit) {
        this.onNotFound = onNotFound
    }

    fun onFail(onFail: (Throwable) -> Unit) {
        this.onFail = onFail
    }

    fun always(always: () -> Unit) {
        this.always = always
    }
}
