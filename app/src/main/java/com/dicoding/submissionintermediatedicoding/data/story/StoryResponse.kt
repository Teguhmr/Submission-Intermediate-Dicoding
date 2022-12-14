package com.dicoding.submissionintermediatedicoding.data.story

data class StoryListResponse(
    val error: Boolean,
    val listStory: List<Story>,
    val message: String
)
data class AddStoryResponse(
    val error: Boolean,
    val message: String
)