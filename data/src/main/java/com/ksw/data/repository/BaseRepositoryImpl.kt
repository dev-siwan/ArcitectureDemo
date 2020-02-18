package com.ksw.data.repository


import com.ksw.base.common.ResultState
import com.ksw.domain.repository.BaseRepository


abstract class BaseRepositoryImpl : BaseRepository {

    fun <C, R> perform(
        remoteResult: ResultState<C>,
        data: (C) -> ResultState.Success<R>,
        exception: (Exception) -> ResultState.Error
    ): ResultState<R> {
        return when (remoteResult) {
            is ResultState.Success -> data(remoteResult.data)
            is ResultState.Error -> exception(remoteResult.exception)
            else ->  exception(Exception("data is null"))
        }
    }


}