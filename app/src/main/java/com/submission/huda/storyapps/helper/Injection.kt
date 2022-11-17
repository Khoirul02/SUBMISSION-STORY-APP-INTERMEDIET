package com.submission.huda.storyapps.helper

import android.content.Context
import com.submission.huda.storyapps.data.StoryRepository
import com.submission.huda.storyapps.rest.RetrofitClient

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = RetrofitClient.getApiService()
        return StoryRepository.getInstance(apiService)
    }
}