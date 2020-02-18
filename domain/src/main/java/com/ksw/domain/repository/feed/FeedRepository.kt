package com.ksw.domain.repository.feed

import android.net.Uri
import com.ksw.domain.common.PagingResult
import com.ksw.base.common.ResultState
import com.ksw.domain.model.Feed
import com.ksw.domain.repository.BaseRepository
import java.io.File

interface FeedRepository : BaseRepository{

    suspend fun insertFeed(feed:Feed): ResultState<Boolean>

    suspend fun deleteFeed(feed:Feed): ResultState<Boolean>

    suspend fun updateFeed(feed:Feed): ResultState<Boolean>

    suspend fun getFeedList():PagingResult<Feed>

    suspend fun getFeed(fid: String): ResultState<Feed?>

    suspend fun savePhoto(fid:String,file: File): ResultState<Uri>

}