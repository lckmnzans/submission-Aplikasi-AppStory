package com.submission.appstory.api

import com.submission.appstory.RegisterRequest
import com.submission.appstory.response.AddStoryResponse
import com.submission.appstory.response.LoginResponse
import com.submission.appstory.response.RegisterResponse
import com.submission.appstory.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("register")
    fun register(
        @Body request: RegisterRequest
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    fun getAllStories(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
    ): Call<StoriesResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<AddStoryResponse>
}