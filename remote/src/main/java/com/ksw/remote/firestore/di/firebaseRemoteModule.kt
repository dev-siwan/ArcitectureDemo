package com.ksw.remote.firestore.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ksw.data.source.feed.FeedFireStoreDataSource
import com.ksw.remote.firestore.api.FeedApi
import com.ksw.remote.firestore.api.FeedApiImpl
import com.ksw.remote.firestore.common.FireBaseTask
import com.ksw.remote.firestore.mapper.FeedModelMapper
import com.ksw.remote.firestore.source.FeedFireStoreDataSourceImpl
import org.koin.dsl.module

val fireBaseRemoteModule= module {

    val fireStoreInstance by lazy { FirebaseFirestore.getInstance() }
    val fireStorageInstance by lazy { FirebaseStorage.getInstance() }
    val fireBaseTask by lazy { FireBaseTask() }

    single { fireStoreInstance }
    single { fireBaseTask }
    single { fireStorageInstance }
    single{ FeedModelMapper() }
    single<FeedApi> {FeedApiImpl(get(),get(),get())}
    single<FeedFireStoreDataSource> { FeedFireStoreDataSourceImpl(get(),get()) }

}