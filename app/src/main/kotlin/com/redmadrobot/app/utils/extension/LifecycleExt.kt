package com.redmadrobot.app.utils.extension

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.*

fun <T> MutableLiveData<T>.onNext(next: T) {
    this.value = next
}

inline fun <reified T : ViewModel> FragmentActivity.obtainViewModel(
    viewModelFactory: ViewModelProvider.Factory? = null
): T {
    return if (viewModelFactory != null) {
        ViewModelProvider(this, viewModelFactory)[T::class.java]
    } else {
        ViewModelProvider(this)[T::class.java]
    }
}

inline fun <reified T : ViewModel> FragmentActivity.obtainViewModel(
    viewModelFactory: ViewModelProvider.Factory? = null,
    body: T.() -> Unit
): T {
    val vm = if (viewModelFactory != null) {
        ViewModelProvider(this, viewModelFactory)[T::class.java]
    } else {
        ViewModelProvider(this)[T::class.java]
    }
    vm.body()
    return vm
}

inline fun <reified T : ViewModel> Fragment.obtainViewModel(
    viewModelFactory: ViewModelProvider.Factory? = null
): T {
    return if (viewModelFactory != null) {
        ViewModelProvider(this, viewModelFactory)[T::class.java]
    } else {
        ViewModelProvider(this)[T::class.java]
    }
}

inline fun <reified T : ViewModel> Fragment.obtainViewModel(
    viewModelFactory: ViewModelProvider.Factory? = null,
    body: T.() -> Unit
): T {
    val vm = if (viewModelFactory != null) {
        ViewModelProvider(this, viewModelFactory)[T::class.java]
    } else {
        ViewModelProvider(this)[T::class.java]
    }
    vm.body()
    return vm
}

inline fun <reified T : ViewModel> Fragment.assistedViewModel(
    crossinline viewModelProducer: (SavedStateHandle) -> T,
): Lazy<T> {
    return viewModels {
        object : AbstractSavedStateViewModelFactory(this, arguments) {
            override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
                @Suppress("UNCHECKED_CAST")
                return viewModelProducer(handle) as T
            }
        }
    }
}
