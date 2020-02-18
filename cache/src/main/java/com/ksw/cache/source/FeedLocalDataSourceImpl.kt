package com.ksw.cache.source

import androidx.paging.DataSource
import com.ksw.cache.dao.FeedDao
import com.ksw.cache.mapper.FeedEntityMapper
import com.ksw.data.model.FeedData
import com.ksw.data.source.feed.FeedLocalDataSource
import com.ksw.base.common.ResultState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/*피드 DataSource*/
class FeedLocalDataSourceImpl constructor(
    private val feedDao: FeedDao,
    private val entityMapper: FeedEntityMapper,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FeedLocalDataSource {

    override suspend fun insertFeed(feed: FeedData): Long = withContext(Dispatchers.IO) {
        entityMapper.mapToEntity(feed).let {
            return@withContext feedDao.insert(it)
        }
    }

    override suspend fun insertAllFeed(feeds: List<FeedData>,insertFinished: () -> Unit) =
    withContext(Dispatchers.IO) {
        feeds.map { list -> entityMapper.mapToEntity(list) }.let {
            feedDao.replaceList(it)
        }
        insertFinished()
    }

    override suspend fun deleteFeed(feed: FeedData) = withContext(Dispatchers.IO) {
        entityMapper.mapToEntity(feed).let {
            feedDao.delete(it)
        }
    }

    override suspend fun updateFeed(feed: FeedData)= withContext(Dispatchers.IO) {
        entityMapper.mapToEntity(feed).let {
            feedDao.update(it)
        }
    }



    override suspend fun getFeedList(): DataSource.Factory<Int, FeedData> =
        withContext(ioDispatcher) {
            return@withContext feedDao.selectAllPaged().map { entityMapper.mapFromEntity(it) }
        }

    override suspend fun getFeed(fid: String): ResultState<FeedData?> = withContext(ioDispatcher) {
        try {
            val feedData = entityMapper.mapFromEntity(feedDao.select(fid))
            ResultState.Success(feedData)
        } catch (e: Exception) {
            ResultState.Error(e)
        }
    }


}