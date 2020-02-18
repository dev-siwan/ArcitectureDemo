package com.ksw.data.repository.feed

import android.net.Uri
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ksw.data.mapper.FeedDataMapper
import com.ksw.data.paging.FeedDataSourceFactory
import com.ksw.data.repository.BaseRepositoryImpl
import com.ksw.data.source.feed.FeedFireStoreDataSource
import com.ksw.data.source.feed.FeedLocalDataSource
import com.ksw.domain.common.PagingResult
import com.ksw.base.common.ResultState
import com.ksw.domain.model.Feed
import com.ksw.domain.repository.feed.FeedRepository
import kotlinx.coroutines.*
import java.io.File


class FeedRepositoryImpl(
    private val feedLocalDataSource: FeedLocalDataSource,
    private val feedFireStoreDataSource: FeedFireStoreDataSource,
    private val mapper: FeedDataMapper,
    private val scope: CoroutineScope
) : BaseRepositoryImpl(), FeedRepository {

    override suspend fun insertFeed(feed: Feed): ResultState<Boolean> =
        withContext(Dispatchers.IO) {

            val feedData = mapper.mapToData(feed)
            val apiFeedResult = feedFireStoreDataSource.insertFeed(feed = feedData)

            return@withContext perform(apiFeedResult, data = {

                runBlocking {
                    try {
                        feedLocalDataSource.insertFeed(mapper.mapToData(feed))
                    } catch (e: Exception) {
                        return@runBlocking ResultState.Error(e)
                    }
                }
                return@perform ResultState.Success(it)
            }, exception = {
                return@perform ResultState.Error(it)
            })
        }


    override suspend fun deleteFeed(feed: Feed): ResultState<Boolean> =
        withContext(Dispatchers.IO) {

            return@withContext perform(feedFireStoreDataSource.deleteFeed(feed.fid!!), data = {
                if (it) {
                    launch {
                        feedLocalDataSource.deleteFeed(mapper.mapToData(feed))
                    }
                }
                return@perform ResultState.Success(it)
            }, exception = {
                return@perform ResultState.Error(it)
            })
        }

    override suspend fun updateFeed(feed: Feed): ResultState<Boolean> =
        withContext(Dispatchers.IO) {
            val feedData = mapper.mapToData(feed)
            val apiFeedResult = feedFireStoreDataSource.updateFeed(feed = feedData)
            return@withContext perform(apiFeedResult, data = { value ->
                launch {
                    feedLocalDataSource.updateFeed(mapper.mapToData(feed))
                }
                return@perform ResultState.Success(value)
            }, exception = {
                return@perform ResultState.Error(it)
            })
        }


    override suspend fun getFeedList(): PagingResult<Feed> {

        val feedDataSourceFactory=FeedDataSourceFactory(scope,feedLocalDataSource,feedFireStoreDataSource)

        val mapperFeedDataSourceFactory =feedDataSourceFactory.map { mapper.mapFromData(it) }

        val config = PagedList.Config.Builder()
            .setPageSize(5)
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(1)
            .build()

        val data = LivePagedListBuilder(mapperFeedDataSourceFactory, config).build()


        val networkState =  Transformations.switchMap(feedDataSourceFactory.sourceLiveData) {
            it.networkState
        }

        return PagingResult(data, networkState , refresh = { feedDataSourceFactory.sourceLiveData.value?.invalidate() })

    }

    override suspend fun getFeed(fid: String): ResultState<Feed?> {

        return when (val result = feedLocalDataSource.getFeed(fid)) {
            is ResultState.Success -> {
                ResultState.Success(mapper.mapFromData(result.data!!))
            }
            is ResultState.Error -> {
                ResultState.Error(result.exception)
            }
            else -> {
                ResultState.Error(Exception("Not found Feed"))
            }
        }
    }

    override suspend fun savePhoto(fid: String, file: File): ResultState<Uri> {
        return feedFireStoreDataSource.savePhoto(fid, file)
    }

}