package com.ksw.base

import androidx.lifecycle.ViewModel
import com.ksw.base.common.SingleLiveEvent
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel(), ViewModelSupporter {
    private val viewModelSupporter: ViewModelSupporter = ViewModelSupporterImpl()


    override fun doClickEvent(liveEvent: SingleLiveEvent<Any>) = liveEvent.call()

    override fun dispose() = viewModelSupporter.dispose()
    override fun addDisposable(vararg disposables: Disposable) = viewModelSupporter.addDisposable(*disposables)
    override fun onCleared() {
        viewModelSupporter.onCleared()
        super.onCleared()
    }
}