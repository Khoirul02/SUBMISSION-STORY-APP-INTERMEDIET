package com.submission.huda.storyapps.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.submission.huda.storyapps.model.RegistrasiResponse
import com.submission.huda.storyapps.rest.RetrofitClient
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddViewModel() : ViewModel() {
    private val _addResult = MutableLiveData<AddResult>()
    val addResult: LiveData<AddResult> = _addResult

    fun addStory(token: String, description: String, photo: File){
        val des = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = photo.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            photo.name,
            requestImageFile
        )
        RetrofitClient.instance.postStory(token,des,imageMultipart)
            .enqueue(object : Callback<RegistrasiResponse>{
                override fun onResponse(
                    call: Call<RegistrasiResponse>?,
                    response: Response<RegistrasiResponse>?
                ) {
                    val result = response!!.body()
                    if (result !== null){
                        if (result.error == false) {
                            _addResult.value = AddResult(success = AddView(message = result.message!!))
                        } else {
                            _addResult.value = AddResult(error = result.message)
                        }
                    } else {
                        _addResult.value = AddResult(error = "Gagal Mengirim Data")
                    }
                }

                override fun onFailure(call: Call<RegistrasiResponse>?, t: Throwable?) {
                    _addResult.value = AddResult(error = "Gagal Mengirim Data")
                }
            })
    }
}