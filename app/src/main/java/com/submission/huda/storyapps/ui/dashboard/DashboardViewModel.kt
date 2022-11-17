package com.submission.huda.storyapps.ui.dashboard

import androidx.lifecycle.ViewModel
import com.submission.huda.storyapps.data.StoryRepository

class DashboardViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getAllStory(token: String, page: Int, size: Int) =
        storyRepository.listStory(token, page, size)

}