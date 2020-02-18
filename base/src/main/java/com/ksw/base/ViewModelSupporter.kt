package com.ksw.base

import com.ksw.base.common.SingleLiveEvent
import io.reactivex.disposables.Disposable

interface ViewModelSupporter {

    fun dispose()
    fun addDisposable(vararg disposables: Disposable): Boolean
    fun onCleared()
    fun doClickEvent(liveEvent: SingleLiveEvent<Any>)
}