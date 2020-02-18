package com.ksw.cache.di

import com.ksw.cache.AppDB
import com.ksw.cache.mapper.FeedEntityMapper
import com.ksw.cache.source.FeedLocalDataSourceImpl
import com.ksw.data.source.feed.FeedLocalDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val cacheModule = module {

    single { AppDB.getInstance(androidApplication()) }
    single { get<AppDB>().getFeedDao() }
    single { FeedEntityMapper() }
    single<FeedLocalDataSource> { FeedLocalDataSourceImpl(get(), get()) }
}