package ffc.android

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

/**
 * MIT License
 *
 * Copyright (c) 2018 Piruin Panichphol
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
//
//import android.arch.lifecycle.LifecycleOwner
//import android.arch.lifecycle.LiveData
//import android.arch.lifecycle.Observer
//import android.arch.lifecycle.ViewModel
//import android.arch.lifecycle.ViewModelProviders
//import android.support.v4.app.Fragment
//import android.support.v4.app.FragmentActivity

inline fun <reified T : ViewModel> FragmentActivity.viewModel(): T =
    ViewModelProviders.of(this).get(T::class.java)

inline fun <reified T : ViewModel> Fragment.viewModel(): T =
    ViewModelProviders.of(this).get(T::class.java)

inline fun <T> LifecycleOwner.observe(liveData: LiveData<T>, crossinline task: (T?) -> Unit) {
    liveData.observe(this, Observer<T> { t -> task(t) })
}
