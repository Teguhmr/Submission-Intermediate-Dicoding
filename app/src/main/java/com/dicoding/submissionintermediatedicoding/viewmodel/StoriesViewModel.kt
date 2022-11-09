package com.dicoding.submissionintermediatedicoding.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submissionintermediatedicoding.data.remote.api.RetrofitConfig
import com.dicoding.submissionintermediatedicoding.data.story.Story
import com.dicoding.submissionintermediatedicoding.data.story.StoryListResponse
import com.dicoding.submissionintermediatedicoding.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoriesViewModel: ViewModel() {
    private val _listStoryData = MutableLiveData<Resource<ArrayList<Story>>>()
    val listStoryData : LiveData<Resource<ArrayList<Story>>> = _listStoryData

    fun getAllStoriesData(auth: String){
        _listStoryData.value = Resource.loading(null)
        RetrofitConfig.getApiService().getAllStory("Bearer $auth")
            .enqueue(object : Callback<StoryListResponse> {
                override fun onResponse(
                    call: Call<StoryListResponse>,
                    response: Response<StoryListResponse>
                ) {
                    if (response.isSuccessful){
                        if (response.body()!!.listStory.isNotEmpty()){
                            _listStoryData.value = Resource.success(response.body()!!.listStory)
                        } else {
                            _listStoryData.value = Resource.empty(null)
                        }
                    }else{
                        _listStoryData.value = Resource.error(response.message(), null)
                    }

                }

                override fun onFailure(call: Call<StoryListResponse>, t: Throwable) {
                    _listStoryData.value = Resource.error(t.message.toString(), null)
                }

            })
    }
}