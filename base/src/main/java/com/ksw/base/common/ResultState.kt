package com.ksw.base.common

import java.lang.Exception

sealed class ResultState <out R>{

    data class Success<out T>(val data:T): ResultState<T>()

    data class Error(val exception: Exception):
        ResultState<Nothing>()

    object Loading: ResultState<Nothing>()

    object EmptyDocument: ResultState<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
            else -> ""
        }
    }
}