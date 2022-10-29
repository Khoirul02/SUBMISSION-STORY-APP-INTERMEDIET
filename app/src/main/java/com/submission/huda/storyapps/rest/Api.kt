package com.submission.huda.storyapps.rest

import com.submission.huda.storyapps.model.DetailResponse
import com.submission.huda.storyapps.model.LoginResponse
import com.submission.huda.storyapps.model.RegistrasiResponse
import com.submission.huda.storyapps.model.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Api {
    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") username : String,
        @Field("password") password : String
    ):Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun regUser(
        @Field("name") name : String,
        @Field("email") username : String,
        @Field("password") password : String
    ):Call<RegistrasiResponse>

    @GET("stories")
    fun getAllStory(
        @Header("Authorization") token: String,
    ):Call<StoryResponse>

    @GET("stories/{id}")
    fun getDetailStory(
        @Header("Authorization") token: String,
        @Path("id") id : String
    ):Call<DetailResponse>

    @Multipart
    @POST("stories")
    fun postStory(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part,
    ):Call<RegistrasiResponse>
}