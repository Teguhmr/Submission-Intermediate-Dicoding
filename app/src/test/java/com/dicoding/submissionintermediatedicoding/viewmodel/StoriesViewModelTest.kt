package com.dicoding.submissionintermediatedicoding.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.submissionintermediatedicoding.data.repository.StoryRepository
import com.dicoding.submissionintermediatedicoding.data.story.Story
import com.dicoding.submissionintermediatedicoding.utils.*
import com.dicoding.submissionintermediatedicoding.utils.DummyData.dummyMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoriesViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyViewModel: StoriesViewModel

    @Before
    fun setUp() {
        storyViewModel = StoriesViewModel(storyRepository)
    }

    @Test
    fun `when get isLoading from viewModel should return LiveData Boolean`() {
        val expectedData = MutableLiveData<Resource<List<Story>>>()
        expectedData.value = Resource.loading(null)

        Mockito.`when`(storyRepository.getStoryListLocation()).thenReturn(expectedData)

        val actualData = storyViewModel.getStory.getOrAwaitValue()

        Mockito.verify(storyRepository).getStoryListLocation()
        assertTrue(actualData.status == Status.LOADING)
        assertEquals(actualData, expectedData.value)
    }

    @Test
    fun `when get message from viewModel should return LiveData Boolean`() {

        val expectedData = MutableLiveData<Resource<List<Story>>>()
        expectedData.value = Resource.error(dummyMessage, null)

        Mockito.`when`(storyRepository.getStoryListLocation()).thenReturn(expectedData)

        val actualData = storyViewModel.getStory.getOrAwaitValue()

        Mockito.verify(storyRepository).getStoryListLocation()
        assertTrue(actualData.status == Status.ERROR)
        assertEquals(actualData, expectedData.value)
    }

    @Test
    fun `when get stories Location get success`() = runTest {
        val dummyListStory = DummyData.generateDummyStoryResponseData()

        val stories = MutableLiveData<Resource<List<Story>>>()
        stories.value = Resource.success(dummyListStory)

        Mockito.`when`(storyRepository.getStoryListLocation()).thenReturn(stories)

        val actualData = storyViewModel.getStory.getOrAwaitValue()

        Mockito.verify(storyRepository).getStoryListLocation()

        assertTrue(actualData.status == Status.SUCCESS)
        assertEquals(actualData, stories.value)
        assertNotNull(actualData.data!![0].lat)
        assertNotNull(actualData.data!![0].lon)
    }
}