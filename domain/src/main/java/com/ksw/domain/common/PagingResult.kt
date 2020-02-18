package com.ksw.domain.common

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class PagingResult<D>(
        val data: LiveData<PagedList<D>>,
        val networkState: LiveData<NetworkState>,
        val refresh: () -> Unit

)

enum class NetworkState {
        LOADING,SUCCESS,FAIL,INIT_EMPTY,LOAD_EMPTY
}