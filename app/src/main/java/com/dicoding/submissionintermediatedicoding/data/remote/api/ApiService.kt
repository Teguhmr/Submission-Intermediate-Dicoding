package com.dicoding.submissionintermediatedicoding.data.remote.api


import com.dicoding.submissionintermediatedicoding.data.auth.UserLoginResponse
import com.dicoding.submissionintermediatedicoding.data.auth.UserRegisterResponse
import com.dicoding.submissionintermediatedicoding.data.story.AddStoryResponse
import com.dicoding.submissionintermediatedicoding.data.story.StoryListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun userRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<UserRegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun userLogin(
        @Field("email") email: String,
        @Field("password")password: String
    ): Call<UserLoginResponse>

    @GET("stories")
    fun getAllStory(
        @Header("Authorization") auth: String
    ): Call<StoryListResponse>

    @GET("stories?location=1")
    fun getStoryListLocation(
        @Header("Authorization") auth: String,
        @Query("size") size: Int
    ): Call<StoryListResponse>

    @Multipart
    @POST("stories")
    fun uploadStory(
        @Header("Authorization") auth: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") latitude: Float?,
        @Part("lon") longitude: Float?,
    ): Call<AddStoryResponse>

    @GET("stories")
    suspend fun getStoryList(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoryListResponse
}