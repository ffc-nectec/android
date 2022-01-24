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

@file:Suppress("UNCHECKED_CAST")

package ffc.android

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

//import android.support.annotation.IdRes
//import android.support.v4.app.Fragment
//import android.support.v4.app.FragmentManager
//import android.support.v4.app.FragmentTransaction

fun <T> FragmentManager.find(id: Int) = findFragmentById(id) as T
fun <T> FragmentManager.find(tag: String) = findFragmentByTag(tag) as T
fun <T> FragmentManager.findFirst(vararg tags: String): T? {
    tags.forEach {
        val fragment = findFragmentByTag(it)
        if (fragment != null)
            return fragment as T
    }
    return null
}

fun FragmentManager.replaceAll(@IdRes id: Int, vararg pair: Pair<String, Fragment>): FragmentTransaction {
    return replaceAll(id, pair.toMap())
}

fun FragmentManager.replaceAll(@IdRes id: Int, map: Map<String, Fragment>): FragmentTransaction {
    val trans = beginTransaction()
    map.forEach {
        val fragment = findFragmentByTag(it.key)
        if (fragment != null) {
            trans.remove(fragment)
        }
        trans.add(id, it.value, it.key)
    }
    return trans
}
