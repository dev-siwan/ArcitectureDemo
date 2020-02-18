package com.ksw.remote.firestore.api

import android.net.Uri
import com.ksw.base.common.ResultState
import com.ksw.remote.firestore.model.FeedModel
import java.io.File

interface FeedApi {
    suspend fun insertFeed(feed: FeedModel): ResultState<Boolean>

    suspend fun updateFeed(feed: FeedModel): ResultState<Boolean>

    suspend fun getFeedList(date:Long,pageSize:Int): ResultState<List<FeedModel>>

    suspend fun getFeed(fid:String): ResultState<FeedModel?>

    suspend fun deleteFeed(fid:String): ResultState<Boolean>

    suspend fun savePhoto(fid:String,file: File): ResultState<Uri>
}