package com.ksw.data.source.feed

import androidx.paging.DataSource
import com.ksw.data.model.FeedData
import com.ksw.data.source.BaseDataSource
import com.ksw.base.common.ResultState


/*피드 DataSource 인터페이스*/
interface FeedLocalDataSource:BaseDataSource {

    suspend fun insertFeed(feed: FeedData):Long

    suspend fun insertAllFeed(feeds: List<FeedData> ,insertFinished: () -> Unit)

    suspend fun deleteFeed(feed: FeedData)

    suspend fun updateFeed(feed: FeedData)

    suspend fun getFeedList(): DataSource.Factory<Int, FeedData>

    suspend fun getFeed(fid: String): ResultState<FeedData?>

}