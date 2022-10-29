package com.submission.huda.storyapps.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.submission.huda.storyapps.model.StoryResponse
import com.submission.huda.storyapps.rest.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel() : ViewModel() {
    private val _storyResult = MutableLiveData<StoryResult>()
    val storyResult: LiveData<StoryResult> = _storyResult

    fun getAllStory(token : String){
        RetrofitClient.instance.getAllStory(token)
            .enqueue(object : Callback<StoryResponse>{
                override fun onResponse(
                    call: Call<StoryResponse>?,
                    response: Response<StoryResponse>?
                ) {
                    val result = response!!.body()
                    if (result !== null){
                        if (result.error == false) {
                            _storyResult.value = StoryResult(success = ListStoryView(listStory = result.listStory ))
                        } else {
                            _storyResult.value = StoryResult(error = result.message)
                        }
                    } else {
                        _storyResult.value = StoryResult(error = "Gagal Memuat Data")
                    }
                }

                override fun onFailure(call: Call<StoryResponse>?, t: Throwable?) {
                    _storyResult.value = StoryResult(error = "Gagal Memuat Data")
                }

            })
    }
}