package com.submission.huda.storyapps.ui.maps
import androidx.lifecycle.ViewModel
import com.submission.huda.storyapps.data.StoryRepository

class MapsActivityViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getAllStoryLatLong(token : String, location : Int) = storyRepository.listStoryLatLong(token, location)
}