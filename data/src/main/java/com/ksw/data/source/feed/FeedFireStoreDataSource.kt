package com.ksw.data.source.feed

import android.net.Uri
import com.ksw.data.model.FeedData
import com.ksw.data.source.BaseDataSource
import com.ksw.base.common.ResultState
import java.io.File

interface FeedFireStoreDataSource:BaseDataSource {
    suspend fun insertFeed(feed: FeedData): ResultState<Boolean>

    suspend fun updateFeed(feed: FeedData): ResultState<Boolean>

    suspend fun getFeedList(date:Long,pageSize:Int): ResultState<List<FeedData>>

    suspend fun deleteFeed(fid:String): ResultState<Boolean>

    suspend fun savePhoto(fid:String,file: File): ResultState<Uri>

}