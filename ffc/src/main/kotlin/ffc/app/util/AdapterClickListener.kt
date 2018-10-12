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

import android.view.View
import ffc.android.onClick
import ffc.android.onLongClick

typealias OnItemClick<T> = View.(T) -> Unit
typealias OnItemLongClick<T> = View.(T) -> Boolean
typealias OnViewClick<T> = View.(View, T) -> Unit
typealias OnViewLongClick<T> = View.(View, T) -> Boolean

class AdapterClickListener<T> {

    var onItemClick: OnItemClick<T>? = null
        private set
    var onItemLongClick: OnItemLongClick<T>? = null
        private set
    var onViewClick: OnViewClick<T>? = null
        private set
    var onViewLongClick: OnViewLongClick<T>? = null
        private set

    fun onItemClick(block: OnItemClick<T>) {
        onItemClick = block
    }

    fun onItemLongClick(block: OnItemLongClick<T>) {
        onItemLongClick = block
    }

    fun onViewClick(block: OnViewClick<T>) {
        onViewClick = block
    }

    fun onViewLongClick(block: OnViewLongClick<T>) {
        onViewLongClick = block
    }

    internal fun bindOnItemClick(itemView: View, data: T) {
        onItemClick?.let { itemView.onClick { _ -> it.invoke(itemView, data) } }
        onItemLongClick?.let { itemView.onLongClick { _ -> it.invoke(itemView, data) } }
    }
}
