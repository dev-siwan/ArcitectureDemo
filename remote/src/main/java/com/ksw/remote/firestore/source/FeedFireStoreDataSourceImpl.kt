package com.ksw.remote.firestore.source

import android.net.Uri
import com.ksw.data.model.FeedData
import com.ksw.data.source.feed.FeedFireStoreDataSource
import com.ksw.base.common.ResultState
import com.ksw.remote.firestore.api.FeedApi
import com.ksw.remote.firestore.mapper.FeedModelMapper
import java.io.File

class FeedFireStoreDataSourceImpl constructor(
    private val feedApi: FeedApi,
    private val mapper: FeedModelMapper
) : FeedFireStoreDataSource {

    override suspend fun insertFeed(feed: FeedData): ResultState<Boolean> {
        return feedApi.insertFeed(feed = mapper.mapToModel(feed))
    }

    override suspend fun updateFeed(feed: FeedData): ResultState<Boolean> {
       return feedApi.updateFeed(feed = mapper.mapToModel(feed))
    }


    override suspend fun getFeedList(date: Long, pageSize: Int): ResultState<List<FeedData>> {
        return try {
            when (val feedList = feedApi.getFeedList(date, pageSize)) {
                is ResultState.Success -> {
                    ResultState.Success(feedList.data.map { mapper.mapFromModel(it) })
                }

                is ResultState.Error -> {
                    ResultState.Error(feedList.exception)
                }
                else -> ResultState.Error(Exception("FeedList Error"))
            }
        } catch (e: Exception) {
            ResultState.Error(e)
        }
    }

    override suspend fun deleteFeed(fid: String): ResultState<Boolean> {
        return feedApi.deleteFeed(fid)
    }

    override suspend fun savePhoto(fid: String, file: File): ResultState<Uri> {
        return feedApi.savePhoto(fid, file)
    }
}