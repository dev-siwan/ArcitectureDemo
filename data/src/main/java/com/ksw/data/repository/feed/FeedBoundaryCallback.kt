package com.ksw.data.repository.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.ksw.data.source.feed.FeedFireStoreDataSource
import com.ksw.data.source.feed.FeedLocalDataSource
import com.ksw.base.common.ResultState
import com.ksw.domain.model.Feed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FeedBoundaryCallback(
    private val feedLocalDataSource: FeedLocalDataSource,
    private val feedFireStoreDataSource: FeedFireStoreDataSource,
    private val scope: CoroutineScope
) : PagedList.BoundaryCallback<Feed>() {

    private var isRequestInProgress = false

    private var loadDate = 0L

    private var isFirst= false

    private val _listEmpty = MutableLiveData<Boolean>()
    val listEmpty: LiveData<Boolean>
        get() = _listEmpty

    private val _error = MutableLiveData<Exception>()
    val errors: LiveData<Exception>
        get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        _listEmpty.postValue(true)
        loadDate=System.currentTimeMillis()
        requestAndSaveFeed()
    }

    override fun onItemAtFrontLoaded(itemAtFront: Feed) {
        super.onItemAtFrontLoaded(itemAtFront)
        if(itemAtFront.updateTime > loadDate) {
            loadDate = System.currentTimeMillis()
            requestAndSaveFeed()
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Feed) {
        super.onItemAtEndLoaded(itemAtEnd)
            requestAndSaveFeed()
    }


    private fun requestAndSaveFeed() {

        if (isRequestInProgress) return

        isRequestInProgress = true

        _loading.value = true

        scope.launch {

            feedFireStoreDataSource.getFeedList(loadDate, NETWORK_PAGE_SIZE).let { result ->
                when (result) {
                    is ResultState.Success -> {

                        val resultList = result.data

                        if (resultList.isNotEmpty()) {
                            feedLocalDataSource.insertAllFeed(resultList) {
                                loadDate = result.data.last().updateTime
                                _listEmpty.postValue(false)
                                _loading.postValue(false)
                            }
                        }
                        isRequestInProgress = false
                    }

                    is ResultState.Error -> {
                        _error.postValue( result.exception)
                        _listEmpty.postValue(true)
                        _loading.postValue(false)
                        isRequestInProgress = false
                    }
                }
            }
        }
    }


    companion object {
        const val NETWORK_PAGE_SIZE = 5
    }
}