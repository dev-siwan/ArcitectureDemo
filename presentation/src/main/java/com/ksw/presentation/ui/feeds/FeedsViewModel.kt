package com.ksw.presentation.ui.feeds

import androidx.lifecycle.viewModelScope
import com.ksw.base.common.SingleLiveEvent
import com.ksw.base.BaseViewModel
import com.ksw.base.common.ResultState
import com.ksw.domain.model.Feed
import com.ksw.domain.usercase.feed.FeedUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class FeedsViewModel(private val feedUseCase: FeedUseCase) :BaseViewModel(){

    private val feedPagingResult = runBlocking { feedUseCase.getFeedList() }

    val feeds = feedPagingResult.data
    val networkState = feedPagingResult.networkState

    val addFeedClickEvent = SingleLiveEvent<Any>()
    val feedOptionClickEvent = SingleLiveEvent<Feed>()
    val deleteResult = SingleLiveEvent<Boolean>()

    fun feedMenu(feed: Feed) {
        feedOptionClickEvent.value = feed
    }

    fun refresh(){
        feedPagingResult.refresh.invoke()
    }

    fun deleteFeed(feed: Feed) {
        viewModelScope.launch {
            feedUseCase.deleteFeed(feed).let {
                when (it) {
                    is ResultState.Success -> {
                        deleteResult.value = it.data
                        /*feedPagingResult*/
                    }
                    is ResultState.Error -> {
                        deleteResult.value = false
                    }
                    else -> {
                        deleteResult.value = false
                    }
                }
            }
        }
    }

}