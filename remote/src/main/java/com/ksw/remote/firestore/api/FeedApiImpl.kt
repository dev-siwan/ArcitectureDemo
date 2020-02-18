package com.ksw.remote.firestore.api

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.ksw.base.common.FeedCollection
import com.ksw.base.common.ResultState
import com.ksw.remote.firestore.common.FireBaseTask
import com.ksw.remote.firestore.model.FeedModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class FeedApiImpl constructor(
    fireStore: FirebaseFirestore,
    fireStorage : FirebaseStorage,
    private val fireBaseTask: FireBaseTask,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FeedApi {
    private val feedCollection = fireStore.collection(FeedCollection)
    private val feedStorageReference = fireStorage.reference.child(FeedCollection)

    override suspend fun insertFeed(feed: FeedModel): ResultState<Boolean> =
        withContext(ioDispatcher) {
            return@withContext fireBaseTask.setData(feedCollection.document(feed.fId!!), feed)
        }

    override suspend fun updateFeed(feed: FeedModel): ResultState<Boolean> =
        withContext(ioDispatcher) {
            return@withContext fireBaseTask.setData(feedCollection.document(feed.fId!!), feed)
        }

    override suspend fun getFeedList(date:Long,pageSize:Int): ResultState<List<FeedModel>> =
        withContext(ioDispatcher){
            val query = feedCollection.whereLessThan("updateTime",date)
                .orderBy("updateTime", Query.Direction.DESCENDING)
                .limit(pageSize.toLong())
            return@withContext fireBaseTask.getData(query, FeedModel::class.java)
        }


    override suspend fun getFeed(fid: String): ResultState<FeedModel?> =
      withContext(ioDispatcher){
          return@withContext fireBaseTask.getData(feedCollection.document(fid),FeedModel::class.java)
      }


    override suspend fun deleteFeed(fid: String): ResultState<Boolean> =
        withContext(ioDispatcher){
        return@withContext fireBaseTask.delete(feedCollection.document(fid))
    }

    override suspend fun savePhoto(fid:String,file: File): ResultState<Uri> =
      withContext(ioDispatcher){
          return@withContext fireBaseTask.saveStorageImage(feedStorageReference.child(fid).child("FeedImg"),file)
      }


}