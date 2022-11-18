package com.submission.huda.storyapps.rest

import com.submission.huda.storyapps.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface Api {
    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") username : String,
        @Field("password") password : String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun regUser(
        @Field("name") name : String,
        @Field("email") username : String,
        @Field("password") password : String
    ):RegistrasiResponse

    @GET("stories")
    suspend fun getAllStory(
        @Header("Authorization") token: String,
        @Query("page") page : Int,
        @Query("size") size : Int
    ):StoryResponse

    @GET("stories")
    suspend fun getAllStoryPage(
        @Header("Authorization") token: String,
        @Query("page") page : Int,
        @Query("size") size : Int
    ): List<ListStoryItem>

    @GET("stories")
    suspend fun getAllStoryAndLocation(
        @Header("Authorization") token: String,
        @Query("location") location : Int
    ):StoryResponse

    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Header("Authorization") token: String,
        @Path("id") id : String
    ):DetailResponse

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Header("Authorization") token: String,
        @Part("lat") lat: Float,
        @Part("lon") lon: Float,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part,
    ):RegistrasiResponse
}