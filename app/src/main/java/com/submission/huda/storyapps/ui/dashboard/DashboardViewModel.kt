package com.submission.huda.storyapps.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.submission.huda.storyapps.data.StoryRepository
import com.submission.huda.storyapps.model.ListStoryItem

class DashboardViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getAllStory(token: String): LiveData<PagingData<ListStoryItem>> =
        storyRepository.listStoryPage(token).cachedIn(viewModelScope)
}