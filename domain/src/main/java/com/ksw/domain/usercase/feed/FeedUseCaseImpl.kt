package com.ksw.domain.usercase.feed

import android.net.Uri
import com.ksw.domain.common.PagingResult
import com.ksw.base.common.ResultState
import com.ksw.domain.model.Feed
import com.ksw.domain.repository.feed.FeedRepository
import java.io.File

class FeedUseCaseImpl constructor(private val feedRepository: FeedRepository):FeedUseCase{
    override suspend fun insertFeed(feed: Feed): ResultState<Boolean> {
        return feedRepository.insertFeed(feed)
    }

    override suspend fun deleteFeed(feed: Feed) : ResultState<Boolean> {
        return feedRepository.deleteFeed(feed)
    }

    override suspend fun updateFeed(feed: Feed): ResultState<Boolean> {
        return feedRepository.updateFeed(feed)
    }


    override suspend fun getFeedList(): PagingResult<Feed> {
        return feedRepository.getFeedList()
    }

    override suspend fun getFeed(fid: String): ResultState<Feed?> {
        return feedRepository.getFeed(fid)
    }


    override suspend fun savePhoto(fid:String,file: File): ResultState<Uri> {
        return feedRepository.savePhoto(fid,file)
    }

}