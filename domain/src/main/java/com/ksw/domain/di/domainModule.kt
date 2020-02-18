package com.ksw.domain.di

import com.ksw.domain.repository.feed.FeedRepository
import com.ksw.domain.usercase.feed.FeedUseCase
import com.ksw.domain.usercase.feed.FeedUseCaseImpl
import org.koin.dsl.module

val domainModule= module {
    single<FeedUseCase> { FeedUseCaseImpl(get()) }
}