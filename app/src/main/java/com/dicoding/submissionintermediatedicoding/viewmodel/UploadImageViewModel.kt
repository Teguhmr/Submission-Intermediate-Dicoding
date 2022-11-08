package com.dicoding.submissionintermediatedicoding.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submissionintermediatedicoding.data.remote.RetrofitConfig
import com.dicoding.submissionintermediatedicoding.data.story.AddStoryResponse
import com.dicoding.submissionintermediatedicoding.utils.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadImageViewModel: ViewModel() {
    private val _storyUpload = MutableLiveData<Resource<AddStoryResponse>>()
    val storyUpload: LiveData<Resource<AddStoryResponse>> = _storyUpload

    fun uploadStory(token: String, image: MultipartBody.Part, description: RequestBody){
        _storyUpload.value = Resource.loading(null)
        RetrofitConfig.getApiService().uploadStory(token, image, description).
        enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(call: Call<AddStoryResponse>, response: Response<AddStoryResponse>) {
                if (response.isSuccessful) {
                    _storyUpload.value = Resource.success(response.body())
                } else {
                    _storyUpload.value = Resource.error(response.message(), null)
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                _storyUpload.value = Resource.error(t.message.toString(), null)

            }

        })
    }

}