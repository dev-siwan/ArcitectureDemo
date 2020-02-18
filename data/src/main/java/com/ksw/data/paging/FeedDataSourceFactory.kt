package com.ksw.data.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.ksw.data.model.FeedData
import com.ksw.data.source.feed.FeedFireStoreDataSource
import com.ksw.data.source.feed.FeedLocalDataSource
import com.ksw.domain.common.PagingResult
import kotlinx.coroutines.CoroutineScope
import java.util.*

class FeedDataSourceFactory(
    private val scope: CoroutineScope,
    private val feedLocalDataSource: FeedLocalDataSource,
    private val feedFireStoreDataSource: FeedFireStoreDataSource
) : DataSource.Factory<Long, FeedData>() {

    val sourceLiveData =MutableLiveData<FeedDataSource>()

    override fun create(): DataSource<Long, FeedData> {
        val source  = FeedDataSource(
            scope,
            feedLocalDataSource,
            feedFireStoreDataSource
        )
        sourceLiveData.postValue(source)
        return source
    }
}