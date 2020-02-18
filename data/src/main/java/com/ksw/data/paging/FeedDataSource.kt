package com.ksw.data.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import com.ksw.data.model.FeedData
import com.ksw.data.source.feed.FeedFireStoreDataSource
import com.ksw.data.source.feed.FeedLocalDataSource
import com.ksw.domain.common.NetworkState
import com.ksw.base.common.ResultState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FeedDataSource(
    private val scope: CoroutineScope,
    private val feedLocalDataSource: FeedLocalDataSource,
    private val feedFireStoreDataSource: FeedFireStoreDataSource
) : ItemKeyedDataSource<Long, FeedData>() {

    private var loadDate = System.currentTimeMillis()

    val networkState = MutableLiveData<NetworkState>()

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<FeedData>
    ) {
        scope.launch {

            networkState.postValue(NetworkState.LOADING)

            feedFireStoreDataSource.getFeedList(
                loadDate,
                NETWORK_PAGE_SIZE
            ).let { result ->
                when (result) {
                    is ResultState.Success -> {

                        val resultList = result.data

                        if (resultList.isNotEmpty()) {
                            feedLocalDataSource.insertAllFeed(resultList) {
                                callback.onResult(resultList)
                                networkState.postValue(NetworkState.SUCCESS)
                            }
                        } else {
                            networkState.postValue(NetworkState.INIT_EMPTY)
                        }
                    }

                    is ResultState.Error -> {
                        networkState.postValue(NetworkState.FAIL)
                    }
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<FeedData>) {
        scope.launch {

            networkState.postValue(NetworkState.LOADING)

            feedFireStoreDataSource.getFeedList(
                params.key,
                NETWORK_PAGE_SIZE
            ).let { result ->
                when (result) {
                    is ResultState.Success -> {

                        val resultList = result.data

                        if (resultList.isNotEmpty()) {
                            feedLocalDataSource.insertAllFeed(resultList) {
                                callback.onResult(resultList)
                                networkState.postValue(NetworkState.SUCCESS)
                            }
                        }else{
                            networkState.postValue(NetworkState.LOAD_EMPTY)
                        }
                    }

                    is ResultState.Error -> {
                        networkState.postValue(NetworkState.FAIL)
                    }
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<FeedData>) {

    }

    override fun getKey(item: FeedData): Long {
        return item.updateTime
    }



    companion object {
        const val NETWORK_PAGE_SIZE = 5
    }
}