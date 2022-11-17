package com.submission.huda.storyapps.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.submission.huda.storyapps.model.DetailResponse
import com.submission.huda.storyapps.model.LoginResponse
import com.submission.huda.storyapps.model.RegistrasiResponse
import com.submission.huda.storyapps.model.StoryResponse
import com.submission.huda.storyapps.rest.Api
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class StoryRepository(private val api: Api) {
    suspend fun actionLogin(username: String, password : String) : Result<LoginResponse> {
        return try {
            val response = api.loginUser(username, password)
            val data = LoginResponse(error = response.error, message = response.message, loginResult = response.loginResult)
            Result.Success(data)
        } catch (e: Exception) {
            Log.d("Story Repository", "Login: ${e.message.toString()} ")
            Result.Error(e.message.toString())
        }
    }

    fun listStory(token : String, page : Int, size : Int) : LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = api.getAllStory(token)
            val data = StoryResponse(error = response.error, message = response.message, listStory = response.listStory)
            emit(Result.Success(data))
        } catch (e: Exception) {
            Log.d("Story Repository", "Get List: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun listStoryBanner(token : String, page : Int, size : Int) : Result<StoryResponse> {
        return try {
            val response = api.getAllStory(token)
            val data = StoryResponse(error = response.error, message = response.message, listStory = response.listStory)
            Result.Success(data)
        } catch (e: Exception) {
            Log.d("Story Repository", "Get List: ${e.message.toString()} ")
            Result.Error(e.message.toString())
        }
    }

    fun listStoryLatLong(token : String, location : Int) : LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = api.getAllStoryAndLocation(token, location)
            val data = StoryResponse(error = response.error, message = response.message, listStory = response.listStory)
            emit(Result.Success(data))
        } catch (e: Exception) {
            Log.d("Story Repository", "Get List: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun actionRegistration(name : String, username: String, password: String) : Result<RegistrasiResponse> {
        return try {
            val response = api.regUser(name, username, password)
            val data = RegistrasiResponse(error = response.error, message = response.message)
            Result.Success(data)
        } catch (e: Exception) {
            Log.d("Story Repository", "Registration: ${e.message.toString()} ")
            Result.Error(e.message.toString())
        }
    }

    suspend fun actionAddStory(token: String, lat : Float, lon : Float,  description: String , photo: File) : Result<RegistrasiResponse> {
        return try {
            val des = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = photo.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                photo.name,
                requestImageFile
            )

            val response = api.postStory(token, lat, lon ,des,imageMultipart)
            val data = RegistrasiResponse(error = response.error, message = response.message)
            Result.Success(data)
        } catch (e: Exception) {
            Log.d("Story Repository", "Registration: ${e.message.toString()} ")
            Result.Error(e.message.toString())
        }
    }

    fun actionDetailStory(token : String ,id : String) : LiveData<Result<DetailResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = api.getDetailStory(token, id)
            val data = DetailResponse(error = response.error, message = response.message, story = response.story)
            emit(Result.Success(data))
        } catch (e: Exception) {
            Log.d("Story Repository", "Login: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }


    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            api: Api
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(api)
            }.also { instance = it }
    }
}