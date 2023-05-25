package com.submission.appstory.api

import com.submission.appstory.RegisterRequest
import com.submission.appstory.response.LoginResponse
import com.submission.appstory.response.RegisterResponse
import com.submission.appstory.response.StoriesResponse
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
    @Headers("Authorization: Bearer {token}")
    fun getAllStories(
        @Path("token") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Call<StoriesResponse>
}