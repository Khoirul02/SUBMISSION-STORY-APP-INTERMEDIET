package com.submission.huda.storyapps.data
sealed class Result<out T : Any>  {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}