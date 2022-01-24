package ffc.app.util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

//
//import android.arch.lifecycle.MutableLiveData
//import android.arch.lifecycle.ViewModel

open class SimpleViewModel<T> : ViewModel() {
    val content = MutableLiveData<T>()
    val loading = MutableLiveData<Boolean>()
    val exception = MutableLiveData<Throwable>()
}
