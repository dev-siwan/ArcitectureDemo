package com.ksw.data.di

import com.ksw.data.mapper.FeedDataMapper
import com.ksw.data.repository.feed.FeedRepositoryImpl
import com.ksw.domain.repository.feed.FeedRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val dataModule = module {
    val scope = CoroutineScope(Dispatchers.Main)
    single { scope }
    single { FeedDataMapper() }
    single<FeedRepository> { FeedRepositoryImpl(get(), get(), get(), get()) }
}