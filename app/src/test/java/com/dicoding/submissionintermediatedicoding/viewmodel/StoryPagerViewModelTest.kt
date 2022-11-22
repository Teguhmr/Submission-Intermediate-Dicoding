package com.dicoding.submissionintermediatedicoding.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.dicoding.submissionintermediatedicoding.adapter.StoryAdapter
import com.dicoding.submissionintermediatedicoding.data.repository.StoryRepository
import com.dicoding.submissionintermediatedicoding.data.story.Story
import com.dicoding.submissionintermediatedicoding.utils.DummyData
import com.dicoding.submissionintermediatedicoding.utils.MainCoroutineRule
import com.dicoding.submissionintermediatedicoding.utils.PagedTestDataSources
import com.dicoding.submissionintermediatedicoding.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
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
class StoryPagerViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyViewModel: StoryPagerViewModel

    @Before
    fun setUp() {
        storyViewModel = StoryPagerViewModel(storyRepository)
    }

    @Test
    fun `when get stories should not Null`() = runTest {
        val dummyListStory = DummyData.generateDummyStoryResponseData()
        val storiesData = PagedTestDataSources.itemSnapshot(dummyListStory)

        val stories = MutableLiveData<PagingData<Story>>()
        stories.value = storiesData

        Mockito.`when`(storyRepository.getStory()).thenReturn(stories)

        val actualData = storyViewModel.storyList.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = PagedTestDataSources.listUpdateCallback,
            mainDispatcher = mainCoroutineRule.dispatcher,
            workerDispatcher = mainCoroutineRule.dispatcher
        )

        differ.submitData(actualData)
        advanceUntilIdle()

        Mockito.verify(storyRepository).getStory()

        assertNotNull(actualData)
        assertEquals(dummyListStory.size, differ.snapshot().size)
        assertEquals(dummyListStory[0].name, differ.snapshot()[0]?.name)
    }
}