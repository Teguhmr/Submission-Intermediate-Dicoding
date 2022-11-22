package com.dicoding.submissionintermediatedicoding.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submissionintermediatedicoding.data.repository.UploadRepository
import com.dicoding.submissionintermediatedicoding.data.story.AddStoryResponse
import com.dicoding.submissionintermediatedicoding.utils.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadStoryViewModel(private val uploadRepository: UploadRepository): ViewModel() {
    val storyUpload: LiveData<Resource<AddStoryResponse>> by lazy {
        uploadRepository.storyUpload
    }

    fun uploadStory(
        token: String,
        image: MultipartBody.Part,
        description: RequestBody,
        latitude: Double?,
        longitude: Double?
    ) = uploadRepository.uploadStory(token, image, description, latitude, longitude)

}