package com.submission.huda.storyapps.ui.detail

import androidx.lifecycle.ViewModel
import com.submission.huda.storyapps.data.StoryRepository

class DetailViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getDetail(token: String, id: String) = storyRepository.actionDetailStory(token, id)
}