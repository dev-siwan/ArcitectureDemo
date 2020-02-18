package com.ksw.presentation.di

import com.ksw.presentation.ui.addfeeds.AddFeedViewModel
import com.ksw.presentation.ui.feeds.FeedsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule= module {
    viewModel { FeedsViewModel(get()) }
    viewModel { AddFeedViewModel(get(),get()) }
}