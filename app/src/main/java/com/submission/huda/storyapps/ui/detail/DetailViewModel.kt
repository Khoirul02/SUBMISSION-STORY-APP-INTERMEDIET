package com.submission.huda.storyapps.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.submission.huda.storyapps.model.DetailResponse
import com.submission.huda.storyapps.rest.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel() : ViewModel() {
    private val _detailResult = MutableLiveData<DetailResult>()
    val detailResult: LiveData<DetailResult> = _detailResult
    fun getDetail(token : String ,id : String){
        RetrofitClient.instance.getDetailStory(token,id)
            .enqueue(object : Callback<DetailResponse>{
                override fun onResponse(
                    call: Call<DetailResponse>?,
                    response: Response<DetailResponse>?
                ) {
                    val result = response!!.body()
                    if (result !== null){
                        if (result.error == false) {
                            _detailResult.value = DetailResult(success = DetailView(detail = result.story!!))
                        } else {
                            _detailResult.value = DetailResult(error = result.message)
                        }
                    } else {
                        _detailResult.value = DetailResult(error = "Gagal Memuat Data")
                    }
                }

                override fun onFailure(call: Call<DetailResponse>?, t: Throwable?) {
                    _detailResult.value = DetailResult(error = "Gagal Memuat Data")
                }

            })
    }
}