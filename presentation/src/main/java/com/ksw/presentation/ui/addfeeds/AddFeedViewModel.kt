package com.ksw.presentation.ui.addfeeds

import android.net.Uri
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ksw.base.common.SingleLiveEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.ksw.base.BaseViewModel
import com.ksw.base.common.FeedCollection
import com.ksw.base.common.ResultState
import com.ksw.domain.model.Feed
import com.ksw.domain.usercase.feed.FeedUseCase
import kotlinx.coroutines.launch
import java.io.File

class AddFeedViewModel constructor(
    fireStore: FirebaseFirestore,
    private val feedUseCase: FeedUseCase
) : BaseViewModel() {

    val doGetPhotoEvent = SingleLiveEvent<Any>()

    var feedImgFile: File? = null

    val successEvent = SingleLiveEvent<SuccessType>()
    val errorEvent = SingleLiveEvent<ErrorType>()
    val emptyEvent = SingleLiveEvent<EmptyType>()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    var feed: Feed? = null

    val title = ObservableField<String>()
    val content = ObservableField<String>()
    val imgUrl = SingleLiveEvent<String>()

    var isPhoto = false
    private var isDataLoaded = false

    //auto document ID
    private val autoDocID = fireStore.collection(FeedCollection).document().id

    //피드 수정 로직
    fun getFeed(fid: String?) {
        if (_dataLoading.value == true) {
            return
        }

        fid?.let { feedID ->

            _dataLoading.value = true

            viewModelScope.launch {

                feedUseCase.getFeed(feedID).let { resultState ->
                    if (resultState is ResultState.Success) {
                        onFeedLoaded(resultState.data!!)
                    } else if (resultState is ResultState.Error) {
                        errorEvent.value = ErrorType.GetFeed
                    }
                }

            }
        }
    }

    //들고온 피드 로드
    private fun onFeedLoaded(feed: Feed) {
        title.set(feed.title)
        content.set(feed.content)
        feed.imgUrl?.let {
            imgUrl.value = it
            isPhoto = true
        }
        this.feed = feed
        _dataLoading.value = false
        isDataLoaded = true
    }

    //피드 저장
    fun saveFeed() {
        //데이터 로딩이 true 일때 리턴
        if (_dataLoading.value == true) {
            return
        }

        val currentTitle = title.get()
        val currentContent = content.get()

        //타이틀이 Empty일때 리턴
        if (currentTitle == null) {
            emptyEvent.value = EmptyType.Title
            return
        }

        //내용이 Empty일때 리턴
        if (currentContent == null) {
            emptyEvent.value = EmptyType.Content
            return
        }

        //ProgressBar
        _dataLoading.value = true

        //이미지 선택 로직
        getPhoto(autoDocID, feedImgFile, action = { uri ->
            //feed 수정
            if (isDataLoaded) {

                feed?.let {
                    //uri 가 없을 경우 imgUrl 반환 아니면 null 반환
                    val imgUrl = if (isPhoto) {
                        uri?.toString() ?: it.imgUrl
                    } else {
                        null
                    }
                    val feed = Feed(it.fid, currentTitle, currentContent, imgUrl, it.createDate, System.currentTimeMillis())
                    updateFeed(feed)
                }

            } else {
                //feed 생성
                feed = Feed(autoDocID, currentTitle, currentContent, uri?.toString(), System.currentTimeMillis(), System.currentTimeMillis())
                insertFeed(feed!!)

            }
        }, error = {
            errorEvent.value = ErrorType.Image
            _dataLoading.value = false
        })
    }


    private fun getPhoto(
        fid: String,
        file: File?,
        action: (Uri?) -> Unit,
        error: (Exception) -> Unit
    ) {
        viewModelScope.launch {
                file?.let {
                    when (val result = feedUseCase.savePhoto(fid, it)) {
                        is ResultState.Success -> {
                            action(result.data)
                        }
                        is ResultState.Error -> {
                            error(result.exception)
                        }
                    }
                }?: action(null)
            }
        }

    //피드 생성 로직
    private fun insertFeed(feed: Feed) = viewModelScope.launch {
        feedUseCase.insertFeed(feed).let { resultState ->
            if (resultState is ResultState.Success) {
                _dataLoading.value = false
                if (resultState.data) {
                    successEvent.value = successType()
                } else {
                    errorEvent.value = errorType()
                }
            } else if (resultState is ResultState.Error) {
                errorEvent.value = errorType()
                _dataLoading.value = false
            }
        }
    }
    //피드 업데이트 로직
    private fun updateFeed(feed: Feed) = viewModelScope.launch {
        feedUseCase.updateFeed(feed).let { resultState ->
            if (resultState is ResultState.Success) {
                _dataLoading.value = false
                if (resultState.data) {
                    successEvent.value = successType()
                } else {
                    errorEvent.value = errorType()
                }
            } else if (resultState is ResultState.Error) {
                errorEvent.value = errorType()
                _dataLoading.value = false
            }
        }
    }

    private fun successType(): SuccessType =
        if (isDataLoaded) SuccessType.Update else SuccessType.Create

    private fun errorType(): ErrorType =
        if (isDataLoaded) ErrorType.UpdateFeed else ErrorType.CreateFeed

    enum class EmptyType { Title, Content }
    enum class ErrorType { Image, CreateFeed, GetFeed, UpdateFeed }
    enum class SuccessType { Create, Update }
}