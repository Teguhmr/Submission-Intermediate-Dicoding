package com.dicoding.submissionintermediatedicoding.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submissionintermediatedicoding.data.repository.StoryRepository
import com.dicoding.submissionintermediatedicoding.data.story.Story
import com.dicoding.submissionintermediatedicoding.utils.Constants
import com.dicoding.submissionintermediatedicoding.utils.Resource

class StoriesViewModel(private val storyRepository: StoryRepository): ViewModel() {
    val coordinateTemp = MutableLiveData(Constants.indonesiaLocation)

    val getStory: LiveData<Resource<List<Story>>> by lazy {
        storyRepository.getStoryListLocation()
    }
    fun getStoryWithLocation() = storyRepository.getStoryListLocation()
}