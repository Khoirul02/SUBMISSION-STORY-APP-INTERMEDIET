package com.submission.huda.storyapps.ui.add
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.submission.huda.storyapps.data.Result
import com.submission.huda.storyapps.data.StoryRepository
import kotlinx.coroutines.launch
import java.io.File

class AddViewModel (private  val storyRepository: StoryRepository) : ViewModel() {
    private val _addResult = MutableLiveData<AddResult>()
    val addResult: LiveData<AddResult> = _addResult
    fun addStory(token: String, lat : Float, lon : Float,  description: String , photo: File){
        viewModelScope.launch {
            val result = storyRepository.actionAddStory(token, lat, lon, description, photo)
            if (result is Result.Success){
                if (result.data.error == false) {
                    _addResult.value = AddResult(success = AddView(message = result.data.message!!))
                } else {
                    _addResult.value = AddResult(error = result.data.message)
                }
            } else {
                _addResult.value = AddResult(error = "Gagal Menambah Data!!")
            }
        }
    }
}