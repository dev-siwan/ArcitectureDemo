package com.ksw.base

import com.ksw.base.common.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class ViewModelSupporterImpl: ViewModelSupporter {
    private val compositeDisposable = CompositeDisposable()

    override fun dispose() = compositeDisposable.dispose()
    override fun addDisposable(vararg disposables: Disposable) = compositeDisposable.addAll(*disposables)
    override fun onCleared() {
        compositeDisposable.clear()
    }

    override fun doClickEvent(liveEvent: SingleLiveEvent<Any>)=liveEvent.call()


}